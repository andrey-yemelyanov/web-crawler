package helvidios.search.webcrawler.url;

import java.util.*;
import java.util.stream.Collectors;
import helvidios.search.webcrawler.HtmlDocument;

/**
 * A URL extractor for HTML pages coming from the Java Docs API specification.
 */
public class SameSiteUrlExtractor extends SimpleUrlExtractor {

    private final List<String> urlPrefixes;

    /**
     * Initializes a new instance of {@code JavaDocsUrlExtractor}.
     * 
     * @param urlPrefix URL prefix that all extracted URLs will have in common. This
     *                  essentially limits the scope of which URLs will be returned
     *                  by this extractor.
     */
    public SameSiteUrlExtractor(String... urlPrefix) {
        this.urlPrefixes = Arrays.asList(urlPrefix);
    }

    @Override
    public List<String> getUrls(HtmlDocument doc) {
        return super.getUrls(doc)
                    .stream()
                    .filter(url -> urlPrefixes.stream().anyMatch(prefix -> url.startsWith(prefix))) // remove urls not sharing urlPrefix
                    .distinct()
                    .collect(Collectors.toList());
    }
}