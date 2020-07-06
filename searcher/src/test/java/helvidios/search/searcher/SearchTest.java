package helvidios.search.searcher;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import helvidios.search.index.Posting;
import helvidios.search.index.Term;
import helvidios.search.index.storage.IndexRepository;
import helvidios.search.linguistics.ApacheNlpLemmatizer;
import helvidios.search.storage.DocId;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.HtmlDocument;
import helvidios.search.tokenizer.HtmlTokenizer;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class SearchTest {

    @Test
    public void searchWithTermsInDocTitle() throws Exception {
        
        Logger log = mock(Logger.class);
        
        DocumentRepository docRepo = new DocRepoMock();
        
        final String url1000 = "http://doc1000.url";
        final String url1001 = "http://doc1001.url";
        final String url1002 = "http://doc1002.url";
        final String url1010 = "http://doc1010.url";
        final String url1020 = "http://doc1020.url";

        final int doc1000 = new DocId(url1000).get();
        final int doc1001 = new DocId(url1001).get();
        final int doc1002 = new DocId(url1002).get();
        final int doc1010 = new DocId(url1010).get();
        final int doc1020 = new DocId(url1020).get();

        docRepo.insert(new HtmlDocument(url1000, "doc1000content", "doc1000 HashMap<T,V>"));
        docRepo.insert(new HtmlDocument(url1001, "doc1001content", "doc1001"));
        docRepo.insert(new HtmlDocument(url1002, "doc1002content", "doc1002"));
        docRepo.insert(new HtmlDocument(url1010, "doc1010content", "doc1010"));
        docRepo.insert(new HtmlDocument(url1020, "doc1020content", "doc1020"));

        IndexRepository indexRepo = new IndexRepositoryMock();
        
        Term hashmap = new Term("hashmap", 3, 0.2218487496);
        Term array = new Term("array", 2, 0.3979400087);
        Term bitset = new Term("bitset", 3, 0.2218487496);

        indexRepo.addTerm(hashmap, Arrays.asList(
            new Posting(hashmap, doc1000, 15),
            new Posting(hashmap, doc1010, 25),
            new Posting(hashmap, doc1020, 30)
        ));

        indexRepo.addTerm(array, Arrays.asList(
            new Posting(array, doc1000, 15),
            new Posting(array, doc1001, 25)
        ));

        indexRepo.addTerm(bitset, Arrays.asList(
            new Posting(bitset, doc1000, 15),
            new Posting(bitset, doc1001, 25),
            new Posting(bitset, doc1002, 30)
        ));

        try(ApacheNlpLemmatizer lemmatizer = new ApacheNlpLemmatizer()){
            Searcher searcher = new IndexSearcher(indexRepo, docRepo, new HtmlTokenizer(), lemmatizer, log);

            List<Match> matches = searcher.search("hashmap", 5);
            assertThat(matches.size(), is(3));

            Match m1 = matches.get(0);
            Match m2 = matches.get(1);
            Match m3 = matches.get(2);

            assertThat(m1.docId(), is(doc1000));
            assertThat(m1.content(), is("doc1000content"));
            assertThat(m1.title(), is("doc1000 HashMap<T,V>"));
            assertThat(m1.score(), is(1.4827631248369926));

            assertThat(m2.docId(), is(doc1020));
            assertThat(m2.content(), is("doc1020content"));
            assertThat(m2.title(), is("doc1020"));
            assertThat(m2.score(), is(0.5495462529671402));

            assertThat(m3.docId(), is(doc1010));
            assertThat(m3.content(), is("doc1010content"));
            assertThat(m3.title(), is("doc1010"));
            assertThat(m3.score(), is(0.5319799925397047));
        }   
    }

    @Test
    public void search() throws Exception {
        
        Logger log = mock(Logger.class);
        
        DocumentRepository docRepo = new DocRepoMock();
        
        final String url1000 = "http://doc1000.url";
        final String url1001 = "http://doc1001.url";
        final String url1002 = "http://doc1002.url";
        final String url1010 = "http://doc1010.url";
        final String url1020 = "http://doc1020.url";

        final int doc1000 = new DocId(url1000).get();
        final int doc1001 = new DocId(url1001).get();
        final int doc1002 = new DocId(url1002).get();
        final int doc1010 = new DocId(url1010).get();
        final int doc1020 = new DocId(url1020).get();

        docRepo.insert(new HtmlDocument(url1000, "doc1000content", "doc1000"));
        docRepo.insert(new HtmlDocument(url1001, "doc1001content", "doc1001"));
        docRepo.insert(new HtmlDocument(url1002, "doc1002content", "doc1002"));
        docRepo.insert(new HtmlDocument(url1010, "doc1010content", "doc1010"));
        docRepo.insert(new HtmlDocument(url1020, "doc1020content", "doc1020"));

        IndexRepository indexRepo = new IndexRepositoryMock();
        
        Term hashmap = new Term("hashmap", 3, 0.2218487496);
        Term array = new Term("array", 2, 0.3979400087);
        Term bitset = new Term("bitset", 3, 0.2218487496);

        indexRepo.addTerm(hashmap, Arrays.asList(
            new Posting(hashmap, doc1000, 15),
            new Posting(hashmap, doc1010, 25),
            new Posting(hashmap, doc1020, 30)
        ));

        indexRepo.addTerm(array, Arrays.asList(
            new Posting(array, doc1000, 15),
            new Posting(array, doc1001, 25)
        ));

        indexRepo.addTerm(bitset, Arrays.asList(
            new Posting(bitset, doc1000, 15),
            new Posting(bitset, doc1001, 25),
            new Posting(bitset, doc1002, 30)
        ));

        try(ApacheNlpLemmatizer lemmatizer = new ApacheNlpLemmatizer()){
            Searcher searcher = new IndexSearcher(indexRepo, docRepo, new HtmlTokenizer(), lemmatizer, log);

            List<Match> matches = searcher.search("hashmap", 5);
            assertThat(matches.size(), is(3));

            Match m1 = matches.get(0);
            Match m2 = matches.get(1);
            Match m3 = matches.get(2);

            assertThat(m1.docId(), is(doc1020));
            assertThat(m1.content(), is("doc1020content"));
            assertThat(m1.title(), is("doc1020"));
            assertThat(m1.score(), is(0.5495462529671402));

            assertThat(m2.docId(), is(doc1010));
            assertThat(m2.content(), is("doc1010content"));
            assertThat(m2.title(), is("doc1010"));
            assertThat(m2.score(), is(0.5319799925397047));

            assertThat(m3.docId(), is(doc1000));
            assertThat(m3.content(), is("doc1000content"));
            assertThat(m3.title(), is("doc1000"));
            assertThat(m3.score(), is(0.4827631248369926));

            matches = searcher.search("hashmap array", 5);
            assertThat(matches.size(), is(4));

            m1 = matches.get(0);
            m2 = matches.get(1);
            m3 = matches.get(2);
            Match m4 = matches.get(3);

            assertThat(m1.docId(), is(doc1000));
            assertThat(m1.content(), is("doc1000content"));
            assertThat(m1.title(), is("doc1000"));
            assertThat(m1.score(), is(1.3487168993976044));

            assertThat(m2.docId(), is(doc1001));
            assertThat(m2.content(), is("doc1001content"));
            assertThat(m2.title(), is("doc1001"));
            assertThat(m2.score(), is(0.9542362679130287));

            assertThat(m3.docId(), is(doc1020));
            assertThat(m3.content(), is("doc1020content"));
            assertThat(m3.title(), is("doc1020"));
            assertThat(m3.score(), is(0.5495462529671402));

            assertThat(m4.docId(), is(doc1010));
            assertThat(m4.content(), is("doc1010content"));
            assertThat(m4.title(), is("doc1010"));
            assertThat(m4.score(), is(0.5319799925397047));
        }
        
    }

}