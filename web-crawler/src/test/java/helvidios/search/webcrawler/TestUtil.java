package helvidios.search.webcrawler;

import java.io.*;
import java.net.URISyntaxException;

public class TestUtil {
    /**
     * Reads text file contents from supplied path.
     * 
     * @param path file path
     * @return file content as a string
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String readFile(String path) throws IOException, URISyntaxException {
        java.net.URL url = TestUtil.class.getResource(path);
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        return new String(java.nio.file.Files.readAllBytes(resPath), "UTF8"); 
    }
}