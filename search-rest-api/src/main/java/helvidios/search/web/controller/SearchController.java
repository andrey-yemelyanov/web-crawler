package helvidios.search.web.controller;

import org.springframework.web.bind.annotation.RestController;
import helvidios.search.searcher.Searcher;
import helvidios.search.web.SearchException;
import helvidios.search.web.model.SearchHit;
import helvidios.search.web.model.TermPage;
import helvidios.search.web.util.Trie;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class SearchController {

    private final Searcher searcher;
    private final Trie terms;

    @Autowired
    public SearchController(Searcher searcher){
        this.searcher = searcher;
        this.terms = new Trie();
        searcher.vocabulary().forEach(terms::add);
    }

    @GetMapping(value = "/api/search")
    public List<SearchHit> search(
        @RequestParam(value = "query", required = true) String query,
        @RequestParam(value = "limit", required = false, defaultValue = "10") int k) {
        try {

            return searcher.search(query, k)
                           .stream()
                           .map(match -> new SearchHit(match.docId(), match.url(), match.title(), match.score()))
                           .collect(Collectors.toList());

        } catch (Exception e) {
            throw new SearchException(e);
        }
    }

    @GetMapping(value = "/api/vocabulary")
    public TermPage getVocabulary(
        @RequestParam(value = "prefix", required = false, defaultValue = "") String prefix,
        @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page){

        List<String> termSlice = terms.getByPrefix(prefix);
        final int nPages = (int) Math.ceil(termSlice.size() / (double) limit);

        termSlice = termSlice.stream()
                             .skip(limit * (page - 1))
                             .limit(limit)
                             .sorted()
                             .collect(Collectors.toList());
                                      
        return new TermPage(page, limit, nPages, termSlice);
    }
}