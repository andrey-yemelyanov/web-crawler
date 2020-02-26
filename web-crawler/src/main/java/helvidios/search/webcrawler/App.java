package helvidios.search.webcrawler;

import java.util.*;

public class App{
    public static void main(String... args) throws InterruptedException {
        List<String> urls = Arrays.asList(
          "https://docs.oracle.com/javase/8/docs/api/overview-summary.html");
        Crawler crawler = new Crawler(urls, new StdOutLog());

        System.out.println("Web Crawler initiated.\nSeed url(s): " + urls.toString());
        System.out.println("Commands:\nstart - start crawling\nstop - stop crawling\nnurls - number of downloaded URLs\nndoc - number of saved documents");

        Scanner in = new Scanner(System.in);
        while(true){
            String s = in.nextLine();
            if(s.equals("start")){
                crawler.start();
            }else if(s.equals("stop")){
                crawler.stop();
            }else if(s.equals("nurls")){
                System.out.println(crawler.nUrls());
            }else if(s.equals("ndocs")){
                System.out.println(crawler.nDocs());
            }
        }
    }
}