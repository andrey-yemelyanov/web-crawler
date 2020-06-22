package helvidios.search.searcher;

import org.apache.logging.log4j.Logger;
import helvidios.search.index.Index;
import helvidios.search.index.storage.IndexRepository;
import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;

public class Searcher {
    
    private final Index index;
    private final Logger log;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;

    /**
     * Initializes a new instance of the searcher.
     * @param indexRepo implementation of {@link IndexRepository} interface which provides access to the index
     * @param tokenizer query tokenizer
     * @param lemmatizer query lemmatizer
     * @param log log component
     */
    public Searcher(
        IndexRepository indexRepo,
        Tokenizer tokenizer,
        Lemmatizer lemmatizer, 
        Logger log){
        this.log = log;
        this.index = new Index(indexRepo, log);
        this.tokenizer = tokenizer;
        this.lemmatizer = lemmatizer;
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
        List<String> terms = lemmatizer.getLemmas(tokens);
        List<Match> matches = getMatches(terms, k);
        log.info("Found {} matches for query {}.", matches.size(), query);
        return matches;
    }

    /**
     * Returns a vocabulary of unique terms in the index.
     */
    public List<String> vocabulary(){
        return index.vocabulary();
    }

    private List<Match> getMatches(List<String> terms, int k){
        return Arrays.asList();
    }

}