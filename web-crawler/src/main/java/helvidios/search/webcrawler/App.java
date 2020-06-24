package helvidios.search.webcrawler;

import java.net.MalformedURLException;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.MongoDbDocumentRepository;

/**
 * Web Crawler runner. For convenience use run-web-crawler.bat to execute the crawler.
 */
public class App {
    public static void main(String... args) throws InterruptedException, MalformedURLException {

        if(args.length < 2) throw new IllegalArgumentException(
            "Command line arguments missing. Usage: run-web-crawler.bat [dbName] [seedUrl] [urlPrefix(optional)]");

        final String dbName = args[0];
        final String seedUrl = args[1];
        String urlPrefix = null;
        if(args.length > 2){
            urlPrefix = args[2];
        }
        
        DocumentRepository docRepo = new MongoDbDocumentRepository.Builder()
                                                                  .setDatabase(dbName)
                                                                  .build();

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