package helvidios.search.webcrawler;

/**
 * Fast and thread-safe cache of already visted/downloaded URLs.
 */
public interface UrlCache{
    /**
     * Adds a new url to this cache.
     * @param url url
     */
    public void add(String url);

    /**
     * Checks if a given url is present in the cache.
     * @param url url
     * @return true if the url is present in the cache
     */
    public boolean contains(String url);
}