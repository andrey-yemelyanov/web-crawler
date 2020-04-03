package helvidios.search.storage;

import java.util.Iterator;
import java.util.concurrent.*;

/**
 * Fast in-memory based implementation of {@link DocumentRepository}.
 */
public class InMemoryDocumentRepository implements DocumentRepository {

    private final ConcurrentMap<Integer, HtmlDocument> docs = new ConcurrentHashMap<>();

    public void insert(HtmlDocument doc) {
        docs.put(doc.getId(), doc);
    }

    public HtmlDocument get(int id) {
        if(!docs.containsKey(id)) return null;
        return docs.get(id);
    }

    public boolean contains(String url) {
        return docs.containsKey(url.hashCode());
    }

    public void clear() {
        docs.clear();
    }

    public Iterator<HtmlDocument> iterator() {
        return docs.values().iterator();
    }

    public long size() {
        return docs.size();
    }

    @Override
    public HtmlDocument get(String url) {
        int docId = new HtmlDocument(url, "").getId();
        return get(docId);
    }
}