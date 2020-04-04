package helvidios.search.storage;

import java.util.concurrent.atomic.AtomicInteger;

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
        this.id = url.hashCode();
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
    public boolean equals(Object obj){
        if(!(obj instanceof HtmlDocument)) return false;
        HtmlDocument other = (HtmlDocument) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public String toString(){
        return String.format("[%d] %s", id, url);
    }
}