package helvidios.search.indexer;

import java.util.*;

class BlockReaderMock implements BlockReader {

    private final List<TermDocIdPair> postings;

    BlockReaderMock(List<TermDocIdPair> postings){
        this.postings = postings;
    }

    @Override
    public Iterator<TermDocIdPair> iterator() {
        return postings.iterator();
    }

    @Override
    public void close() throws Exception {}

    @Override
    public String filePath() {
        // TODO Auto-generated method stub
        return null;
    }

}