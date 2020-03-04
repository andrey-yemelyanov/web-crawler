package helvidios.search.webcrawler;

import java.util.*;
import helvidios.search.webcrawler.logging.Log;
import helvidios.search.webcrawler.storage.DocumentRepository;
import helvidios.search.webcrawler.url.UrlExtractor;

/**
 * Represents a pool of page downloader threads.
 */
public class PageDownloaderPool{
    private Log log;
    private List<PageDownloader> downloaders;

    /**
     * Initializes a new instance of {@link PageDownloaderPool}.
     * @param urlQueue url queue
     * @param docQueue document queue
     * @param cache url cache
     * @param log logging component
     * @param nDownloaders max number of page downloaders running in this pool
     */
    public PageDownloaderPool(
        UrlQueue urlQueue, 
        DocumentRepository docRepo, 
        UrlExtractor urlExtractor,
        Log log,
        int nDownloaders){

        this.log = log;

        downloaders = new ArrayList<>();
        for(int i = 0; i < nDownloaders; i++){
            downloaders.add(new InternetPageDownloader(urlQueue, docRepo, urlExtractor, log));
        }
    }

    /**
     * Starts all page downloader threads in this pool.
     */
    public void start(){
        log.info("PageDownloaderPool started.");
        downloaders.forEach(PageDownloader::start);
    }

    /**
     * Stops all page downloader threads in this pool.
     */
    public void stop(){
        log.info("PageDownloaderPool stopped.");
        downloaders.forEach(PageDownloader::doStop);
    }

    /**
     * Returns true if this downloader pool is stopped.
     */
    public boolean isStopped(){
        return downloaders.stream().allMatch(downloader -> downloader.isStopped());
    }
}