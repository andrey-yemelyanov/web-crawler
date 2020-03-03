package helvidios.search.webcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

public class Util {
    /**
     * Computes MD5 checksum/hash of an input String.
     * @param data input data in String format
     * @return hex MD5 hash in string format
     * @throws Exception
     */
    public static String checksum(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString().toUpperCase();
    }

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