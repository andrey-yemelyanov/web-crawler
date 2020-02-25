package helvidios.search.webcrawler;

/**
 * Represents document storage.
 */
public interface DocumentDb{

    /**
     * Saves the document in storage.
     * @param doc document
     * @return the id of the saved document
     */
    public String save(Document doc);

    /**
     * Removes the document from storage.
     * @param docId document id
     */
    public void delete(String docId);

    /**
     * Retrieves document from storage. 
     * @param docId document id
     */
    public Document get(String docId);
}