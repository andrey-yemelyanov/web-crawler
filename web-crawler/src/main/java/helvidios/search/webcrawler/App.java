package helvidios.search.webcrawler;

import java.util.*;
import helvidios.search.webcrawler.logging.Log;
import helvidios.search.webcrawler.logging.Log4j;
import helvidios.search.webcrawler.storage.DocumentRepository;
import helvidios.search.webcrawler.storage.MongoDbStorage;

public class App{
    public static void main(String... args) throws InterruptedException {
        
        String prefixUrl = "https://docs.oracle.com/javase/8/docs/api/";
        List<String> seedUrls = Arrays.asList(prefixUrl + "allclasses-noframe.html");

        Log log = new Log4j();

        DocumentRepository docRepo = new MongoDbStorage.Builder(log).build();

        Crawler crawler = new Crawler.Builder(seedUrls, prefixUrl)
                                     .setDocumentRepository(docRepo)
                                     .setLog(log)
                                     .build();
        crawler.start();

        System.out.println("Crawling " + prefixUrl);
        System.out.println("--URLS-------DOCS--");
        while(!crawler.isStopped()){
            System.out.print("\r");
            System.out.printf("%6d %10d", crawler.nUrls(), crawler.nDocs());
            Thread.sleep(200);
        }

        System.out.println("\nCrawling is completed.");
    }
}