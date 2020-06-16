package helvidios.search.indexer;

import java.util.concurrent.*;
import helvidios.search.linguistics.*;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.*;
import java.util.*;

public class Indexer implements AutoCloseable {
    private final int N_DOC_PROCESSORS = 10;

    private final BlockingDeque<Term> terms;
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
        this.terms = new LinkedBlockingDeque<>();
        this.pool = Executors.newFixedThreadPool(N_DOC_PROCESSORS);
    }

    public void buildIndex() {

        System.out.println("Indexing started...");
        
        // populate document queue with document ids to be indexed
        docRepo.iterator().forEachRemaining(doc -> docQueue.add(doc.getId()));

        // run concurrent document processors - extract terms from each document
        List<CompletableFuture<?>> jobs = new ArrayList<>();
        for (int i = 0; i < N_DOC_PROCESSORS; i++) {
            jobs.add(CompletableFuture.runAsync(() ->
            {

                System.out.printf("Document processor %d started.\n", Thread.currentThread().getId());

                while (!docQueue.isEmpty()) {
                    try {
                        int docId = docQueue.remove();
                        HtmlDocument doc = docRepo.get(new DocId(docId));
                        List<String> tokens = tokenizer.getTokens(doc.getContent());
                        for (String term : lemmatizer.getLemmas(tokens)) {
                            terms.add(new Term(term, docId));
                        }
                        System.out.println("Processed " + doc.toString());
                    } catch (Exception ex) {
                        System.out.println(ex.toString());
                    }
                }

                System.out.printf("Document processor %d terminated.\n", Thread.currentThread().getId());

            }, pool));
        }

        // wait for all document processors to finish
        CompletableFuture.allOf(jobs.toArray(new CompletableFuture[0])).join();

        // sort terms by term and docId
        List<Term> sortedTerms = new ArrayList<>(terms);
        sortedTerms.sort((t1, t2) -> {
            if(t1.getTerm().equals(t2.getTerm())){
                return Integer.compare(t1.getDocId(), t2.getDocId());
            }
            return t1.getTerm().compareTo(t2.getTerm());
        });

        System.out.println("Indexing completed.");
    }

    @Override
    public void close() throws Exception {
        pool.shutdown();
        pool.awaitTermination(5000, TimeUnit.MILLISECONDS);
    }
}