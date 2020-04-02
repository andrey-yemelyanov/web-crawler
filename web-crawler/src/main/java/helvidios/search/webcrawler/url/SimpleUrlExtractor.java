package helvidios.search.webcrawler.url;

import java.util.*;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import helvidios.search.storage.HtmlDocument;
import helvidios.search.webcrawler.Util;

/**
 * Extracts distinct normalized absolute URLs from an HTML document.
 */
public class SimpleUrlExtractor implements UrlExtractor {

    private final String baseUrl;

    /**
     * Initializes a new instance of {@link SimpleUrlExtractor}.
     * @param baseUrl base URL which all URLs must have in common
     */
    public SimpleUrlExtractor(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public List<String> getUrls(HtmlDocument doc) {

        return Jsoup.parse(doc.getContent(), doc.getUrl())
                    .select("a[href]")
                    .stream()
                    .map(link -> link.attr("abs:href"))
                    .map(Util::normalize)
                    .filter(url -> url != null)
                    .filter(url -> url.startsWith(baseUrl))
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
    }
}