package helvidios.search.index;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import helvidios.search.index.storage.IndexRepository;
import helvidios.search.index.storage.MongoDbIndexRepository;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class IndexTest {

    @Test
    public void getPostingsList(){
        Logger log = mock(Logger.class);

        IndexRepository indexRepo = new MongoDbIndexRepository.Builder()
                                                              .setDatabase("test-index")
                                                              .build();
        indexRepo.clear();

        Term term1 = new Term("hashmap");
        List<Posting> postingsList1 = Arrays.asList(
            new Posting(term1, 1000, 15, true),
            new Posting(term1, 1001, 25),
            new Posting(term1, 1002, 30)
        );

        Term term2 = new Term("array");
        List<Posting> postingsList2 = Arrays.asList(
            new Posting(term2, 1000, 15, true),
            new Posting(term2, 1001, 25),
            new Posting(term2, 1002, 30)
        );

        Term term3 = new Term("bitset");
        List<Posting> postingsList3 = Arrays.asList(
            new Posting(term3, 1000, 15, true),
            new Posting(term3, 1001, 25),
            new Posting(term3, 1002, 30)
        );

        indexRepo.addTerm(term1, postingsList1);
        indexRepo.addTerm(term2, postingsList2);
        indexRepo.addTerm(term3, postingsList3);

        Index index = new Index(indexRepo, log);
        assertThat(index.size(), is(3L));
        assertThat(index.vocabulary(), is(Arrays.asList(
            "array", "bitset", "hashmap"
        )));
        assertThat(index.postingsList("array"), is(Arrays.asList(
            new Posting(term2, 1000, 15),
            new Posting(term2, 1001, 25),
            new Posting(term2, 1002, 30)
        )));
        assertThat(index.postingsList("hashmap"), is(Arrays.asList(
            new Posting(term1, 1000, 15),
            new Posting(term1, 1001, 25),
            new Posting(term1, 1002, 30)
        )));
        assertThat(index.postingsList("abracadabra"), is(Arrays.asList()));
        assertThat(index.postingsList("termdoesnotexist"), is(Arrays.asList()));

        List<Posting> hashmapPostings = index.postingsList("hashmap");
        assertThat(hashmapPostings.get(0).termAppearsInDocTitle(), is(true));
        assertThat(hashmapPostings.get(1).termAppearsInDocTitle(), is(false));
        assertThat(hashmapPostings.get(2).termAppearsInDocTitle(), is(false));
    }
}