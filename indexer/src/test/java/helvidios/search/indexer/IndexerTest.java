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
        public long size() {return 0;}
    }

    @Test
    public void buildIndex1() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(new Term("map"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 1)
                )
            )));
        }
    }  
    
    @Test
    public void buildIndex2() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1),
            new TermDocIdPair("map", 2),
            new TermDocIdPair("map", 3)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(new Term("map"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 1),
                    new Posting(new Term("map"), 2, 1),
                    new Posting(new Term("map"), 3, 1)
                )
            )));
        }
    }

    @Test
    public void buildIndex3() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1),
            new TermDocIdPair("map", 1),
            new TermDocIdPair("map", 3)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(new Term("map"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 2),
                    new Posting(new Term("map"), 3, 1)
                )
            )));
        }
    }

    @Test
    public void buildIndex4() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1),
            new TermDocIdPair("map", 1),
            new TermDocIdPair("map", 3),
            new TermDocIdPair("map", 3),
            new TermDocIdPair("map", 3),
            new TermDocIdPair("map", 4)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(new Term("map"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 2),
                    new Posting(new Term("map"), 3, 3),
                    new Posting(new Term("map"), 4, 1)
                )
            )));
        }
    }

    @Test
    public void buildIndex5() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1),
            new TermDocIdPair("map", 1),
            new TermDocIdPair("map", 2),
            new TermDocIdPair("node", 3),
            new TermDocIdPair("node", 3),
            new TermDocIdPair("node", 4),
            new TermDocIdPair("node", 5),
            new TermDocIdPair("tree", 4)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(new Term("map"), new Term("node"), new Term("tree"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 2),
                    new Posting(new Term("map"), 2, 1)
                ),
                Arrays.asList(
                    new Posting(new Term("node"), 3, 2),
                    new Posting(new Term("node"), 4, 1),
                    new Posting(new Term("node"), 5, 1)
                ),
                Arrays.asList(
                    new Posting(new Term("tree"), 4, 1)
                )
            )));
        }
    }

    @Test
    public void buildIndex6() throws Exception {
        try(BlockReader br = new BlockReaderMock(Arrays.asList(
            new TermDocIdPair("map", 1),
            new TermDocIdPair("node", 3),
            new TermDocIdPair("tree", 4)
        ))){
            IndexRepoMock indexRepo = new IndexRepoMock();
            Indexer indexer = new Indexer(indexRepo, br, log);
            indexer.buildIndex();
            assertThat(indexRepo.terms, is(Arrays.asList(new Term("map"), new Term("node"), new Term("tree"))));
            assertThat(indexRepo.postings, is(Arrays.asList(
                Arrays.asList(
                    new Posting(new Term("map"), 1, 1)
                ),
                Arrays.asList(
                    new Posting(new Term("node"), 3, 1)
                ),
                Arrays.asList(
                    new Posting(new Term("tree"), 4, 1)
                )
            )));
        }
    }
}