package helvidios.search.webcrawler;

import java.net.MalformedURLException;
import helvidios.search.webcrawler.logging.Log;
import helvidios.search.webcrawler.logging.Log4j;
import helvidios.search.webcrawler.storage.DocumentRepository;
import helvidios.search.webcrawler.storage.MongoDbStorage;

public class App {
    public static void main(String... args) throws InterruptedException, MalformedURLException {
        
        String seedUrl = "https://fergana.news/";

        Log log = new Log4j();

        DocumentRepository docRepo = new MongoDbStorage.Builder(log).build();

        Crawler crawler = new Crawler.Builder(seedUrl)
                                     .setDocumentRepository(docRepo)
                                     .setLog(log)
                                     .build();
        crawler.start();

        System.out.println("Crawling " + Util.getBaseUrl(seedUrl));
        System.out.println("--URLS-------DOCS--");
        while(!crawler.isStopped()){
            System.out.print("\r");
            System.out.printf("%6d %10d", crawler.nUrls(), crawler.nDocs());
            Thread.sleep(200);
        }

        System.out.println("\nCrawling is completed.");
    }
}