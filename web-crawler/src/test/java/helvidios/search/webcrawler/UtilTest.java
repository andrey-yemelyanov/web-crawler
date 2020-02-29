package helvidios.search.webcrawler;

import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class UtilTest{
    @Test
    public void checksum() throws Exception {
        final String url = "https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html";
        final String expectedHash = "5C9BDEF051BA64082644BF1DE421BE02";
        assertThat(Util.checksum(url), is(expectedHash));
    }
}