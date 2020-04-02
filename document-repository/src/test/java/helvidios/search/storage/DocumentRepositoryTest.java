package helvidios.search.storage;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.*;

public class DocumentRepositoryTest {
    @Test
    public void testMongoDbDocRepo(){
        DocumentRepository docRepo = new MongoDbDocumentRepository.Builder()
                                                                  .setDatabase("test-db")
                                                                  .setCollection("testcollection")
                                                                  .build();
        testDocRepo(docRepo);
    }

    @Test
    public void testInMemoryDocStorage(){
        DocumentRepository docRepo = new InMemoryDocumentRepository();
        testDocRepo(docRepo);
    }

    private void testDocRepo(DocumentRepository docRepo){
        docRepo.clear();

        HtmlDocument doc1 = new HtmlDocument("http://www.url1.com", "<html></html>");
        HtmlDocument doc2 = new HtmlDocument("http://www.url2.com", "<html></html>");
        HtmlDocument doc3 = new HtmlDocument("http://www.url3.com", "<html></html>");

        assertThat(docRepo.size(), is(0L));
        assertThat(docRepo.get(doc1.getId()), is(nullValue()));
        assertThat(docRepo.get(doc2.getId()), is(nullValue()));
        assertThat(docRepo.get(doc3.getId()), is(nullValue()));

        docRepo.insert(doc1);
        docRepo.insert(doc2);
        docRepo.insert(doc3);

        assertThat(docRepo.size(), is(3L));

        assertThat(docRepo.get(doc1.getUrl()).getContent(), is(doc1.getContent()));

        assertThat(docRepo.contains(doc1.getUrl()), is(true));
        assertThat(docRepo.contains(doc2.getUrl()), is(true));
        assertThat(docRepo.contains(doc3.getUrl()), is(true));

        List<HtmlDocument> allDocs = new ArrayList<>();
        docRepo.iterator().forEachRemaining(allDocs::add);
        assertThat(allDocs, is(Arrays.asList(doc1, doc2, doc3)));

        docRepo.clear();
        assertThat(docRepo.size(), is(0L));
    }
}