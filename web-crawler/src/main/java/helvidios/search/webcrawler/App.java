package helvidios.search.webcrawler;

import java.net.MalformedURLException;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.MongoDbDocumentRepository;
import helvidios.search.webcrawler.logging.Log;
import helvidios.search.webcrawler.logging.Log4j;

public class App {
    public static void main(String... args) throws InterruptedException, MalformedURLException {
        
        String seedUrl = "https://docs.oracle.com/javase/8/docs/api/index-files/index-1.html";

        Log log = new Log4j();

        DocumentRepository docRepo = new MongoDbDocumentRepository.Builder().build();

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