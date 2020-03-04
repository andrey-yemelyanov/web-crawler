package helvidios.search.webcrawler;

import java.io.IOException;
import org.jsoup.Jsoup;
import helvidios.search.webcrawler.logging.Log;
import helvidios.search.webcrawler.storage.DocumentRepository;
import helvidios.search.webcrawler.url.UrlExtractor;

/**
 * Implements a page downloader that fetches documents from the Internet.
 */
public class InternetPageDownloader extends PageDownloader {

    /**
     * Initializes a new instance of {@link InternetPageDownloader}.
     * @param urlQueue crawler frontier
     * @param docRepo document repository
     * @param urlExtractor URL filtering component
     * @param log logging component
     */
    public InternetPageDownloader(
        UrlQueue urlQueue, 
        DocumentRepository docRepo, 
        UrlExtractor urlExtractor, 
        Log log) {
        super(urlQueue, docRepo, urlExtractor, log);

    }

    @Override
    public String downloadPage(String url) throws IOException {
        return Jsoup.connect(url).get().html();
    }

}