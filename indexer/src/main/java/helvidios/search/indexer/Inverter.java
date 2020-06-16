package helvidios.search.indexer;

import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;
import java.util.concurrent.*;

class Inverter implements Callable<Map<String, SortedSet<Term>>> {

    private final BlockingQueue<Integer> docQueue;
    private final DocumentRepository docRepo;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;
    private final Map<String, SortedSet<Term>> index;

    Inverter(
        BlockingQueue<Integer> docQueue,
        DocumentRepository docRepo,
        Tokenizer tokenizer, 
        Lemmatizer lemmatizer){
        this.docQueue = docQueue;
        this.docRepo = docRepo;
        this.tokenizer = tokenizer;
        this.lemmatizer = lemmatizer;
        this.index = new HashMap<>();
    }

    @Override
    public Map<String, SortedSet<Term>> call() throws Exception {
        final long id = Thread.currentThread().getId();
        int nDocsProcessed = 0;
        System.out.printf("Inverter %d started.\n", id);
        while (!docQueue.isEmpty()) {
            try {
                int docId = docQueue.remove();
                HtmlDocument doc = docRepo.get(new DocId(docId));
                List<String> tokens = tokenizer.getTokens(doc.getContent());
                
                // generate frequency map for terms in this document
                Map<String, Integer> freq = new HashMap<>();
                for (String term : lemmatizer.getLemmas(tokens)) {
                    freq.put(term, freq.getOrDefault(term, 0) + 1);
                }

                // merge the frequency map into index
                for(String term : freq.keySet()){
                    index.computeIfAbsent(term, (key) -> new TreeSet<>(
                        (t1, t2) -> Integer.compare(t1.getDocId(), t2.getDocId())
                    )).add(
                        new Term(term, docId, freq.get(term))
                    );
                }

                nDocsProcessed++;
                System.out.printf("Inverter %d: Processed %s. Found %d terms.\n", id, doc.toString(), tokens.size());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.printf("Inverter %d completed. Processed %d docs.\n", id, nDocsProcessed);
        return index;
    }
}