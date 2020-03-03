package helvidios.search.webcrawler;

import org.jsoup.Jsoup;
import helvidios.search.webcrawler.exceptions.QueueTimeoutException;
import helvidios.search.webcrawler.logging.Log;
import helvidios.search.webcrawler.storage.DocumentRepository;
import helvidios.search.webcrawler.url.UrlExtractor;

/**
 * Continuously attempts to take a URL from the url queue and download the web
 * page at that URL. The downloaded page is then saved in storage and new URLs
 * are discovered.
 */
public class PageDownloader extends Thread {
    private UrlQueue urlQueue;
    private DocumentRepository docRepo;
    private UrlExtractor urlExtractor;
    private Log log;
    private boolean isStopped;

    private static int counter = 0;
    private int id;

    /**
     * Initializes a new instance of {@link PageDownloader}.
     */
    public PageDownloader(
        UrlQueue urlQueue, 
        DocumentRepository docRepo, 
        UrlExtractor urlExtractor, 
        Log log) {

        this.urlQueue = urlQueue;
        this.docRepo = docRepo;
        this.urlExtractor = urlExtractor;
        this.log = log;
        this.id = counter++;
    }

    @Override
    public void run() {
        log.info("PageDownloader started.");
        while (!isStopped()) {
            String url = "";
            try {
                url = urlQueue.getUrl();
                
                String html = Jsoup.connect(url).get().html();

                HtmlDocument doc = new HtmlDocument(url, html);
                
                docRepo.save(doc);

                for (String nextUrl : urlExtractor.getUrls(doc)) {
                    if (!docRepo.contains(Util.checksum(nextUrl))) {
                        urlQueue.addUrl(nextUrl);
                    }
                }

                log.info(String.format("Downloaded %s", url));
                
            } catch(QueueTimeoutException ex){
                log.info(ex.getMessage());
                setStopped(true);
            } catch (Exception ex) {
                // log exception but keep running and try to download next url
                log.err("Unable to download " + url, ex);
            }
        }
        log.info("PageDownloader terminated.");
    }

    /**
     * Signals to this downloader to stop fetching URLs.
     */
    public synchronized void doStop() {
        log.info(String.format("PageDownloader %d stopped.", id));
        setStopped(true);
        interrupt();
    }

    /**
     * Returns true if this downloader is stopped.
     */
    public synchronized boolean isStopped() {
        return isStopped;
    }

    private synchronized void setStopped(boolean value){
        isStopped = value;
    }
}