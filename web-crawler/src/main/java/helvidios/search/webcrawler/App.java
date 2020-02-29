package helvidios.search.webcrawler;

import java.util.*;

import helvidios.search.webcrawler.url.SameSiteUrlExtractor;

public class App{
    public static void main(String... args) throws InterruptedException {
        List<String> seedUrls = Arrays.asList(
          "https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html");
        String prefixUrl = "https://docs.oracle.com/javase/8/docs/api/";
        Crawler crawler = new Crawler.Builder(seedUrls)
                                     .setNumberOfDownloaders(20)
                                     .setUrlExtractor(new SameSiteUrlExtractor(prefixUrl))
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