package helvidios.search.webcrawler;

import static org.junit.Assert.assertThat;
import java.net.MalformedURLException;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class UtilTest{
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