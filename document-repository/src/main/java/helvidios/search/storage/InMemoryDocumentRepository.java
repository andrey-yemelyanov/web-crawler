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

    public HtmlDocument get(DocId id) {
        if(!docs.containsKey(id.get())) return null;
        return docs.get(id.get());
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

    public boolean contains(String url) {
        return get(url) != null;
    }

    public HtmlDocument get(String url) {
        return get(new DocId(url));
    }
}