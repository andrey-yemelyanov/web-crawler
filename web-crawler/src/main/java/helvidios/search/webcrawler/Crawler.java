package helvidios.search.webcrawler;

import java.util.*;
import java.util.concurrent.*;

/**
 * Web crawler that can discover a single web site (a set of urls with the same base url).
 */
public class Crawler {

    private static final int URL_QUEUE_CAPACITY = 10000;
    private static final int DOC_QUEUE_CAPACITY = 10000;
    private static final int N_DOWNLOADERS = 5;
    private static final int N_DOC_HANDLERS = 20;

    private PageDownloaderPool downloaderPool;
    private DocumentHandlerPool handlerPool;
    private Log log;
    private UrlCache cache;
    private DocumentDb documentDb;

    /**
     * Initializes a new instance of {@link Crawler}.
     * 
     * @param seedUrls initial seed urls to start crawling from
     * @param log      logging component
     * @throws InterruptedException
     */
    public Crawler(List<String> seedUrls, Log log) throws InterruptedException {

        cache = new HashMapCache();
        documentDb = new PostgresDocumentDb();

        BlockingQueue<String> urlQueue = new LinkedBlockingQueue<String>(URL_QUEUE_CAPACITY);
        // add seed urls to the url queue
        for(String url : seedUrls) urlQueue.put(url);

        BlockingQueue<Document> docQueue = new LinkedBlockingQueue<Document>(DOC_QUEUE_CAPACITY);

        downloaderPool = new PageDownloaderPool(urlQueue, docQueue, cache, log, N_DOWNLOADERS);
        handlerPool = new DocumentHandlerPool(urlQueue, docQueue, cache, documentDb, log, N_DOC_HANDLERS);

        this.log = log;
    }

    public int nDocs(){
        return documentDb.nDocs();
    }

    public int nUrls(){
        return cache.size();
    }

    /**
     * Starts the crawling process by launching asynchronous downloaders and document handlers.
     */
    public void start() {
        log.info("Web crawler started.");
        downloaderPool.start();
        handlerPool.start();
    }

    /**
     * Stops the crawling process by stopping asynchronous downloaders and document handlers.
     */
    public void stop(){
        log.info("Web crawler stopped.");
        downloaderPool.stop();
        handlerPool.stop();
    }
}