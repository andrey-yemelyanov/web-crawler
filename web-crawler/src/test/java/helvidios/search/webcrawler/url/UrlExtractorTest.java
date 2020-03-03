package helvidios.search.webcrawler.url;

import org.junit.Test;
import helvidios.search.webcrawler.HtmlDocument;
import helvidios.search.webcrawler.TestUtil;
import helvidios.search.webcrawler.Util;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UrlExtractorTest {
    @Test
    public void extractUrls() throws Exception {
        HtmlDocument doc = new HtmlDocument("http://www.mycrawler.net/dir1/dir2/page.html",
                TestUtil.readFile("page1.html"));

        UrlExtractor extractor = new SimpleUrlExtractor(Util.getBaseUrl(doc.getUrl()));
        List<String> urls = extractor.getUrls(doc);
        List<String> expected = Arrays.asList(
            "http://www.mycrawler.net/admin",
            "http://www.mycrawler.net/dir1/dir2/start",
            "http://www.mycrawler.net/html/default.asp");
        assertThat(urls, is(expected));
    }

    @Test
    public void extractUrlsWithPrefix() throws Exception {
        HtmlDocument doc = new HtmlDocument("http://www.mycrawler.net/dir1/dir2/page.html",
                TestUtil.readFile("page1.html"));

        UrlExtractor extractor = new SimpleUrlExtractor("https://www.w3schools.com");
        List<String> urls = extractor.getUrls(doc);
        List<String> expected = Arrays.asList("https://www.w3schools.com/html/");
        Collections.sort(expected);
        assertThat(urls, is(expected));
    }

    @Test
    public void noMathchingUrls() throws Exception {
        HtmlDocument doc = new HtmlDocument("http://www.mycrawler.net/dir1/dir2/page.html", TestUtil.readFile("page1.html"));
        UrlExtractor extractor = new SimpleUrlExtractor("https://www.google.com");
        List<String> urls = extractor.getUrls(doc);
        assertThat(urls.isEmpty(), is(true));
    }

    @Test
    public void getUrlsJavaDocs() throws Exception {
        HtmlDocument doc = new HtmlDocument("https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html", TestUtil.readFile("JavaDoc.html"));
        String baseUrl = Util.getBaseUrl(doc.getUrl());
        UrlExtractor extractor = new SimpleUrlExtractor(baseUrl);
        List<String> urls = extractor.getUrls(doc);
        assertThat(urls.size(), is(4240));
        assertThat(urls.stream().allMatch(url -> url.startsWith(baseUrl)), is(true));
    }

    @Test
    public void getUrlsDiadrom() throws Exception{
        HtmlDocument doc = new HtmlDocument("https://diadrom.se/", TestUtil.readFile("diadrom.html"));
        String baseUrl = Util.getBaseUrl(doc.getUrl());
        UrlExtractor extractor = new SimpleUrlExtractor(baseUrl);
        List<String> urls = extractor.getUrls(doc);
        assertThat(urls.stream().allMatch(url -> url.startsWith(baseUrl)), is(true));
    }
}