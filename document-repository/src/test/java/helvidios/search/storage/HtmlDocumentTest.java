package helvidios.search.storage;

import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class HtmlDocumentTest{
    @Test
    public void checkDocumentId() throws Exception {
        HtmlDocument doc1 = new HtmlDocument("url1", "<html></html>", "title1");
        HtmlDocument doc2 = new HtmlDocument("url2", "<html></html>", "title2");
        HtmlDocument doc3 = new HtmlDocument("url3", "<html></html>", "title3");
        HtmlDocument doc4 = new HtmlDocument("url4", "<html></html>", "title4");
        HtmlDocument doc5 = new HtmlDocument("url5", "<html></html>", "title5");
        assertThat(doc1.getId(), is(3598498));
        assertThat(doc2.getId(), is(3598499));
        assertThat(doc3.getId(), is(3598500));
        assertThat(doc4.getId(), is(3598501));
        assertThat(doc5.getId(), is(3598502));
    }
}