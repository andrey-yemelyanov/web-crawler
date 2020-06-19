package helvidios.search.indexer;

import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import helvidios.search.linguistics.ApacheNlpLemmatizer;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.HtmlDocument;
import helvidios.search.tokenizer.HtmlTokenizer;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.*;

public class InverterTest {

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
    public void invert() throws Exception {
        final String page1 = readFile("page1.html");
        final String page2 = readFile("page2.html");
        DocumentRepository docRepo = new DocRepoMock(Arrays.asList(
            new HtmlDocument("url1", page1, "title1"),
            new HtmlDocument("url2", page2, "title2")
        ));
        final int docId1 = "url1".hashCode();
        final int docId2 = "url2".hashCode();

        try(ApacheNlpLemmatizer lemmatizer =  new ApacheNlpLemmatizer()){
            Inverter inverter = new Inverter(docRepo, new HtmlTokenizer(), lemmatizer, log);
            List<String> blocks = inverter.buildPostings();
            assertThat(blocks.size(), is(1));

            try(BlockReader br = new FileBlockReader(blocks.get(0), log)){
                List<TermDocIdPair> pairs = new ArrayList<>();
                br.iterator().forEachRemaining(pair -> pairs.add(pair));
                assertThat(pairs, is(Arrays.asList(
                    new TermDocIdPair("2", docId2),
                    new TermDocIdPair("attribute", docId2),
                    new TermDocIdPair("html", docId1),
                    new TermDocIdPair("html", docId1),
                    new TermDocIdPair("html", docId2),
                    new TermDocIdPair("link", docId1),
                    new TermDocIdPair("link1", docId1),
                    new TermDocIdPair("link1", docId1),
                    new TermDocIdPair("link1", docId2),
                    new TermDocIdPair("page", docId2),
                    new TermDocIdPair("title", docId1),
                    new TermDocIdPair("title", docId1),
                    new TermDocIdPair("title", docId2),
                    new TermDocIdPair("tutorial", docId1),
                    new TermDocIdPair("tutorial", docId1),
                    new TermDocIdPair("tutorial", docId2),
                    new TermDocIdPair("visit", docId1)
                )));
            }
        }
    }
}