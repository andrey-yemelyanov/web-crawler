package helvidios.search.indexer;

import java.util.*;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.HtmlDocument;

public class DocRepoMock implements DocumentRepository {

    private final List<HtmlDocument> docs;

    public DocRepoMock(List<HtmlDocument> docs){
        this.docs = docs;
    }

    @Override
    public void insert(HtmlDocument doc) {
        docs.add(doc);
    }

    @Override
    public HtmlDocument get(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean contains(String url) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public HtmlDocument get(String url) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clear() {
        docs.clear();
    }

    @Override
    public Iterator<HtmlDocument> iterator() {
        return docs.iterator();
    }

    @Override
    public long size() {
        return docs.size();
    }
    
}