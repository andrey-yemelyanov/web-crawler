package helvidios.search.searcher;

import org.apache.logging.log4j.Logger;
import helvidios.search.index.Index;
import helvidios.search.index.Posting;
import helvidios.search.index.storage.IndexRepository;
import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.storage.DocId;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.HtmlDocument;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;
import java.util.stream.Collectors;

public class Searcher {
    
    private final Index index;
    private final Logger log;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;
    private final DocumentRepository docRepo;

    /**
     * Initializes a new instance of the searcher.
     * @param indexRepo implementation of {@link IndexRepository} interface which provides access to the index
     * @param docRepo document repository where raw documents are stored
     * @param tokenizer query tokenizer
     * @param lemmatizer query lemmatizer
     * @param log log component
     */
    public Searcher(
        IndexRepository indexRepo,
        DocumentRepository docRepo,
        Tokenizer tokenizer,
        Lemmatizer lemmatizer, 
        Logger log){
        this.log = log;
        this.index = new Index(indexRepo, log);
        this.tokenizer = tokenizer;
        this.lemmatizer = lemmatizer;
        this.docRepo = docRepo;
    }

    /**
     * Searches for top K documents with the best match for the supplied query.
     * @param query query in free text
     * @param k number of top documents to return
     * @return top K best matching documents
     * @throws Exception
     */
    public List<Match> search(String query, int k) throws Exception {
        log.info("Searching for query '{}' (return top {} matches) ...", query, k);
        List<String> tokens = tokenizer.getTokens(query);
        if(tokens.isEmpty()) {
            log.info("Empty query. Nothing found.");
            return Arrays.asList();
        }
        List<String> queryTerms = lemmatizer.getLemmas(tokens);
        List<Match> matches = getTopKMatches(computeDocumentScores(queryTerms), k);
        log.info("Found {} matches for query {}.", matches.size(), query);
        return matches;
    }

    /**
     * Returns a vocabulary of unique terms in the index.
     */
    public List<String> vocabulary(){
        return index.vocabulary();
    }

    private Map<Integer, Double> computeDocumentScores(List<String> queryTerms){

        Map<Integer, Double> scores = new HashMap<>();

        for(String queryTerm : queryTerms){
            List<Posting> postings = index.postingsList(queryTerm);
            for(Posting posting : postings){
                final int docId = posting.docId();
                // weights of all query terms are assumed to be equal to 1
                scores.put(
                    docId, 
                    scores.getOrDefault(docId, 0.0) + posting.tfIdfScore()
                );
            }
        }

        // length-normalize the scores
        for(int docId : scores.keySet()){
            final double docScore = scores.get(docId);
            final double vectorMagnitude = index.documentVectorMagnitude(docId);
            scores.put(docId, docScore / vectorMagnitude);
        }

        return scores;
    }

    private List<Match> getTopKMatches(Map<Integer, Double> scores, int k){
        return scores.keySet().stream()
                              .sorted((docId1, docId2) -> Double.compare(scores.get(docId2), scores.get(docId1)))
                              .limit(k)
                              .map(docId -> buildMatch(docId, scores.get(docId)))
                              .collect(Collectors.toList());
    }

    private Match buildMatch(int docId, double documentScore){
        HtmlDocument doc = docRepo.get(new DocId(docId));
        return new Match.Builder()
                        .docId(docId)
                        .documentUrl(doc.getUrl())
                        .documentContent(doc.getContent())
                        .documentTitle(doc.getTitle())
                        .documentScore(documentScore)
                        .build();
    }
}