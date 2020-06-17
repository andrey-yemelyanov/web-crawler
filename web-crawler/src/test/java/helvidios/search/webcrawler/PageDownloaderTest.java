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

    @Mock
    Logger log;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void getHtmlTitle() throws Exception {
        final String page1 = TestUtil.readFile("webgraph/page1.html");
        final String page2 = TestUtil.readFile("webgraph/page2.html");
        
        UrlQueue urlQueue = new UrlQueue(1);
        DocumentRepository docRepo = new InMemoryDocumentRepository();
        PageDownloader pageDownloader = new InternetPageDownloader(
            urlQueue,
            docRepo,
            new SimpleUrlExtractor("BASEURL"),
            log
        );

        assertThat(pageDownloader.getDocumentTitle(page1), is("Unknown document title"));
        assertThat(pageDownloader.getDocumentTitle(page2), is("Page2 Title Long Text"));
    }

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

            @Override
            protected String getDocumentTitle(String content) {
                return "titleABC";
            }
        };

        pageDownloader.run();

        assertThat(docRepo.size(), is(4L));
        assertThat(urlQueue.size(), is(0));

        List<HtmlDocument> expectedDocs = Arrays.asList(
            new HtmlDocument(url1, page1, "title1"),
            new HtmlDocument(url2, page2, "title1"),
            new HtmlDocument(url3, page3, "title1"),
            new HtmlDocument(url4, page4, "title1")
        );
        
        List<HtmlDocument> actualDocs = Arrays.asList(
            docRepo.get(url1),
            docRepo.get(url2),
            docRepo.get(url3),
            docRepo.get(url4)
        );

        assertThat(actualDocs, is(expectedDocs));

        assertThat(docRepo.get(url1).getTitle(), is("titleABC"));
        assertThat(docRepo.get(url2).getTitle(), is("titleABC"));
        assertThat(docRepo.get(url3).getTitle(), is("titleABC"));
        assertThat(docRepo.get(url4).getTitle(), is("titleABC"));
    }
}