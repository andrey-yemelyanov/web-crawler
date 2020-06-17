package helvidios.search.indexer;

import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;
import java.util.concurrent.*;

import org.apache.logging.log4j.Logger;

class Indexer implements Callable<Map<String, List<Term>>> {

    private final BlockingQueue<Integer> docQueue;
    private final DocumentRepository docRepo;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;
    private final Map<String, SortedSet<Term>> index;
    private final Logger log;

    Indexer(
        BlockingQueue<Integer> docQueue,
        DocumentRepository docRepo,
        Tokenizer tokenizer, 
        Lemmatizer lemmatizer,
        Logger log){
        this.docQueue = docQueue;
        this.docRepo = docRepo;
        this.tokenizer = tokenizer;
        this.lemmatizer = lemmatizer;
        this.index = new HashMap<>();
        this.log = log;
    }

    @Override
    public Map<String, List<Term>> call() throws Exception {
        final long id = Thread.currentThread().getId();
        int nDocsProcessed = 0;
        log.info("Indexer {} started.", id);
        while (!docQueue.isEmpty()) {
            try {
                Integer docId = docQueue.poll();
                if(docId == null) break;
                HtmlDocument doc = docRepo.get(new DocId(docId));
                List<String> tokens = tokenizer.getTokens(doc.getContent());
                List<String> lemmas = lemmatizer.getLemmas(tokens);
                
                // generate frequency map for terms in this document
                Map<String, Integer> freq = new HashMap<>();
                for (String term : lemmas) {
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
                log.info("Indexer {}: Indexed {}. Found {} terms.", id, doc.toString(), tokens.size());
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Indexer failed", ex);
            }
        }
        log.info("Indexer {} completed. Processed {} docs.", id, nDocsProcessed);
        return toPostingsLists(index);
    }

    private static Map<String, List<Term>> toPostingsLists(Map<String, SortedSet<Term>> index){
        Map<String, List<Term>> map = new HashMap<>();
        for(String key : index.keySet()){
            for(Term term : index.get(key)){
                map.computeIfAbsent(key, k -> new LinkedList<>()).add(term);
            }
        }
        return map;
    }
}