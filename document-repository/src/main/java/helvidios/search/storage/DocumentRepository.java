package helvidios.search.storage;

import java.util.Iterator;

/**
 * Represents HTML document storage. For each document, its URL and full HTML content is stored.
 */
public interface DocumentRepository {

    /**
     * Adds an HTML document to the storage.
     * @param doc HTML document
     */
    public void insert(HtmlDocument doc);

    /**
     * Retrieves an HTML document with the given id.
     * @param id unique document identifier
     * @return HTML document or {@code null} if document not found
     */
    public HtmlDocument get(DocId id);

    /**
     * Checks if a document with the given URL is present in the storage.
     * @param url document URL
     * @return {@code true} if the document is present
     */
    boolean contains(String url);

    /**
     * Retrieves an HTML document with the given URL.
     * @param url document URL
     * @return HTML document or {@code null} if document not found
     */
    public HtmlDocument get(String url);

    /**
     * Removes all documents from the storage.
     */
    void clear();

    /**
     * Returns an {@link Iterator} object over all HTML documents in the storage.
     */
    Iterator<HtmlDocument> iterator();

    /**
     * Returns the total number of documents in the storage.
     */
    long size();
}