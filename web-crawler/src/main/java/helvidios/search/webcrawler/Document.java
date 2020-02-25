package helvidios.search.webcrawler;

import java.util.*;

/**
 * Represents a downloaded document.
 */
public class Document {
    private String url;
    private String content;

    /**
     * Returns URL of this document.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns downloaded HTML content of this document.
     */
    public String getContent() {
        return content;
    }

    /**
     * Initializes a new instance of {@link Document}.
     * @param url document URL
     * @param content document HTML content
     */
    public Document(String url, String content){
        this.url = url;
        this.content = content;
    }

    /**
     * Returns a list of urls in this document. These urls will have the same base url as the document url.
     */
    public List<String> getUrls(){
        return new ArrayList<>();
    }
}