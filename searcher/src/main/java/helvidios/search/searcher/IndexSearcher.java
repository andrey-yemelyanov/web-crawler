package helvidios.search.searcher;

import org.apache.logging.log4j.Logger;
import helvidios.search.index.Index;
import helvidios.search.index.Posting;
import helvidios.search.index.storage.IndexRepository;
import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.HtmlDocument;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Index search component that implements {@link Searcher}.
 */
public class IndexSearcher implements Searcher {

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
    public IndexSearcher(
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

    @Override
    public List<Match> search(String query, int k) throws Exception {
        log.info("Searching for query '{}' (return top {} matches) ...", query, k);
        List<String> tokens = tokenizer.getTokens(query);
        if(tokens.isEmpty()) {
            log.info("Empty query. Nothing found.");
            return Arrays.asList();
        }
        List<String> queryTerms = lemmatizer.getLemmas(tokens);
        List<Match> matches = getTopKMatches(computeDocumentScores(queryTerms), k, queryTerms);
        log.info("Found {} matches for query {}.", matches.size(), query);
        return matches;
    }

    @Override
    public List<String> vocabulary(){
        return index.vocabulary();
    }

    private Map<Integer, Double> computeDocumentScores(List<String> queryTerms) throws Exception {

        Map<Integer, Double> scores = new HashMap<>();

        for(String queryTerm : queryTerms){
            List<Posting> postings = index.postingsList(queryTerm);
            for(Posting posting : postings){
                final int docId = posting.docId();

                // weights of all query terms are assumed to be equal to 1
                scores.put(
                    docId,
                    scores.getOrDefault(docId, 0.0) + tfIdfScore(posting)
                );

                // increase document score by 1 if the term occurs in document title
                if(posting.termAppearsInDocTitle()){
                    scores.put(docId, scores.get(docId) + 1);
                }
            }
        }

        return scores;
    }

    private double tfIdfScore(Posting posting){
        //double tfWeight = ((double) posting.tf()) / index.getDocumentLength(posting.docId());
        double tfWeight = 1 + Math.log10(posting.tf());
        double idf = posting.term().idf();
        return tfWeight * idf;
    }

    private List<Match> getTopKMatches(Map<Integer, Double> scores, int k, List<String> queryTerms){
        return scores.keySet().stream()
                              .sorted((docId1, docId2) -> Double.compare(scores.get(docId2), scores.get(docId1)))
                              .limit(k)
                              .map(docId -> buildMatch(docId, scores.get(docId), queryTerms))
                              .collect(Collectors.toList());
    }

    private Match buildMatch(int docId, double documentScore, List<String> queryTerms){
        HtmlDocument doc = docRepo.get(docId);
        TextHighlighter highlighter = new TextHighlighter(tokenizer.getText(doc.getContent()));
        return new Match.Builder()
                        .docId(docId)
                        .documentUrl(doc.getUrl())
                        .documentTitle(doc.getTitle())
                        .documentScore(documentScore)
                        .documentContent(doc.getContent())
                        .textSnippet(highlighter.generateSnippet(queryTerms))
                        .build();
    }
}