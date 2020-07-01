package helvidios.search.searcher;

import java.util.*;

/**
 * Represents a short summary snippet of original text with highlighted query terms.
 */
public class TextSnippet {

    private final String text;
    private final Map<String, List<Integer>> offsets;

    public TextSnippet(String text, Map<String, List<Integer>> highlights) {
        this.text = text;
        this.offsets = highlights;
    }

    /**
     * Returns occurrences of query terms in this snippet.
     */
    public Map<String, List<Integer>> offsets() {
        return offsets;
    }

    /**
     * Returns snippet text.
     */
    public String text() {
        return text;
    }

    @Override
    public String toString(){
        return String.format("Snippet {text=%s offsets=%s}", text, offsets.toString());
    }
}