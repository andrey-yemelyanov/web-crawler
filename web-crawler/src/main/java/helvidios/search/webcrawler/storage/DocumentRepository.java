package helvidios.search.webcrawler.storage;

import helvidios.search.webcrawler.HtmlDocument;

/**
 * Represents document storage.
 */
public interface DocumentRepository{

    /**
     * Saves a document in the storage.
     * @param doc HTML document
     */
    public void save(HtmlDocument doc);

    /**
     * Checks if a document with a given docId exists in the storage.
     * @param docId document id
     * @return true if a document with docId exists in the storage
     */
    public boolean contains(String docId);

    /**
     * Retrieves a document with docId from the storage.
     * @param docId document id
     * @return HTML document or {@code null} if no document with specified docId is present
     */
    public HtmlDocument get(String docId);

    /**
     * Removes a document with a given docId from the storage. If the document is not present, this operation has no effect.
     * @param docId document id
     */
    public void delete(String docId);

    /**
     * Returns the number of documents in the storage.
     */
    public int size();
}