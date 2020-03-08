package helvidios.search.storage;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DocumentRepositoryTest {
    @Test
    public void testStorage(){
        DocumentRepository docRepo = new MongoDbDocumentRepository.Builder()
                                                                  .setDatabase("test-db")
                                                                  .setCollection("testcollection")
                                                                  .build();
        HtmlDocument doc1 = new HtmlDocument("http://www.url1.com", "<html></html>");
        HtmlDocument doc2 = new HtmlDocument("http://www.url1.com", "<html></html>");
        HtmlDocument doc3 = new HtmlDocument("http://www.url1.com", "<html></html>");

        assertThat(docRepo.size(), is(0L));
    }
}