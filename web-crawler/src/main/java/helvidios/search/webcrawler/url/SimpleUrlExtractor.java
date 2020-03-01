package helvidios.search.webcrawler.url;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import helvidios.search.webcrawler.HtmlDocument;

/** 
 * Extracts distinct normalized absolute URLs from an HTML document.
 * If {@code urlPrefix} is specified, then only URLs that begin with the prefix are extracted.
 * */
public class SimpleUrlExtractor implements UrlExtractor {

    private final List<String> urlPrefixes;

    public SimpleUrlExtractor(String... urlPrefix) {
        this.urlPrefixes = Arrays.asList(urlPrefix);
    }

    @Override
    public List<String> getUrls(HtmlDocument doc) {

        Elements links = Jsoup.parse(doc.getContent(), doc.getUrl())
                              .select("a[href]");
        
        Stream<String> urls = links.stream()
                                 .map(link -> link.attr("abs:href")) // get absolute urls
                                 .map(this::normalize) // normalize urls
                                 .filter(url -> !url.isEmpty()) // remove invalid/empty urls
                                 .filter(url -> !url.equals(doc.getUrl())) // remove same urls as url of the original document
                                 .distinct()
                                 .sorted();

        if(!urlPrefixes.isEmpty()){
            // remove urls not sharing urlPrefix
            urls = urls.filter(url -> urlPrefixes.stream().anyMatch(prefix -> url.startsWith(prefix)));
        }

        return urls.collect(Collectors.toList());
    }

    private String normalize(String url) {
        try{
            URL absoluteUrl = new URL(url);
            return new URL(
                absoluteUrl.getProtocol(), 
                absoluteUrl.getHost(), 
                absoluteUrl.getPort(), 
                absoluteUrl.getPath())
            .toString();
        }catch(IOException ex){
            // log exception
        }
        return "";
    }
}