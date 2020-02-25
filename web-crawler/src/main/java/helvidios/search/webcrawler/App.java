package helvidios.search.webcrawler;

import java.util.*;

public class App{
    public static void main(String... args) throws InterruptedException {
        List<String> urls = Arrays.asList(
          "https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html",
          "https://docs.oracle.com/javase/8/docs/api/java/util/Map.html",
          "https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html",
          "https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html"  
        );
        Crawler crawler = new Crawler(urls, new StdOutLog());
        crawler.start();
        
    }
}