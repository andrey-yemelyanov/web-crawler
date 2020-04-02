package helvidios.search.storage;

import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class HtmlDocumentTest{
    @Test
    public void checkDocumentId() throws Exception {
        HtmlDocument doc = new HtmlDocument(
            "https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html", "<html></html>");
        assertThat(doc.getId(), is(1866136580));
    }
}