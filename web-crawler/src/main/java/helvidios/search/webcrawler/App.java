package helvidios.search.webcrawler;

import java.net.MalformedURLException;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.MongoDbDocumentRepository;

/**
 * Web Crawler runner. For convenience use run-web-crawler.bat to execute the crawler.
 */
public class App {
    public static void main(String... args) throws InterruptedException, MalformedURLException {

        if(args.length == 0) throw new IllegalArgumentException("Seed URL is required as command-line arg.");
        String seedUrl = args[0];
        String urlPrefix = null;
        if(args.length > 1){
            urlPrefix = args[1];
        }
        
        DocumentRepository docRepo = new MongoDbDocumentRepository.Builder().build();

        Crawler.Builder builder = new Crawler.Builder(seedUrl);
        if(urlPrefix != null){
            builder = new Crawler.Builder(seedUrl, urlPrefix);
        }

        Crawler crawler = builder.setDocumentRepository(docRepo)
                                 .build();
        
        crawler.start();

        System.out.printf("\nCrawling from seed URL: %s\n", seedUrl);
        if(urlPrefix != null){
            System.out.printf("URL prefix: %s\n", urlPrefix);
        }
        System.out.println("\n--URLS-------DOCS--");
        while(!crawler.isStopped()){
            System.out.print("\r");
            System.out.printf("%6d %10d", crawler.nUrls(), crawler.nDocs());
            Thread.sleep(200);
        }

        System.out.printf("\nCrawling is completed. Downloaded %d documents.", crawler.nDocs());
    }
}