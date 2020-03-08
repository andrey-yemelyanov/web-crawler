package helvidios.search.storage;

/**
 * Represents an HTML document.
 */
public class HtmlDocument {

    private int id;
    private String url;
    private String content;

    /**
     * Initializes a new instance of {@link HtmlDocument}.
     * 
     * @param url     URL of the document
     * @param content HTML content of the document
     */
    public HtmlDocument(String url, String content) {
        this.url = url;
        this.content = content;
        this.id = urlToId(url);
    }

    /**
     * Returns the unique id of this document.
     */
    public int getId() {
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
        return this.id == other.id;
    }

    /**
     * Returns a unique integer identifier corresponding to a given URL.
     * @param url URL
     */
    public static int urlToId(String url){
        return url.hashCode();
    }
}