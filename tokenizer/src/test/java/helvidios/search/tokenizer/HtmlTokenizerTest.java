package helvidios.search.tokenizer;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.*;

public class HtmlTokenizerTest {
    @Test
    public void tokenizeHtmlPage() throws Exception {
        Arrays.asList(
            "Collectors.html",
            "DateFormat.html",
            "HashMap.html"
        ).forEach(doc -> {
            String html = readFile(doc);
            Tokenizer tokenizer = new HtmlTokenizer();
            List<String> tokens = tokenizer.getTokens(html);
            assertThat(tokens, is(not(empty())));
            //System.out.println(tokens);
        });
    }

    private String readFile(String path) {
        try{
            java.net.URL url = HtmlTokenizerTest.class.getResource(path);
            java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
            return new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
        }
        catch(Exception ex){
            return "";
        }
    }
}