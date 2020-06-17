package helvidios.search.indexer;

import helvidios.search.index.Posting;
import helvidios.search.index.Term;
import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;
import java.util.concurrent.*;
import org.apache.logging.log4j.Logger;

/**
 * This component creates a full index for a subset of documents.
 */
class Indexer implements Callable<Map<Term, List<Posting>>> {

    private final BlockingQueue<Integer> docQueue;
    private final DocumentRepository docRepo;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;
    private final Map<Term, SortedSet<Posting>> index;
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
    public Map<Term, List<Posting>> call() throws Exception {
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
                for(String word : freq.keySet()){
                    Term term = new Term(word);
                    index.computeIfAbsent(term, (key) -> new TreeSet<>(
                        (t1, t2) -> Integer.compare(t1.getDocId(), t2.getDocId())
                    )).add(
                        new Posting(term, docId, freq.get(word))
                    );
                }

                nDocsProcessed++;
                log.info("Indexer {}: Indexed {}. Found {} terms.", id, doc.toString(), tokens.size());
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error(String.format("Indexer %d failed.", id), ex);
            }
        }
        log.info("Indexer {} completed. Processed {} docs.", id, nDocsProcessed);
        return toPostingsLists(index);
    }

    private static Map<Term, List<Posting>> toPostingsLists(Map<Term, SortedSet<Posting>> index){
        Map<Term, List<Posting>> map = new HashMap<>();
        for(Term term : index.keySet()){
            for(Posting posting : index.get(term)){
                map.computeIfAbsent(term, k -> new LinkedList<>()).add(posting);
            }
        }
        return map;
    }
}