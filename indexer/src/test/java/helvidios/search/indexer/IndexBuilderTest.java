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
                new Term("title1"), // title term
                new Term("title2"), // title term
                new Term("tutorial"),
                new Term("visit")
            );

            assertThat(indexRepo.terms, is(ex));

            // 2
            assertThat(indexRepo.terms.get(0).df(), is(1));
            assertThat(indexRepo.terms.get(0).idf(), is(0.3010299956639812));

            // attribute
            assertThat(indexRepo.terms.get(1).df(), is(1));
            assertThat(indexRepo.terms.get(1).idf(), is(0.3010299956639812));

            // html
            assertThat(indexRepo.terms.get(2).df(), is(2));
            assertThat(indexRepo.terms.get(2).idf(), is(0.0));

            // link
            assertThat(indexRepo.terms.get(3).df(), is(1));
            assertThat(indexRepo.terms.get(3).idf(), is(0.3010299956639812));

            // link1
            assertThat(indexRepo.terms.get(4).df(), is(2));
            assertThat(indexRepo.terms.get(4).idf(), is(0.0));

            // page
            assertThat(indexRepo.terms.get(5).df(), is(1));
            assertThat(indexRepo.terms.get(5).idf(), is(0.3010299956639812));

            // title
            assertThat(indexRepo.terms.get(6).df(), is(2));
            assertThat(indexRepo.terms.get(6).idf(), is(0.0));

            // title1
            assertThat(indexRepo.terms.get(7).df(), is(1));
            assertThat(indexRepo.terms.get(7).idf(), is(0.3010299956639812));

            // title2
            assertThat(indexRepo.terms.get(8).df(), is(1));
            assertThat(indexRepo.terms.get(8).idf(), is(0.3010299956639812));

            // tutorial
            assertThat(indexRepo.terms.get(9).df(), is(2));
            assertThat(indexRepo.terms.get(9).idf(), is(0.0));

            // visit
            assertThat(indexRepo.terms.get(10).df(), is(1));
            assertThat(indexRepo.terms.get(10).idf(), is(0.3010299956639812));

            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(new Posting(ex.get(0), docId2, 1)), // 2
                Arrays.asList(new Posting(ex.get(1), docId2, 1)), // attribute
                Arrays.asList(new Posting(ex.get(2), docId1, 2), new Posting(ex.get(2), docId2, 1)), // html
                Arrays.asList(new Posting(ex.get(3), docId1, 1)), // link
                Arrays.asList(new Posting(ex.get(4), docId1, 2), new Posting(ex.get(4), docId2, 1)), // link1
                Arrays.asList(new Posting(ex.get(5), docId2, 1)), // page
                Arrays.asList(new Posting(ex.get(6), docId1, 2), new Posting(ex.get(6), docId2, 1)), // title
                Arrays.asList(new Posting(ex.get(7), docId1, 1)), // title1
                Arrays.asList(new Posting(ex.get(8), docId2, 1)), // title2
                Arrays.asList(new Posting(ex.get(9), docId1, 2), new Posting(ex.get(9), docId2, 1)), // tutorial
                Arrays.asList(new Posting(ex.get(10), docId1, 1)) // visit
            )));
        }
    }
}