package helvidios.search.storage;

import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class HtmlDocumentTest{
    @Test
    public void checkDocumentId() throws Exception {
        HtmlDocument doc1 = new HtmlDocument("url1", "<html></html>");
        HtmlDocument doc2 = new HtmlDocument("url2", "<html></html>");
        HtmlDocument doc3 = new HtmlDocument("url3", "<html></html>");
        HtmlDocument doc4 = new HtmlDocument("url4", "<html></html>");
        HtmlDocument doc5 = new HtmlDocument("url5", "<html></html>");
        assertThat(doc1.getId(), is(3598498));
        assertThat(doc2.getId(), is(3598499));
        assertThat(doc3.getId(), is(3598500));
        assertThat(doc4.getId(), is(3598501));
        assertThat(doc5.getId(), is(3598502));
    }
}