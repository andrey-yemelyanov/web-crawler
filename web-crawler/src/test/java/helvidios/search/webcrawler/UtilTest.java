package helvidios.search.webcrawler;

import static org.junit.Assert.assertThat;
import java.net.MalformedURLException;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class UtilTest{
    @Test
    public void checksum() throws Exception {
        final String url = "https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html";
        final String expectedHash = "5C9BDEF051BA64082644BF1DE421BE02";
        assertThat(Util.checksum(url), is(expectedHash));
    }

    @Test
    public void getBaseUrl() throws MalformedURLException {
        String url = "https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html";
        assertThat(Util.getBaseUrl(url), is("https://docs.oracle.com"));
    }

    @Test
    public void normalizeUrl(){
        String url = "https://www.w3schools.com/html/?q=qq#234";
        assertThat(Util.normalize(url), is("https://www.w3schools.com/html/"));
    }
}