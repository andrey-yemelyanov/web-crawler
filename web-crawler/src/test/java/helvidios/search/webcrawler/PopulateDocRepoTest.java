package helvidios.search.webcrawler;

import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import helvidios.search.storage.*;
import java.util.*;

public class PopulateDocRepoTest {

    @Mock
    Logger log;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    @Ignore
    public void populateDocRepo() throws Exception {

        DocumentRepository docRepo = new CompressedDocumentRepository(
                                        new MongoDbDocumentRepository.Builder()
                                                                     .setDatabase("javadocs-compressed-doc-db")
                                                                     .build(), log);

        docRepo.clear();

        List<String> urls = Arrays.asList(
            "https://docs.oracle.com/javase/8/docs/api/overview-summary.html",
            "https://docs.oracle.com/javase/8/docs/api/java/io/package-summary.html",
            "https://docs.oracle.com/javase/8/docs/api/java/io/OutputStreamWriter.html",
            "https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html",
            "https://docs.oracle.com/javase/8/docs/api/java/util/Map.html",
            "https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html",
            "https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html",
            "https://docs.oracle.com/javase/8/docs/api/javax/xml/transform/TransformerException.html",
            "https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html",
            "https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html",
            "https://docs.oracle.com/javase/8/docs/api/java/lang/ref/WeakReference.html",
            "https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html"
        );

        for(String url : urls){
            if(docRepo.contains(url)){
                System.out.printf("URL %s already downloaded. Skipping...\n", url);
                continue;
            }

            String htmlContent = Jsoup.connect(url).get().html();
            String title = Jsoup.parse(htmlContent).title().trim();
            System.out.printf("Downloaded '%s' from %s\n", title, url);
            HtmlDocument doc = new HtmlDocument(url, htmlContent, title);
            docRepo.insert(doc);
        }

        System.out.println("DocRepo populated.");
        
    }

}