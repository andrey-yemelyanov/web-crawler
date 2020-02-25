package helvidios.search.webcrawler;

import java.util.concurrent.*;

/**
 * In-memory url cache based on concurrent hash map.
 */
public class HashMapCache implements UrlCache{

    private ConcurrentMap<String, String> map = new ConcurrentHashMap<>();

    @Override
    public void add(String url) {
        map.put(url, url);

    }

    @Override
    public boolean contains(String url) {
        return map.containsKey(url);
    }
}