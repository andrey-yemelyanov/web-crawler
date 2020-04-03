package helvidios.search.tokenizer;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.*;

public class HtmlTokenizerTest {
    @Test
    public void tokenizeHtmlPage() throws Exception {
        String html = readFile("DateFormat.html");
        Map<String, Long> tokens = HtmlTokenizer.getTokens(html);
        System.out.println(tokens);
    }

    private String readFile(String path) throws Exception {
        java.net.URL url = HtmlTokenizerTest.class.getResource(path);
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        return new String(java.nio.file.Files.readAllBytes(resPath), "UTF8"); 
    }
}