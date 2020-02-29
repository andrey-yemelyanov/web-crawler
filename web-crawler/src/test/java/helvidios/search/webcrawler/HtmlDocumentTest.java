package helvidios.search.webcrawler;

import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class HtmlDocumentTest{
    @Test
    public void checkDocumentId() throws Exception {
        HtmlDocument doc = new HtmlDocument(
            "https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html", "<html></html>");
        assertThat(doc.getId(), is("5C9BDEF051BA64082644BF1DE421BE02"));
    }
}