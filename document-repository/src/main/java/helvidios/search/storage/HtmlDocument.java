package helvidios.search.storage;

/**
 * Represents an HTML document.
 */
public class HtmlDocument {

    private final DocId docId;
    private final String url;
    private final String content;
    private final String title;

    /**
     * Initializes a new instance of {@link HtmlDocument}.
     * 
     * @param url     URL of the document
     * @param content HTML content of the document
     * @param title   Document title
     */
    public HtmlDocument(String url, String content, String title) {
        this.url = url;
        this.content = content;
        this.docId = new DocId(url);
        this.title = title;
    }

    /**
     * Returns human-readable title of this document.
     */
    public String getTitle(){
        return title;
    }

    /**
     * Returns the unique id of this document.
     */
    public int getId() {
        return docId.get();
    }

    /**
     * Returns the URL of this document.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the HTML content of this document.
     */
    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof HtmlDocument)) return false;
        HtmlDocument other = (HtmlDocument) obj;
        return this.docId.equals(other.docId);
    }

    @Override
    public int hashCode(){
        return docId.hashCode();
    }

    @Override
    public String toString(){
        return String.format("%s '%s' %s", docId, title, url);
    }
}