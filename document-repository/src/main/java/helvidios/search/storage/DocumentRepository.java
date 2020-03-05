package helvidios.search.storage;

import java.util.Iterator;

public interface DocumentRepository {
    public void insert(HtmlDocument doc);
    public HtmlDocument get(int id);
    boolean contains(String url);
    void clear();
    Iterator<HtmlDocument> iterator();
    long size();
}