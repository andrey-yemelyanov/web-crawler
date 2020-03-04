package helvidios.search.webcrawler.storage;

import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import helvidios.search.webcrawler.HtmlDocument;
import helvidios.search.webcrawler.logging.Log;

import static org.hamcrest.Matchers.*;

public class MongoDbStorageTest{

    @Mock Log log;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void testDbAccess() throws Exception {
        DocumentRepository docRepo = new MongoDbStorage.Builder(log)
                                                       .setDatabase("test-db")
                                                       .setCollection("testcollection")
                                                       .build();
        HtmlDocument doc = new HtmlDocument("http://www.url.com", "<html></html>");

        docRepo.clear();

        assertThat(docRepo.size(), is(0));
        assertThat(docRepo.get("docId"), is(nullValue()));

        docRepo.save(doc);
        assertThat(docRepo.size(), is(1));
        assertThat(docRepo.contains(doc.getId()), is(true));

        HtmlDocument storedDoc = docRepo.get(doc.getId());
        assertThat(storedDoc.getId(), is(doc.getId()));
        assertThat(storedDoc.getUrl(), is(doc.getUrl()));
        assertThat(storedDoc.getContent(), is(doc.getContent()));

        docRepo.delete(doc.getId());
        assertThat(docRepo.size(), is(0));
        assertThat(docRepo.get(doc.getId()), is(nullValue()));
    }
}