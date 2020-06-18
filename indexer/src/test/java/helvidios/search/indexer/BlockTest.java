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
            TermDocIdPair posting = new TermDocIdPair("hashmap", -12345);
            bw.writePosting(posting);
            filePath = bw.filePath();
        }

        try(BlockReader br = new FileBlockReader(filePath, log)){
            List<TermDocIdPair> postings = new ArrayList<>();
            br.iterator().forEachRemaining(p -> postings.add(p));
            assertThat(postings, is(Arrays.asList(new TermDocIdPair("hashmap", -12345))));
        }
    }

    @Test
    public void multiplePostings() throws Exception {
        List<TermDocIdPair> postings = Arrays.asList(
            new TermDocIdPair("computeifabsent", 0),
            new TermDocIdPair("hashmap", 12345),
            new TermDocIdPair("treeset", -43252352),
            new TermDocIdPair("treeset", -43252352),
            new TermDocIdPair("treeset", -43252352),
            new TermDocIdPair("concurrentmodificationexception", 2147483647),
            new TermDocIdPair("filenotfoundexception", -2147483648),
            new TermDocIdPair("filenotfoundexception", 831188330),
            new TermDocIdPair("filenotfoundexception", 1787004475),
            new TermDocIdPair("filenotfoundexception", -787070170)
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
        }
    }
}