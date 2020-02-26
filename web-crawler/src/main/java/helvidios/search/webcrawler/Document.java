package helvidios.search.webcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
     * 
     * @param url     document URL
     * @param content document HTML content
     */
    public Document(String url, String content) {
        this.url = url;
        this.content = content;
    }

    /**
     * Returns a list of urls in this document. These urls will have the same base
     * url as the document url.
     * 
     * @throws MalformedURLException
     */
    public List<String> getUrls() throws MalformedURLException {
        String serverUrl = getServerUrl();
        
        org.jsoup.nodes.Document doc = Jsoup.parse(content, url);
        Elements links = doc.select("a[href]");
        Set<String> urls = new HashSet<>();
        for (Element link : links) {
            if (link.attr("abs:href").isEmpty())
                continue;
            URL absoluteUrl = new URL(link.attr("abs:href"));
            String discoveredUrl = new URL(
                absoluteUrl.getProtocol(), 
                absoluteUrl.getHost(), 
                absoluteUrl.getPort(),
                absoluteUrl.getPath())
            .toString();

            // only urls linking to this domain are interesting
            // do not include anchor links to the same page
            if (discoveredUrl.startsWith(serverUrl) && !discoveredUrl.equals(url)) {
                urls.add(discoveredUrl);
            }
        }

        return new ArrayList<>(urls);
    }

    private String getServerUrl() throws MalformedURLException {
        URL docUrl = new URL(url);
        return new URL(
            docUrl.getProtocol(), 
            docUrl.getHost(), 
            docUrl.getPort(), "")
        .toString();
    }
}