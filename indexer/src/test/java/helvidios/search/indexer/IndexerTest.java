package helvidios.search.indexer;

import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import helvidios.search.index.Posting;
import helvidios.search.index.Term;
import helvidios.search.index.storage.IndexRepository;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.*;

public class IndexerTest {

    @Mock
    Logger log;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    static class IndexRepoMock implements IndexRepository {

        List<Term> terms = new ArrayList<>();
        List<List<Posting>> postings = new ArrayList<>();

        @Override
        public void addTerm(Term term, List<Posting> postingsList) {
            terms.add(term);
            postings.add(postingsList);
        }

        @Override
        public List<Term> getVocabulary() {return null;}
        @Override
        public List<Posting> getPostingsList(Term term) {return null;}
        @Override
        public void clear() {}
        @Override
        public long size() {return terms.size();}

        Map<Integer, Long> docLen = new HashMap<>();
        @Override
        public long getDocumentLength(int docId) {
            return docLen.get(docId);
        }

        @Override
        public void setDocumentLength(int docId, long len) {
            docLen.put(docId, len);
        }
    }

    @Test
    public void buildIndex1() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1, false)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log, 1);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(new Term("map"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 1, false)
                )
            )));
            
            assertThat(indexRepo.getDocumentLength(1), is(1L));
            assertThat(indexRepo.terms.get(0).idf(), is(0.0));
        }
    }  
    
    @Test
    public void buildIndex2() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1, false),
            new TermDocIdPair("map", 2, false),
            new TermDocIdPair("map", 3, false)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log, 3);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(new Term("map"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 1, false),
                    new Posting(new Term("map"), 2, 1, false),
                    new Posting(new Term("map"), 3, 1, false)
                )
            )));

            assertThat(indexRepo.getDocumentLength(1), is(1L));
            assertThat(indexRepo.getDocumentLength(2), is(1L));
            assertThat(indexRepo.getDocumentLength(3), is(1L));
            assertThat(indexRepo.terms.get(0).idf(), is(0.0));
        }
    }

    @Test
    public void buildIndex3() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1, false),
            new TermDocIdPair("map", 1, false),
            new TermDocIdPair("map", 3, false)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log, 2);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(new Term("map"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 2, false),
                    new Posting(new Term("map"), 3, 1, false)
                )
            )));

            assertThat(indexRepo.getDocumentLength(1), is(2L));
            assertThat(indexRepo.getDocumentLength(3), is(1L));

            assertThat(indexRepo.terms.get(0).idf(), is(0.0));
        }
    }

    @Test
    public void buildIndex4() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1, false),
            new TermDocIdPair("map", 1, false),
            new TermDocIdPair("map", 3, false),
            new TermDocIdPair("map", 3, false),
            new TermDocIdPair("map", 3, false),
            new TermDocIdPair("map", 4, false)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log, 3);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(new Term("map"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 2, false),
                    new Posting(new Term("map"), 3, 3, false),
                    new Posting(new Term("map"), 4, 1, false)
                )
            )));

            assertThat(indexRepo.getDocumentLength(1), is(2L));
            assertThat(indexRepo.getDocumentLength(3), is(3L));
            assertThat(indexRepo.getDocumentLength(4), is(1L));

            assertThat(indexRepo.terms.get(0).idf(), is(0.0));
        }
    }

    @Test
    public void buildIndex5() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1, false),
            new TermDocIdPair("map", 1, false),
            new TermDocIdPair("map", 2, false),
            new TermDocIdPair("node", 3, false),
            new TermDocIdPair("node", 3, false),
            new TermDocIdPair("node", 4, false),
            new TermDocIdPair("node", 5, false),
            new TermDocIdPair("tree", 4, false)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log, 5);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(new Term("map"), new Term("node"), new Term("tree"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 2, false),
                    new Posting(new Term("map"), 2, 1, false)
                ),
                Arrays.asList(
                    new Posting(new Term("node"), 3, 2, false),
                    new Posting(new Term("node"), 4, 1, false),
                    new Posting(new Term("node"), 5, 1, false)
                ),
                Arrays.asList(
                    new Posting(new Term("tree"), 4, 1, false)
                )
            )));

            assertThat(indexRepo.getDocumentLength(1), is(2L));
            assertThat(indexRepo.getDocumentLength(2), is(1L));
            assertThat(indexRepo.getDocumentLength(3), is(2L));
            assertThat(indexRepo.getDocumentLength(4), is(2L));
            assertThat(indexRepo.getDocumentLength(5), is(1L));

            assertThat(indexRepo.terms.get(0).idf(), is(0.3979400086720376));
            assertThat(indexRepo.terms.get(1).idf(), is(0.2218487496163564));
            assertThat(indexRepo.terms.get(2).idf(), is(0.6989700043360189));
        }
    }

    @Test
    public void buildIndex6() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1, false),
            new TermDocIdPair("map", 1, false),
            new TermDocIdPair("map", 1, false),
            new TermDocIdPair("node", 3, false),
            new TermDocIdPair("tree", 4, false)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log, 3);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(
                new Term("map"), 
                new Term("node"), 
                new Term("tree"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 3, false)
                ),
                Arrays.asList(
                    new Posting(new Term("node"), 3, 1, false)
                ),
                Arrays.asList(
                    new Posting(new Term("tree"), 4, 1, false)
                )
            )));

            assertThat(indexRepo.getDocumentLength(1), is(3L));
            assertThat(indexRepo.getDocumentLength(3), is(1L));
            assertThat(indexRepo.getDocumentLength(4), is(1L));

            assertThat(indexRepo.terms.get(0).idf(), is(0.47712125471966244));
            assertThat(indexRepo.terms.get(1).idf(), is(0.47712125471966244));
            assertThat(indexRepo.terms.get(2).idf(), is(0.47712125471966244));
        }
    }
}