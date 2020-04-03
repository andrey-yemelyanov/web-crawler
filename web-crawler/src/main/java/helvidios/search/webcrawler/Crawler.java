package helvidios.search.webcrawler;

import java.net.MalformedURLException;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.InMemoryDocumentRepository;
import helvidios.search.webcrawler.logging.Log;
import helvidios.search.webcrawler.logging.Log4j;
import helvidios.search.webcrawler.url.*;

/**
 * A generic web crawler that traverses the URL graph until the URL queue
 * becomes empty.
 */
public class Crawler {

    private PageDownloaderPool downloaderPool;
    private DocumentRepository docRepo;
    private UrlQueue urlQueue;
    private Log log;

    private Crawler(
        String seedUrl, 
        DocumentRepository docRepo, 
        UrlExtractor urlExtractor, 
        Log log,
        int nDownloaders, 
        int timeout) throws InterruptedException {

        urlQueue = new UrlQueue(timeout);
        urlQueue.addUrl(seedUrl);

        this.docRepo = docRepo;
        this.docRepo.clear(); // remove any previously downloaded documents

        downloaderPool = new PageDownloaderPool(urlQueue, docRepo, urlExtractor, log, nDownloaders);

        this.log = log;

        log.info(String.format("WebCrawler initialized.\nSeed URL: %s.", seedUrl));
    }

    /**
     * Returns the number of documents that have been downloaded so far.
     */
    public long nDocs() {
        return docRepo.size();
    }

    /**
     * Returns the number of URLs currently scheduled for download.
     */
    public int nUrls() {
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
    public void stop() {
        log.info("Web crawler stopped.");
        downloaderPool.stop();
    }

    /**
     * Returns true if this crawler is completed or stopped.
     */
    public boolean isStopped() {
        return downloaderPool.isStopped();
    }

    /**
     * Builder for a Crawler object.
     */
    public static class Builder {
        private final static int N_DOWNLOADERS = 30;
        private final static int TIMEOUT = 60;

        private DocumentRepository docRepo = new InMemoryDocumentRepository();
        private UrlExtractor urlExtractor;
        private Log log = new Log4j();
        private String seedUrl;
        private int nDownloaders = N_DOWNLOADERS;
        private int timeout = TIMEOUT;

        /**
         * Initializes a new instance of {@link Builder}.
         * 
         * @param seedUrl initial URL from which the crawling will begin
         * @throws MalformedURLException
         */
        public Builder(String seedUrl) throws MalformedURLException {
            this.seedUrl = seedUrl;
            this.urlExtractor = new SimpleUrlExtractor(Util.getBaseUrl(seedUrl));
        }

        /**
         * Initializes a new instance of {@link Builder}.
         * 
         * @param seedUrl initial URL from which the crawling will begin
         * @param urlPrefix prefix that all crawled URLs must share. 
         * E.g. to crawl only Java 8 API docs use the following prefix: https://docs.oracle.com/javase/8/docs/api.
         * Otherwise the crawler might end up downloading the entire docs.oracle.com domain.
         * @throws MalformedURLException
         */
        public Builder(String seedUrl, String urlPrefix) throws MalformedURLException {
            this.seedUrl = seedUrl;
            this.urlExtractor = new SimpleUrlExtractor(urlPrefix);
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
            return new Crawler(seedUrl, docRepo, urlExtractor, log, nDownloaders, timeout);
        }
    }
}