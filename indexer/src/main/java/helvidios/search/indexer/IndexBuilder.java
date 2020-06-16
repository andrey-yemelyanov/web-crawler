package helvidios.search.indexer;

import java.util.concurrent.*;
import helvidios.search.linguistics.*;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.*;
import java.util.*;

public class IndexBuilder implements AutoCloseable {
    private final int N_CONCURRENT_INDEXERS = 10;

    private final BlockingQueue<Integer> docQueue;
    private final DocumentRepository docRepo;
    private final ExecutorService pool;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;

    public IndexBuilder(DocumentRepository docRepo, Tokenizer tokenizer, Lemmatizer lemmatizer) {
        this.docQueue = new LinkedBlockingDeque<>();
        this.docRepo = docRepo;
        this.tokenizer = tokenizer;
        this.lemmatizer = lemmatizer;
        this.pool = Executors.newFixedThreadPool(N_CONCURRENT_INDEXERS);
    }

    public void build() throws InterruptedException, ExecutionException {

        System.out.println("Indexing started...");
        
        // populate document queue with document ids to be indexed
        docRepo.iterator().forEachRemaining(doc -> docQueue.add(doc.getId()));

        // run concurrent indexers
        List<Indexer> indexers = new ArrayList<>();
        for(int i = 0; i < N_CONCURRENT_INDEXERS; i++){
            indexers.add(new Indexer(
                docQueue,
                docRepo,
                tokenizer,
                lemmatizer
            ));
        }
        List<Future<Map<String, List<Term>>>> jobs = pool.invokeAll(indexers);

        // build total index from sub-indexes returned by the concurrent indexers
        InMemoryIndex index = new InMemoryIndex();
        for(Future<Map<String, List<Term>>> job : jobs){
            index.append(job.get());
        }

        System.out.println("Indexing completed.");
        System.out.printf("Index size: %d\n%s\n", index.getIndex().size(), index);
    }

    @Override
    public void close() throws Exception {
        pool.shutdown();
        pool.awaitTermination(5000, TimeUnit.MILLISECONDS);
    }
}