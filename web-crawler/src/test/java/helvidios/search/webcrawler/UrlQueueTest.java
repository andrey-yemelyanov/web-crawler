package helvidios.search.webcrawler;

import static org.junit.Assert.assertThat;
import org.junit.Test;
import helvidios.search.webcrawler.exceptions.QueueTimeoutException;
import static org.hamcrest.Matchers.*;

public class UrlQueueTest{
    
    @Test(expected = QueueTimeoutException.class)
    public void getUrlWithTimeout() throws InterruptedException, QueueTimeoutException {
        UrlQueue urlQueue = new UrlQueue(2);
        urlQueue.getUrl();
    }

    @Test
    public void addDuplicateUrls() throws InterruptedException, QueueTimeoutException {
        UrlQueue urlQueue = new UrlQueue(20);
        urlQueue.addUrl("https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html");
        urlQueue.addUrl("https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html");
        urlQueue.addUrl("https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html");
        urlQueue.addUrl("https://docs.oracle.com/javase/8/docs/api/noframe.html");
        urlQueue.addUrl("https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html");
        assertThat(urlQueue.size(), is(2));
        assertThat(urlQueue.getUrl(), is("https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html"));
        assertThat(urlQueue.getUrl(), is("https://docs.oracle.com/javase/8/docs/api/noframe.html"));
    }
}