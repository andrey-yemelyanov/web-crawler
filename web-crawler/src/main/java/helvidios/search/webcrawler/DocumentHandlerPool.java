package helvidios.search.webcrawler;

import java.util.concurrent.BlockingQueue;
import java.util.*;

/**
 * Represents a pool of document handler threads.
 */
public class DocumentHandlerPool {
    private Log log;
    private List<DocumentHandler> handlers;

    /**
     * Initializes a new instance of {@link DocumentHandlerPool}.
     * @param urlQueue url queue
     * @param docQueue document queue
     * @param cache url cache
     * @param documentDb document storage
     * @param log logging component
     * @param nHandlers max number of handlers running in this pool
     */
    public DocumentHandlerPool(
        BlockingQueue<String> urlQueue, 
        BlockingQueue<Document> docQueue, 
        UrlCache cache,
        DocumentDb documentDb,
        Log log, 
        int nHandlers){
        
        this.log = log;

        handlers = new ArrayList<>();
        for(int i = 0; i < nHandlers; i++){
            handlers.add(new DocumentHandler(urlQueue, docQueue, cache, documentDb, log));
        }
    }

    /**
     * Starts all document handler threads in this pool.
     */
    public void start(){
        log.info("DocumentHandlerPool started.");
        handlers.forEach(DocumentHandler::start);
    }

    /**
     * Stops all document handler threads in this pool.
     */
    public void stop(){
        log.info("DocumentHandlerPool stopped.");
        handlers.forEach(DocumentHandler::doStop);
    }
}