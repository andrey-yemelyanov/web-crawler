package helvidios.search.webcrawler;

import java.util.*;
import helvidios.search.webcrawler.logging.Log;
import helvidios.search.webcrawler.logging.Log4j;
import helvidios.search.webcrawler.storage.*;
import helvidios.search.webcrawler.url.*;

/**
 * A generic web crawler that traverses the URL graph until the URL queue becomes empty.
 * Since the web graph is infinitely large the crawler will run as long as there are new URLs 
 * to download and as long as the machine resources (memory and CPU) allow it.
 * It is therefore strongly recommended to use this crawler on a single web-site by instantiating the
 * {@link SameSiteUrlExtractor} class.
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

    /**
     * Builder for a Crawler object.
     */
    public static class Builder{
        private final static int N_DOWNLOADERS = 30;
        private final static int TIMEOUT = 60;

        private DocumentRepository docRepo = new InMemoryDocStorage();
        private UrlExtractor urlExtractor = new SimpleUrlExtractor();
        private Log log = new Log4j();
        private List<String> seedUrls;
        private int nDownloaders = N_DOWNLOADERS;
        private int timeout = TIMEOUT;

        /**
         * Initializes a new instance of {@link Builder}.
         * @param seedUrls initial URLs from which the crawling will begin
         */
        public Builder(List<String> seedUrls){
            this.seedUrls = seedUrls;
        }

        /**
         * Set which document repository implementation to use. 
         * Default is the in-memory repository implementation.
         * @param docRepo
         */
        public Builder setDocumentRepository(DocumentRepository docRepo){
            this.docRepo = docRepo;
            return this;
        }

        /**
         * Set which logging component to use during the crawling process.
         * Default is the Log4j-based implementation.
         * @param log
         */
        public Builder setLog(Log log){
            this.log = log;
            return this;
        }

        /**
         * Set which URL extractor to use during the crawling process.
         * Default is a simple extractor that extracts all links from an HTML document without any filtering.
         * @param urlExtractor
         */
        public Builder setUrlExtractor(UrlExtractor urlExtractor){
            this.urlExtractor = urlExtractor;
            return this;
        }

        /**
         * Set the max number of asynchronous downloaders running in parallel.
         * Default value is {@value #N_DOWNLOADERS} downloaders.
         * @param nDownloaders
         */
        public Builder setNumberOfDownloaders(int nDownloaders){
            this.nDownloaders = nDownloaders;
            return this;
        }

        /**
         * Set the number of seconds to wait before an operation on the URL queue will time out.
         * Default value is {@value #TIMEOUT} seconds.
         * @param timeout
         */
        public Builder setTimeout(int timeout){
            this.timeout = timeout;
            return this;
        }

        /**
         * Builds an instance of {@link Crawler} for the supplied configuration settings.
         */
        public Crawler build() throws InterruptedException {
            return new Crawler(seedUrls, docRepo, urlExtractor, log, nDownloaders, timeout);
        }
    }
}