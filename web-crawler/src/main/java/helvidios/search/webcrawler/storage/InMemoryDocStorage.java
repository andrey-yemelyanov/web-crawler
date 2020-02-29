package helvidios.search.webcrawler.storage;

import java.util.concurrent.*;
import helvidios.search.webcrawler.HtmlDocument;

public class InMemoryDocStorage implements DocumentRepository {

    private final ConcurrentMap<String, HtmlDocument> docs = new ConcurrentHashMap<>();

    @Override
    public void save(HtmlDocument doc) {
        docs.put(doc.getId(), doc);
    }

    @Override
    public boolean contains(String docId) {
        return docs.containsKey(docId);
    }

    @Override
    public HtmlDocument get(String docId) {
        return docs.get(docId);
    }

    @Override
    public void delete(String docId) {
        docs.remove(docId);
    }

    @Override
    public int size() {
        return docs.size();
    }

    @Override
    public void clear() {
        docs.clear();
    }
}