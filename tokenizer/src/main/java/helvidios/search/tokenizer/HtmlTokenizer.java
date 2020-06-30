package helvidios.search.tokenizer;

import java.util.*;
import org.jsoup.Jsoup;

public class HtmlTokenizer extends TextTokenizer {

    @Override
    public List<String> getTokens(String html){
        return super.getTokens(getText(html));
    }

    /**
     * Extracts raw text from an HTML page.
     * @param html source document in HTML format
     */
    public String getText(String html){
        return Jsoup.parse(html).select("body").text(); 
    }
}