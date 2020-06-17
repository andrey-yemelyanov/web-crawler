package helvidios.search.indexer;

import java.util.concurrent.*;
import org.apache.logging.log4j.Logger;
import helvidios.search.index.Posting;
import helvidios.search.index.Term;
import helvidios.search.index.storage.IndexRepository;
import helvidios.search.linguistics.*;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.*;
import java.util.*;

/**
 * Represents a main orchestrator that builds the index over the entire document set.
 */
public class IndexBuilder implements AutoCloseable {
    private final int N_CONCURRENT_INDEXERS = 50;

    private final BlockingQueue<Integer> docQueue;
    private final DocumentRepository docRepo;
    private final IndexRepository indexRepo;
    private final ExecutorService pool;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;
    private final Logger log;

    /**
     * Initializes a new instance of {@link IndexBuilder}.
     * @param docRepo document storage where the documents to be indexed are stored
     * @param tokenizer tokenizer of text in a document
     * @param lemmatizer lexical analysis lemmatizer
     * @param indexRepo index storage where the index will be saved
     * @param log logging component
     */
    public IndexBuilder(
        DocumentRepository docRepo,
        IndexRepository indexRepo, 
        Tokenizer tokenizer, 
        Lemmatizer lemmatizer,
        Logger log) {
        this.docQueue = new LinkedBlockingDeque<>();
        this.docRepo = docRepo;
        this.tokenizer = tokenizer;
        this.lemmatizer = lemmatizer;
        this.pool = Executors.newFixedThreadPool(N_CONCURRENT_INDEXERS);
        this.log = log;
        this.indexRepo = indexRepo;
    }

    /**
     * Builds an index over the entire document set stored in the document repository.
     * The method blocks until the complete index has been built and saved to storage.
     * This method launches multiple concurrent indexers that built their subset of the
     * complete index. Consequently all index subsets are merged into the complete index
     * and the index is stored in persistent storage.
     * @throws Exception
     */
    public void build() throws Exception {

        System.out.println("Indexing started...");
        log.info("Indexing started...");
        
        // populate document queue with document ids to be indexed
        docRepo.iterator().forEachRemaining(doc -> docQueue.add(doc.getId()));

        System.out.printf("Indexing %d documents...", docQueue.size());
        log.info("Indexing {} documents...", docQueue.size());

        // run concurrent indexers
        List<Indexer> indexers = new ArrayList<>();
        for(int i = 0; i < N_CONCURRENT_INDEXERS; i++){
            indexers.add(new Indexer(
                docQueue,
                docRepo,
                tokenizer,
                lemmatizer,
                log
            ));
        }

        InMemoryIndex index = buildIndex(pool.invokeAll(indexers));

        storeIndex(index);

        log.info("Indexing completed.");
        log.info("Index size: {}", index.size());
        log.info("Vocabulary:\n{}", index.getVocabulary());
        System.out.printf("Indexing completed.\nIndex size: %d\n", index.size());
    }

    private static InMemoryIndex buildIndex(
        List<Future<Map<Term, List<Posting>>>> jobs) throws Exception{
        
        InMemoryIndex index = new InMemoryIndex();
        for(Future<Map<Term, List<Posting>>> job : jobs){
            index.append(job.get());
        }

        return index;
    }

    private void storeIndex(InMemoryIndex index){
        for(Term term : index.getVocabulary()){
            indexRepo.addTerm(term, index.getPostingsList(term));
        }
    }

    @Override
    public void close() throws Exception {
        pool.shutdown();
        pool.awaitTermination(5000, TimeUnit.MILLISECONDS);
    }
}