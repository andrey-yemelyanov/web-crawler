package helvidios.search.webcrawler;

import java.util.concurrent.BlockingQueue;

/**
 * Continuously extracts downloaded documents from the document queue and extracts all hyperlinks from the document.
 * Stores the downloaded document in the persistent storage and enqueues new unvisited hyperlinks for download.
 */
public class DocumentHandler extends Thread{
    private BlockingQueue<Document> docQueue;
    private BlockingQueue<String> urlQueue;
    private UrlCache cache;
    private Log log;
    private DocumentDb documentDb;
    private boolean isStopped;

    private static int counter = 0;
    private int id;

    /**
     * Initializes a new instance of {@link DocumentHandler}.
     * @param urlQueue url queue
     * @param docQueue document queue
     * @param cache url cache
     * @param documentDb document storage
     * @param log logging component
     */
    public DocumentHandler(
        BlockingQueue<String> urlQueue, 
        BlockingQueue<Document> docQueue, 
        UrlCache cache,
        DocumentDb documentDb,
        Log log){

        this.docQueue = docQueue;
        this.urlQueue = urlQueue;
        this.cache = cache;
        this.log = log;
        this.documentDb = documentDb;

        id = counter++;
    }

    @Override
    public void run() {
        log.info(String.format("DocumentHandler %d started.", id));
        while (!isStopped()) {
            try {
                log.info(String.format("DocumentHandler %d: waiting for documents to become available...", id));
                Document doc = docQueue.take();
                for(String url : doc.getUrls()){
                    if(!cache.contains(url)){
                        urlQueue.put(url);
                    }
                }
                documentDb.save(doc);
                log.info(String.format("DocumentHandler %d: Processed doc from %s", id, doc.getUrl()));
            } catch (Exception ex) {
                // log exception but keep running and try to process next document
                log.err("Unable to process a document. " + ex.toString());
            }
        }
        log.info(String.format("DocumentHandler %d terminated.", id));
    }

    /**
     * Signals to this document handler to stop processing documents.
     */
    public synchronized void doStop(){
        log.info(String.format("DocumentHandler %d stopped.", id));
        isStopped = true;
        interrupt();
    }

    /**
     * Returns true if this document handler is stopped.
     */
    public synchronized boolean isStopped(){
        return isStopped;
    }
}