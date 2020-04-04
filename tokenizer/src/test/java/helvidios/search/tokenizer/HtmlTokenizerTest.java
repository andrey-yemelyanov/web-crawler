package helvidios.search.tokenizer;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.*;

public class HtmlTokenizerTest {
    @Test
    public void tokenizeJavaDocsPages() throws Exception {
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

    @Test
    public void tokenizeHtmlPage(){
        String html = readFile("page1.html");
        Tokenizer tokenizer = new HtmlTokenizer();
        List<String> tokens = tokenizer.getTokens(html);
        assertThat(tokens, is(Arrays.asList(
            "link", "titles", "title", "attribute", 
            "specifies", "extra", "information", "element", 
            "information", "often", "shown", "tooltip", "text", 
            "mouse", "moves", "element", "visit", "html", "tutorial", 
            "html", "tutorial", "link1", "visit", "facebook", "tutorial"
        )));
        System.out.println(tokens);
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