package helvidios.search.indexer;

import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.*;

public class BlockTest {
    
    @Mock
    Logger log;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void writePosting() throws Exception {
        String filePath;
        try(BlockWriter bw = new FileBlockWriter()){
            TermDocIdPair posting = new TermDocIdPair("hashmap", -12345, true);
            bw.writePosting(posting);
            filePath = bw.filePath();
        }

        try(BlockReader br = new FileBlockReader(filePath, log)){
            List<TermDocIdPair> postings = new ArrayList<>();
            br.iterator().forEachRemaining(p -> postings.add(p));
            assertThat(postings, is(Arrays.asList(new TermDocIdPair("hashmap", -12345, true))));
            assertThat(postings.get(0).termAppearsInDocTitle(), is(true));
        }
    }

    @Test
    public void multiplePostings() throws Exception {
        List<TermDocIdPair> postings = Arrays.asList(
            new TermDocIdPair("computeifabsent", 0, true),
            new TermDocIdPair("hashmap", 12345, true),
            new TermDocIdPair("treeset", -43252352, false),
            new TermDocIdPair("treeset", -43252352, false),
            new TermDocIdPair("treeset", -43252352, true),
            new TermDocIdPair("concurrentmodificationexception", 2147483647, true),
            new TermDocIdPair("filenotfoundexception", -2147483648, false),
            new TermDocIdPair("filenotfoundexception", 831188330, false),
            new TermDocIdPair("filenotfoundexception", 1787004475, true),
            new TermDocIdPair("filenotfoundexception", -787070170, true)
        );

        String filePath;
        try(BlockWriter bw = new FileBlockWriter()){
            for(TermDocIdPair posting : postings){
                bw.writePosting(posting);
            }
            filePath = bw.filePath();
        }

        try(BlockReader br = new FileBlockReader(filePath, log)){
            List<TermDocIdPair> actualPostings = new ArrayList<>();
            br.iterator().forEachRemaining(p -> actualPostings.add(p));
            assertThat(actualPostings, is(postings));

            assertThat(actualPostings.get(0), is(true));
            assertThat(actualPostings.get(1), is(true));

            assertThat(actualPostings.get(2), is(false));
            assertThat(actualPostings.get(3), is(false));

            assertThat(actualPostings.get(4), is(true));
            assertThat(actualPostings.get(5), is(true));

            assertThat(actualPostings.get(6), is(false));
            assertThat(actualPostings.get(7), is(false));

            assertThat(actualPostings.get(8), is(true));
            assertThat(actualPostings.get(9), is(true));
        }
    }
}