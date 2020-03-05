package helvidios.search.storage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents an HTML document.
 */
public class HtmlDocument {
    private final static AtomicInteger counter = new AtomicInteger();

    private int id;
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
        this.id = counter.getAndIncrement();
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
}