package helvidios.search.webcrawler.url;

import java.util.*;
import helvidios.search.webcrawler.HtmlDocument;

/**
 * Extracts normalized absolute URLs from an HTML document that conform to a set of rules.
 */
public interface UrlExtractor {

    /**
     * Returns a list of normalized absolute URLs that satisfy set rules from HTML document. 
     * @param doc HTML document
     */
    public List<String> getUrls(HtmlDocument doc);
}