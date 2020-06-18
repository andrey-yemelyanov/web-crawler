package helvidios.search.indexer;

import java.io.IOException;

public interface BlockWriter extends AutoCloseable {
    public void writePosting(TermDocIdPair posting) throws IOException;
    public String filePath();
}