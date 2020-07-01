package helvidios.search.searcher;

import org.junit.Test;
import helvidios.search.tokenizer.HtmlTokenizer;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TextHighlighterTest {

    @Test
    public void getTextSnippet() {
        Tokenizer tokenizer = new HtmlTokenizer();
        String html = "<html><body><h1>HashMap Description</h1><p>HashMap is a useful data structure.</p><p>It implements a dictionary Abstraction.</p></body></html>";
        String text = tokenizer.getText(html);
        
        TextHighlighter highlighter = new TextHighlighter(text);

        List<String> query = Arrays.asList("hashmap", "entry", "abstraction");
        
        TextSnippet snippet = highlighter.generateSnippet(query);
        
        assertThat(snippet.text(), is("...HashMap Description HashMap is a useful data structure. It implements a dictionary Abstraction..."));
        Map<String, List<Integer>> expectedOffsets = new HashMap<>();
        expectedOffsets.put("hashmap", Arrays.asList(3, 23));
        expectedOffsets.put("abstraction", Arrays.asList(86));
        assertThat(snippet.offsets(), is(expectedOffsets));
    }
}