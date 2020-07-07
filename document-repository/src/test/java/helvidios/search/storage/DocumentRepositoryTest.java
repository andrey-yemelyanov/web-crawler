package helvidios.search.storage;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.*;

public class DocumentRepositoryTest {
    @Test
    public void testMongoDbDocRepo(){
        DocumentRepository docRepo = new MongoDbDocumentRepository.Builder()
                                                                  .setDatabase("test-document-db")
                                                                  .build();
        testDocRepo(docRepo);
    }

    @Test
    public void testInMemoryDocStorage(){
        DocumentRepository docRepo = new InMemoryDocumentRepository();
        testDocRepo(docRepo);
    }

    @Test
    public void testCompressedDocRepo(){
        DocumentRepository docRepo = new CompressedDocumentRepository(new InMemoryDocumentRepository());
        testDocRepo(docRepo);
    }

    private void testDocRepo(DocumentRepository docRepo){
        docRepo.clear();

        HtmlDocument doc1 = new HtmlDocument("http://www.url1.com", "<html>1\n2n3</html>", "title1");
        HtmlDocument doc2 = new HtmlDocument("http://www.url2.com", "<html>2\n\n4\n5</html>", "title2");
        HtmlDocument doc3 = new HtmlDocument("http://www.url3.com", "<html>3</html>", "title3");

        assertThat(docRepo.size(), is(0L));
        assertThat(docRepo.get(doc1.getId()), is(nullValue()));
        assertThat(docRepo.get(doc2.getId()), is(nullValue()));
        assertThat(docRepo.get(doc3.getId()), is(nullValue()));

        docRepo.insert(doc1);
        docRepo.insert(doc2);
        docRepo.insert(doc3);

        assertThat(docRepo.size(), is(3L));

        assertThat(docRepo.get(doc1.getUrl()).getContent(), is(doc1.getContent()));
        assertThat(docRepo.get(doc2.getUrl()).getContent(), is(doc2.getContent()));
        assertThat(docRepo.get(doc3.getUrl()).getContent(), is(doc3.getContent()));

        assertThat(docRepo.contains(doc1.getUrl()), is(true));
        assertThat(docRepo.contains(doc2.getUrl()), is(true));
        assertThat(docRepo.contains(doc3.getUrl()), is(true));

        assertThat(docRepo.get(doc1.getId()).getTitle(), is("title1"));
        assertThat(docRepo.get(doc2.getId()).getTitle(), is("title2"));
        assertThat(docRepo.get(doc3.getId()).getTitle(), is("title3"));

        List<HtmlDocument> allDocs = new ArrayList<>();
        docRepo.iterator().forEachRemaining(allDocs::add);
        assertThat(allDocs, is(Arrays.asList(doc1, doc2, doc3)));

        docRepo.clear();
        assertThat(docRepo.size(), is(0L));
    }
}