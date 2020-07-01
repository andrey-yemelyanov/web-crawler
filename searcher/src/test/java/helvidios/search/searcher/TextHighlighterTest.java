package helvidios.search.searcher;

import org.junit.Test;
import helvidios.search.tokenizer.HtmlTokenizer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import java.util.*;

public class TextHighlighterTest {

    @Test
    public void getTextFromHtml() throws IOException {
        HtmlTokenizer tokenizer = new HtmlTokenizer();
        String html = new String(Files.readAllBytes(Paths.get("C:\\Users\\A351509\\Downloads\\m.html")));
        String text = tokenizer.getText(html);
        //System.out.println(text);
        
        TextHighlighter highlighter = new TextHighlighter(text);

        List<String> query = Arrays.asList("map", "entry", "getkey");
        
        TextSnippet snippet = highlighter.generateSnippet(query);
        System.out.println(snippet);
        
    }

}