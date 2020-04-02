package helvidios.search.webcrawler;

import java.net.MalformedURLException;
import java.net.URL;

public class Util {
    /**
     * Removes all query parameters and achor tags after URL path.
     * @param url URL
     * @return normalized URL
     */
    public static String normalize(String url) {
        try {
            URL absoluteUrl = new URL(url);
            return new URL(
                absoluteUrl.getProtocol(), 
                absoluteUrl.getHost(), 
                absoluteUrl.getPort(),
                absoluteUrl.getPath())
            .toString();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Returns base URL for given URL. The base URL is in format {@code <protocol>://<host>:<port>}
     * @param url URL
     * @throws MalformedURLException
     */
    public static String getBaseUrl(String url) throws MalformedURLException {
        URL absoluteUrl = new URL(url);
            return new URL(
                absoluteUrl.getProtocol(), 
                absoluteUrl.getHost(), 
                absoluteUrl.getPort(),
                "")
            .toString();
    }
}