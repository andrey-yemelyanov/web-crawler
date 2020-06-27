package helvidios.search.web.util;

import java.util.*;

/**
 * Represents a vocabulary of terms.
 */
public interface Vocabulary {
    int size();
    List<String> getByPrefix(String prefix);
    boolean containsPrefix(String prefix);
    boolean containsWord(String word);
    void add(String word);
}