package helvidios.search.webcrawler;

import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.HtmlDocument;
import helvidios.search.webcrawler.exceptions.QueueTimeoutException;
import helvidios.search.webcrawler.logging.Log;
import helvidios.search.webcrawler.url.UrlExtractor;

/**
 * Continuously attempts to take a URL from the url queue and download the web
 * page at that URL. The downloaded page is then saved in storage and newly discovered URLs
 * are added to the crawler frontier.
 */
public abstract class PageDownloader extends Thread {
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

                // get the next URL to download from the crawler frontier
                url = urlQueue.getUrl();

                // download the document with this URL
                HtmlDocument doc = new HtmlDocument(url, downloadPage(url));

                // store the downloaded document in repository
                if(!docRepo.contains(url)) {
                    docRepo.insert(doc);
                    log.info(String.format("Downloaded [%d] %s", doc.getId(), url));
                }

                // add the newly discovered URLs in the downloaded document to the crawler frontier
                for (String nextUrl : urlExtractor.getUrls(doc)) {
                    if (!docRepo.contains(nextUrl)) {
                        urlQueue.addUrl(nextUrl);
                    }
                }
            } catch(QueueTimeoutException ex){
                log.info(ex.getMessage());
                // there are no more URLs in the frontier queue - just terminate this downloader
                setStopped(true);
            } catch (Exception ex) {
                // log exception but keep running and try to download next url
                log.err(String.format("Unable to download %s", url), ex);
            }
        }
        log.info("PageDownloader terminated.");
    }

    /**
     * Downloads an HTML page from the specified URL.
     * @param url URL of the HTML document to download
     * @return full HTML content of the downloaded page
     * @throws Exception
     */
    public abstract String downloadPage(String url) throws Exception;

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