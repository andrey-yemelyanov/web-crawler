package helvidios.search.searcher;

import java.util.*;
import helvidios.search.storage.DocId;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.HtmlDocument;

public class DocRepoMock implements DocumentRepository {

    private final Map<Integer, HtmlDocument> map = new HashMap<>();

    @Override
    public void insert(HtmlDocument doc) {
        map.put(doc.getId(), doc);
    }

    @Override
    public HtmlDocument get(DocId id) {
        return map.get(id.get());
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
        // TODO Auto-generated method stub

    }

    @Override
    public Iterator<HtmlDocument> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long size() {
        // TODO Auto-generated method stub
        return 0;
    }
    
}