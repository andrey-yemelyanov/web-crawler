package helvidios.search.indexer;

public interface BlockReader extends Iterable<TermDocIdPair>, AutoCloseable {
    public String filePath();
}