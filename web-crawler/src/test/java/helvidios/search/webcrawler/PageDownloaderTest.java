package helvidios.search.webcrawler;

import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.HtmlDocument;
import helvidios.search.storage.InMemoryDocumentRepository;
import helvidios.search.webcrawler.url.SimpleUrlExtractor;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.*;

public class PageDownloaderTest {

    @Mock Logger log;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void runPageDownloader() throws Exception {
        final String url1 = "https://www.w3schools.com/page1.html";
        final String url2 = "https://www.w3schools.com/page2.html";
        final String url3 = "https://www.w3schools.com/page3.html";
        final String url4 = "https://www.w3schools.com/page4.html";
        final String page1 = TestUtil.readFile("webgraph/page1.html");
        final String page2 = TestUtil.readFile("webgraph/page2.html");
        final String page3 = TestUtil.readFile("webgraph/page3.html");
        final String page4 = TestUtil.readFile("webgraph/page4.html");
        
        UrlQueue urlQueue = new UrlQueue(1);
        urlQueue.addUrl(url1);

        DocumentRepository docRepo = new InMemoryDocumentRepository();

        PageDownloader pageDownloader = new PageDownloader(
            urlQueue,
            docRepo,
            new SimpleUrlExtractor(Util.getBaseUrl(url1)),
            log
        ){
            @Override
            public String downloadPage(String url) throws Exception {
                if(url.equals(url1)) return page1;
                if(url.equals(url2)) return page2;
                if(url.equals(url3)) return page3;
                if(url.equals(url4)) return page4;
                throw new Exception(String.format("Document for url %s not found.", url));
            }
        };

        pageDownloader.run();

        assertThat(docRepo.size(), is(4L));
        assertThat(urlQueue.size(), is(0));

        List<HtmlDocument> expectedDocs = Arrays.asList(
            new HtmlDocument(url1, page1),
            new HtmlDocument(url2, page2),
            new HtmlDocument(url3, page3),
            new HtmlDocument(url4, page4)
        );
        
        List<HtmlDocument> actualDocs = Arrays.asList(
            docRepo.get(url1),
            docRepo.get(url2),
            docRepo.get(url3),
            docRepo.get(url4)
        );

        assertThat(actualDocs, is(expectedDocs));
    }
}