package helvidios.search.indexer;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.util.*;

public class ExternalSortTest {

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
                new TermDocIdPair("hash", 1, false),
                new TermDocIdPair("hash", 5, false),
                new TermDocIdPair("tree", 1, false),
                new TermDocIdPair("tree", 5, false)
            )),
            new BlockReaderMock(Arrays.asList(
                new TermDocIdPair("attribute", 2, false),
                new TermDocIdPair("hash", 10, false),
                new TermDocIdPair("tree", 1, false),
                new TermDocIdPair("tree", 2, false),
                new TermDocIdPair("unicode", 1, false),
                new TermDocIdPair("zip", 4, false)
            ))
        );

        BlockWriterMock writer = new BlockWriterMock();
        ExternalSort sorter = new ExternalSort(readers, writer);
        String mergedFile = sorter.sort();
        assertThat(mergedFile, is("filePath1"));

        assertThat(writer.postings, is(Arrays.asList(
            new TermDocIdPair("attribute", 2, false),
            new TermDocIdPair("hash", 1, false),
            new TermDocIdPair("hash", 5, false),
            new TermDocIdPair("hash", 10, false),
            new TermDocIdPair("tree", 1, false),
            new TermDocIdPair("tree", 1, false),
            new TermDocIdPair("tree", 2, false),
            new TermDocIdPair("tree", 5, false),
            new TermDocIdPair("unicode", 1, false),
            new TermDocIdPair("zip", 4, false)
        )));
    }
}