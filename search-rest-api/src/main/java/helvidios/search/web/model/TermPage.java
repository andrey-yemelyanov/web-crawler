package helvidios.search.web.model;

import java.util.*;

public class TermPage {

    private final int page;
    private final int limit;
    private final int nPages;
    private final List<String> terms;

    public TermPage(int page, int limit, int nPages, List<String> terms) {
        this.page = page;
        this.limit = limit;
        this.nPages = nPages;
        this.terms = terms;
    }

    public int getnPages() {
        return nPages;
    }

    public List<String> getTerms() {
        return terms;
    }

    public int getLimit() {
        return limit;
    }

    public int getPage() {
        return page;
    }
}