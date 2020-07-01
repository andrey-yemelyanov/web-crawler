package helvidios.search.web.model;

import java.util.*;

import helvidios.search.searcher.TextSnippet;

public class Snippet {

    private final String text;
    private final Map<String, List<Integer>> offsets;

    public Snippet(TextSnippet snippet) {
        this.text = snippet.text();
        this.offsets = snippet.offsets();
    }

    public Map<String, List<Integer>> getOffsets() {
        return offsets;
    }

    public String getText() {
        return text;
    }

}