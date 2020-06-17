package helvidios.search.webcrawler;

import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import helvidios.search.storage.DocumentRepository;
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
        Logger log) {
        super(urlQueue, docRepo, urlExtractor, log);

    }

    @Override
    public String downloadPage(String url) throws IOException {
        return Jsoup.connect(url).get().html();
    }

    @Override
    protected String getDocumentTitle(String content) {
        final String defaultTitle = "Unknown document title";
        try{
            String title = Jsoup.parse(content).title().trim();
            if(title == null || title.isEmpty()) return defaultTitle;
            return title;
        }catch(Exception ex){
            log.error(ex);
            return defaultTitle;
        }
    }

}