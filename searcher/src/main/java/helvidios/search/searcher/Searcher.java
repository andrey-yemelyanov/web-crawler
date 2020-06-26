package helvidios.search.searcher;

import java.util.*;

/**
 * Represents a search component.
 */
public interface Searcher {

    /**
     * Searches for top K documents with the best match for the supplied query.
     * @param query query in free text
     * @param k number of top documents to return
     * @return top K best matching documents
     * @throws Exception
     */
    public List<Match> search(String query, int k) throws Exception;

    /**
     * Returns a list of terms (vocabulary) in the index.
     */
    public List<String> vocabulary();
}