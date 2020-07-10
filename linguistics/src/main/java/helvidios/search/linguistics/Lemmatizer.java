package helvidios.search.linguistics;

import java.util.*;

/**
 * Removes any changes in form of the word like tense, 
 * gender, mood, etc. and returns dictionary or base form of word.
 * E.g. cars -> car, threw -> throw
 */
public interface Lemmatizer {
    public List<String> getLemmas(List<String> tokens) throws Exception;
}