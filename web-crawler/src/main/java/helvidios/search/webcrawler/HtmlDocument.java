package helvidios.search.webcrawler;

/**
 * Represents an HTML document.
 */
public class HtmlDocument {
    private String id;
    private String url;
    private String content;

    /**
     * Initializes a new instance of {@link HtmlDocument}.
     * 
     * @param url     URL of the document
     * @param content HTML content of the document
     * @throws Exception
     */
    public HtmlDocument(String url, String content) throws Exception {
        this.url = url;
        this.content = content;
        this.id = Util.checksum(url);
    }

    /**
     * Returns the unique id of this document in the form of the URL checksum.
     */
    public String getId() {
        return id;
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
    public int hashCode(){
        return url.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof HtmlDocument)) return false;
        HtmlDocument other = (HtmlDocument) obj;
        return this.id.equals(other.id);
    }
}