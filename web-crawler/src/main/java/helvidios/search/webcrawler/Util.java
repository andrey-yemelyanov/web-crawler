package helvidios.search.webcrawler;

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
}