package helvidios.search.indexer;

import java.util.concurrent.*;
import helvidios.search.linguistics.*;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.*;
import java.util.*;

public class Indexer implements AutoCloseable {
    private final int N_INVERTERS = 10;

    private final BlockingQueue<Integer> docQueue;
    private final DocumentRepository docRepo;
    private final ExecutorService pool;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;

    public Indexer(DocumentRepository docRepo, Tokenizer tokenizer, Lemmatizer lemmatizer) {
        this.docQueue = new LinkedBlockingDeque<>();
        this.docRepo = docRepo;
        this.tokenizer = tokenizer;
        this.lemmatizer = lemmatizer;
        this.pool = Executors.newFixedThreadPool(N_INVERTERS);
    }

    public void buildIndex() throws InterruptedException, ExecutionException {

        System.out.println("Indexing started...");
        
        // populate document queue with document ids to be indexed
        docRepo.iterator().forEachRemaining(doc -> docQueue.add(doc.getId()));

        // run concurrent inverters
        List<Inverter> inverters = new ArrayList<>();
        for(int i = 0; i < N_INVERTERS; i++){
            inverters.add(new Inverter(
                docQueue,
                docRepo,
                tokenizer,
                lemmatizer
            ));
        }
        List<Future<Map<String, SortedSet<Term>>>> jobs = pool.invokeAll(inverters);

        Map<String, SortedSet<Term>> index = new HashMap<>();
        for(Future<Map<String, SortedSet<Term>>> job : jobs){
            System.out.println(job.get());
        }

        System.out.println("Indexing completed.");
    }

    @Override
    public void close() throws Exception {
        pool.shutdown();
        pool.awaitTermination(5000, TimeUnit.MILLISECONDS);
    }
}