package helvidios.search.indexer;

import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.util.*;

public class ExternalSortTest {

    @Mock
    Logger log;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private static class BlockWriterMock implements BlockWriter {

        @Override
        public void close() throws Exception {
            // TODO Auto-generated method stub

        }

        List<TermDocIdPair> postings = new ArrayList<>();
        @Override
        public void writePosting(TermDocIdPair posting) throws IOException {
            postings.add(posting);
        }

        @Override
        public String filePath() {
            return "filePath1";
        }

    }

    @Test
    public void sort() throws IOException {
        List<BlockReader> readers = Arrays.asList(
            new BlockReaderMock(Arrays.asList(
                new TermDocIdPair("hash", 1),
                new TermDocIdPair("hash", 5),
                new TermDocIdPair("tree", 1),
                new TermDocIdPair("tree", 5)
            )),
            new BlockReaderMock(Arrays.asList(
                new TermDocIdPair("attribute", 2),
                new TermDocIdPair("hash", 10),
                new TermDocIdPair("tree", 1),
                new TermDocIdPair("tree", 2),
                new TermDocIdPair("unicode", 1),
                new TermDocIdPair("zip", 4)
            ))
        );

        BlockWriterMock writer = new BlockWriterMock();
        ExternalSort sorter = new ExternalSort(readers, writer, log);
        String mergedFile = sorter.sort();
        assertThat(mergedFile, is("filePath1"));

        assertThat(writer.postings, is(Arrays.asList(
            new TermDocIdPair("attribute", 2),
            new TermDocIdPair("hash", 1),
            new TermDocIdPair("hash", 5),
            new TermDocIdPair("hash", 10),
            new TermDocIdPair("tree", 1),
            new TermDocIdPair("tree", 1),
            new TermDocIdPair("tree", 2),
            new TermDocIdPair("tree", 5),
            new TermDocIdPair("unicode", 1),
            new TermDocIdPair("zip", 4)
        )));
    }
}