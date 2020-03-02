package helvidios.search.webcrawler.url;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import helvidios.search.webcrawler.HtmlDocument;
import helvidios.search.webcrawler.TestUtil;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UrlExtractorTest {
    @Test
    public void extractUrls() throws IOException, Exception {
        HtmlDocument doc = new HtmlDocument("http://www.mycrawler.net/dir1/dir2/page.html",
                TestUtil.readFile("page1.html"));

        UrlExtractor extractor = new SimpleUrlExtractor();
        List<String> urls = extractor.getUrls(doc);
        List<String> expected = Arrays.asList("https://www.w3schools.com/html/",
                "http://www.mycrawler.net/html/default.asp", "http://www.mycrawler.net/admin",
                "http://www.mycrawler.net/dir1/dir2/start");
        Collections.sort(expected);
        assertThat(urls, is(expected));
    }

    @Test
    public void extractUrlsWithPrefix() throws IOException, Exception {
        HtmlDocument doc = new HtmlDocument("http://www.mycrawler.net/dir1/dir2/page.html",
                TestUtil.readFile("page1.html"));

        UrlExtractor extractor = new SimpleUrlExtractor("https://www.w3schools.com");
        List<String> urls = extractor.getUrls(doc);
        List<String> expected = Arrays.asList("https://www.w3schools.com/html/");
        Collections.sort(expected);
        assertThat(urls, is(expected));
    }

    @Test
    public void noMathchingUrls() throws IOException, URISyntaxException, Exception {
        HtmlDocument doc = new HtmlDocument("http://www.mycrawler.net/dir1/dir2/page.html", TestUtil.readFile("page1.html"));
        UrlExtractor extractor = new SimpleUrlExtractor("https://www.google.com");
        List<String> urls = extractor.getUrls(doc);
        assertThat(urls.isEmpty(), is(true));
    }
}