package helvidios.search.index;

import org.junit.Test;
import helvidios.search.index.storage.IndexRepository;
import helvidios.search.index.storage.MongoDbIndexRepository;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class IndexRepositoryTest {

    private final static String DB = "test-index";

    @Test
    public void addTerm(){
        IndexRepository indexRepo = new MongoDbIndexRepository.Builder()
                                                              .setDatabase(DB)
                                                              .build();
        indexRepo.clear();

        Term term = new Term("hashmap");

        List<Posting> postingsList = Arrays.asList(
            new Posting(term, 1000, 15),
            new Posting(term, 1001, 25),
            new Posting(term, 1002, 30)
        );

        indexRepo.addTerm(term, postingsList);

        List<Posting> storedPostingsList = indexRepo.getPostingsList(term);
        assertThat(storedPostingsList, is(postingsList));
        assertThat(indexRepo.size(), is(1L));

        List<Term> vocabulary = indexRepo.getVocabulary();
        assertThat(vocabulary.size(), is(1));
        assertThat(vocabulary.get(0).df(), is(3));
    }

    @Test
    public void getVocabulary(){

        IndexRepository indexRepo = new MongoDbIndexRepository.Builder()
                                                              .setDatabase(DB)
                                                              .build();
        indexRepo.clear();

        Term term1 = new Term("hashmap");
        List<Posting> postingsList1 = Arrays.asList(
            new Posting(term1, 1000, 15),
            new Posting(term1, 1001, 25),
            new Posting(term1, 1002, 30)
        );

        Term term2 = new Term("array");
        List<Posting> postingsList2 = Arrays.asList(
            new Posting(term2, 1000, 15),
            new Posting(term2, 1001, 25),
            new Posting(term2, 1002, 30)
        );

        Term term3 = new Term("bitset");
        List<Posting> postingsList3 = Arrays.asList(
            new Posting(term3, 1000, 15),
            new Posting(term3, 1001, 25),
            new Posting(term3, 1002, 30)
        );

        indexRepo.addTerm(term1, postingsList1);
        indexRepo.addTerm(term2, postingsList2);
        indexRepo.addTerm(term3, postingsList3);

        List<Term> vocabulary = indexRepo.getVocabulary();

        assertThat(vocabulary, is(Arrays.asList(term2, term3, term1)));
        assertThat(vocabulary.get(0).df(), is(3));
        assertThat(vocabulary.get(1).df(), is(3));
        assertThat(vocabulary.get(2).df(), is(3));
    }

    @Test(expected = Exception.class)
    public void  nonUniqueTerm(){
        IndexRepository indexRepo = new MongoDbIndexRepository.Builder()
                                                              .setDatabase(DB)
                                                              .build();
        indexRepo.clear();

        Term term1 = new Term("hashmap");
        List<Posting> postingsList1 = Arrays.asList(
            new Posting(term1, 1000, 15),
            new Posting(term1, 1001, 25),
            new Posting(term1, 1002, 30)
        );

        Term term2 = new Term("hashmap");
        List<Posting> postingsList2 = Arrays.asList(
            new Posting(term2, 1000, 15),
            new Posting(term2, 1001, 25),
            new Posting(term2, 1002, 30)
        );

        indexRepo.addTerm(term1, postingsList1);
        indexRepo.addTerm(term2, postingsList2);
    }

    @Test
    public void storeTfIdfScore(){
        IndexRepository indexRepo = new MongoDbIndexRepository.Builder()
                                                              .setDatabase(DB)
                                                              .build();
        indexRepo.clear();

        Term term = new Term("hashmap");
        Posting posting = new Posting(term, 1000, 15);
        posting.setTfIdfScore(0.4321);
        List<Posting> postingsList = Arrays.asList(posting);

        indexRepo.addTerm(term, postingsList);

        List<Posting> actual = indexRepo.getPostingsList(term);
        assertThat(actual.size(), is(1));
        assertThat(actual.get(0).term().equals(term), is(true));
        assertThat(actual.get(0).docId(), is(1000));
        assertThat(actual.get(0).tf(), is(15));
        assertThat(actual.get(0).tfIdfScore(), is(0.4321));
    }

    @Test
    public void storeDocVectorMagnitude(){
        IndexRepository indexRepo = new MongoDbIndexRepository.Builder()
                                                              .setDatabase(DB)
                                                              .build();
        indexRepo.clear();

        indexRepo.addDocumentVectorMagnitude(123, 0.55432);
        indexRepo.addDocumentVectorMagnitude(1234, 23.55431);

        assertThat(indexRepo.documentVectorMagnitude(123), is(0.55432));
        assertThat(indexRepo.documentVectorMagnitude(1234), is(23.55431));
    }
}