package helvidios.search.webcrawler;

import java.util.*;
import helvidios.search.webcrawler.logging.Log;
import helvidios.search.webcrawler.logging.Log4j;
import helvidios.search.webcrawler.storage.*;
import helvidios.search.webcrawler.url.*;

/**
 * Web crawler that can discover a single web site (a set of urls with the same base url).
 */
public class Crawler {

    private PageDownloaderPool downloaderPool;
    private DocumentRepository docRepo;
    private UrlQueue urlQueue;
    private Log log;

    private Crawler(
        List<String> seedUrls, 
        DocumentRepository docRepo,
        UrlExtractor urlExtractor,
        Log log,
        int nDownloaders,
        int timeout) 
        throws InterruptedException {

        urlQueue = new UrlQueue(timeout);
        this.docRepo = docRepo;

        // add seed urls to the url queue
        for(String url : seedUrls) urlQueue.addUrl(url);

        downloaderPool = new PageDownloaderPool(
            urlQueue, 
            docRepo, 
            urlExtractor, 
            log, 
            nDownloaders);

        this.log = log;
    }

    /**
     * Returns the number of documents that have been downloaded so far.
     */
    public int nDocs(){
        return docRepo.size();
    }

    /**
     * Returns the number of URLs currently scheduled for download.
     */
    public int nUrls(){
        return urlQueue.size();
    }

    /**
     * Starts the crawling process by launching asynchronous downloaders.
     */
    public void start() {
        log.info("Web crawler started.");
        downloaderPool.start();
    }

    /**
     * Stops the crawling process by stopping asynchronous downloaders.
     */
    public void stop(){
        log.info("Web crawler stopped.");
        downloaderPool.stop();
    }

    /**
     * Returns true if this crawler is completed or stopped.
     */
    public boolean isStopped(){
        return downloaderPool.isStopped();
    }

    public static class Builder{
        private DocumentRepository docRepo = new InMemoryDocStorage();
        private UrlExtractor urlExtractor = new SimpleUrlExtractor();
        private Log log = new Log4j();
        private List<String> seedUrls;
        private int nDownloaders = 30;
        private int timeout = 60;

        public Builder(List<String> seedUrls){
            this.seedUrls = seedUrls;
        }

        public Builder setDocumentRepository(DocumentRepository docRepo){
            this.docRepo = docRepo;
            return this;
        }

        public Builder setLog(Log log){
            this.log = log;
            return this;
        }

        public Builder setUrlExtractor(UrlExtractor urlExtractor){
            this.urlExtractor = urlExtractor;
            return this;
        }

        public Builder setNumberOfDownloaders(int nDownloaders){
            this.nDownloaders = nDownloaders;
            return this;
        }

        public Builder setTimeout(int timeout){
            this.timeout = timeout;
            return this;
        }

        public Crawler build() throws InterruptedException {
            return new Crawler(seedUrls, docRepo, urlExtractor, log, nDownloaders, timeout);
        }
    }
}