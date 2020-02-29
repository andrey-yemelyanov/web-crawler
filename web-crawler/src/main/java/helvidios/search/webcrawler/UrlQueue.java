package helvidios.search.webcrawler;

import java.util.concurrent.*;
import helvidios.search.webcrawler.exceptions.QueueTimeoutException;

/**
 * A thread-safe in-memory FIFO queue of distinct URLs.
 */
public class UrlQueue{
    private final BlockingQueue<String> queue;
    private final ConcurrentMap<String, String> cache;
    private final int timeout;

    /**
     * Initializes a new instance of {@link UrlQueue}.
     * @param timeout amount of seconds that elapse before the call to get next URL times out
     */
    public UrlQueue(int timeout){
        queue = new LinkedBlockingDeque<>();
        cache = new ConcurrentHashMap<>();
        this.timeout = timeout;
    }

    /**
     * Adds a new url to the queue. If the same url is already present in the queue, this operation has no effect.
     * This method is thread-safe - only one thread can be executing it at any given time.
     * @param url URL
     * @throws InterruptedException
     */
    public synchronized void addUrl(String url) throws InterruptedException {
        if(cache.containsKey(url)) return;
        queue.put(url);
        cache.put(url, "");
    }

    /**
     * Returns the next-in-line URL from the queue or blocks if the queue is empty.
     * The call will time out after {@code timeout} seconds if the url queue is still empty.
     * 
     * @return URL
     * @throws InterruptedException
     * @throws QueueTimeoutException
     */
    public String getUrl() throws InterruptedException, QueueTimeoutException {
        String url = queue.poll(timeout, TimeUnit.SECONDS);
        if(url == null) {
            throw new QueueTimeoutException(
                String.format("Timeout on GetURL. URL queue has been empty for %d seconds.", timeout));
        }
        if(cache.containsKey(url)) cache.remove(url);
        return url;
    }

    /**
     * Returns the number of URLs currently present in this queue.
     * @return number of available URLs
     */
    public int size(){
        return queue.size();
    }
}