package helvidios.search.indexer;

import java.util.concurrent.*;

import org.apache.logging.log4j.Logger;

import helvidios.search.linguistics.*;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.*;
import java.util.*;

public class IndexBuilder implements AutoCloseable {
    private final int N_CONCURRENT_INDEXERS = 100;

    private final BlockingQueue<Integer> docQueue;
    private final DocumentRepository docRepo;
    private final ExecutorService pool;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;
    private final Logger log;

    public IndexBuilder(DocumentRepository docRepo, Tokenizer tokenizer, Lemmatizer lemmatizer, Logger log) {
        this.docQueue = new LinkedBlockingDeque<>();
        this.docRepo = docRepo;
        this.tokenizer = tokenizer;
        this.lemmatizer = lemmatizer;
        this.pool = Executors.newFixedThreadPool(N_CONCURRENT_INDEXERS);
        this.log = log;
    }

    public void build() throws InterruptedException, ExecutionException {

        System.out.println("Indexing started...");
        log.info("Indexing started...");
        
        // populate document queue with document ids to be indexed
        docRepo.iterator().forEachRemaining(doc -> docQueue.add(doc.getId()));

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
        List<Future<Map<String, List<Term>>>> jobs = pool.invokeAll(indexers);

        // build total index from sub-indexes returned by the concurrent indexers
        InMemoryIndex index = new InMemoryIndex();
        for(Future<Map<String, List<Term>>> job : jobs){
            index.append(job.get());
        }

        log.info("Indexing completed.");
        log.info("Index size: {}", index.getIndex().size());
        log.info("Dictionary:\n{}", index.getIndex().keySet());
        System.out.printf("Indexing completed.\nIndex size: %d\n", index.getIndex().size());
    }

    @Override
    public void close() throws Exception {
        pool.shutdown();
        pool.awaitTermination(5000, TimeUnit.MILLISECONDS);
    }
}