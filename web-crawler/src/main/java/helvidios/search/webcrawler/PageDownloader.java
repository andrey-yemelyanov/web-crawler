package helvidios.search.webcrawler;

import java.util.concurrent.BlockingQueue;
import org.jsoup.Jsoup;

/**
 * Continuously attempts to take a URL from the url queue and download the web page at
 * that URL. The downloaded page is then added to the document queue for further asynchronous processing.
 */
public class PageDownloader extends Thread {
    private BlockingQueue<String> urlQueue;
    private BlockingQueue<Document> docQueue;
    private UrlCache cache;
    private Log log;
    private boolean isStopped;

    private static int counter = 0;
    private int id;

    /**
     * Initializes a new instance of {@link PageDownloader}.
     */
    public PageDownloader(
        BlockingQueue<String> urlQueue, 
        BlockingQueue<Document> docQueue,
        UrlCache cache,
        Log log) {
        this.urlQueue = urlQueue;
        this.docQueue = docQueue;
        this.cache = cache;
        this.log = log;
        this.id = counter++;
    }

    @Override
    public void run() {
        log.info(String.format("PageDownloader %d started.", id));
        while (!isStopped()) {
            try {
                log.info(String.format("PageDownloader %d: waiting for urls to be available...", id));
                String url = urlQueue.take();
                String html = Jsoup.connect(url).get().html();
                cache.add(url);
                log.info(String.format("PageDownloader %d: downloaded %s", id, url));
                docQueue.put(new Document(url, html));
            } catch (Exception ex) {
                // log exception but keep running and try to download next url
                log.err("Unable to download a page. " + ex.toString());
            }
        }
        log.info(String.format("PageDownloader %d terminated.", id));
    }

    /**
     * Signals to this downloader to stop fetching URLs.
     */
    public synchronized void doStop(){
        log.info(String.format("PageDownloader %d stopped.", id));
        isStopped = true;
        interrupt();
    }

    /**
     * Returns true if this downloader is stopped.
     */
    public synchronized boolean isStopped(){
        return isStopped;
    }
}