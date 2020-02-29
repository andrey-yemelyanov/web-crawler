package helvidios.search.webcrawler.url;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import helvidios.search.webcrawler.HtmlDocument;

/** 
 * This extractor finds all urls in HTML document without any filtering. 
 * */
public class SimpleUrlExtractor implements UrlExtractor {

    @Override
    public List<String> getUrls(HtmlDocument doc) {
        Elements links = Jsoup.parse(doc.getContent(), doc.getUrl())
                              .select("a[href]");
        
        return links.stream()
                    .map(link -> link.attr("abs:href")) // get absolute urls
                    .map(this::normalize) // normalize urls
                    .filter(url -> !url.isEmpty()) // remove invalid/empty urls
                    .filter(url -> !url.equals(doc.getUrl())) // remove same urls as url of the original document
                    .sorted()
                    .collect(Collectors.toList());
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