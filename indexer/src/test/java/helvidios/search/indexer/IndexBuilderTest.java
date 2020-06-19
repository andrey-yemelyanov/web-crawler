package helvidios.search.indexer;

import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import helvidios.search.index.Posting;
import helvidios.search.index.Term;
import helvidios.search.linguistics.ApacheNlpLemmatizer;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.HtmlDocument;
import helvidios.search.tokenizer.HtmlTokenizer;
import helvidios.search.tokenizer.Tokenizer;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.*;

public class IndexBuilderTest {

    @Mock
    Logger log;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private static String readFile(String path) throws Exception {
        java.net.URL url = InverterTest.class.getResource(path);
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        return new String(java.nio.file.Files.readAllBytes(resPath), "UTF8"); 
    }

    @Test
    public void buildIndex() throws Exception{
        final String page1 = readFile("page1.html");
        final String page2 = readFile("page2.html");
        DocumentRepository docRepo = new DocRepoMock(Arrays.asList(
            new HtmlDocument("url1", page1, "title1"),
            new HtmlDocument("url2", page2, "title2")
        ));
        final int docId1 = "url1".hashCode();
        final int docId2 = "url2".hashCode();

        Tokenizer tokenizer = new HtmlTokenizer();
        try(ApacheNlpLemmatizer lemmatizer = new ApacheNlpLemmatizer()){
            IndexRepoMock indexRepo = new IndexRepoMock();
            try(IndexBuilder indexBuilder = new IndexBuilder(
                docRepo, indexRepo, tokenizer, lemmatizer, log)){
                indexBuilder.build();
            }

            List<Term> ex = Arrays.asList(
                new Term("2"),
                new Term("attribute"),
                new Term("html"),
                new Term("link"),
                new Term("link1"),
                new Term("page"),
                new Term("title"),
                new Term("tutorial"),
                new Term("visit")
            );

            assertThat(indexRepo.terms, is(ex));

            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(new Posting(ex.get(0), docId2, 1)),
                Arrays.asList(new Posting(ex.get(1), docId2, 1)),
                Arrays.asList(new Posting(ex.get(2), docId1, 2), new Posting(ex.get(2), docId2, 1)),
                Arrays.asList(new Posting(ex.get(3), docId1, 1)),
                Arrays.asList(new Posting(ex.get(4), docId1, 2), new Posting(ex.get(4), docId2, 1)),
                Arrays.asList(new Posting(ex.get(5), docId2, 1)),
                Arrays.asList(new Posting(ex.get(6), docId1, 2), new Posting(ex.get(6), docId2, 1)),
                Arrays.asList(new Posting(ex.get(7), docId1, 2), new Posting(ex.get(7), docId2, 1)),
                Arrays.asList(new Posting(ex.get(8), docId1, 1))
            )));
        }
    }
}