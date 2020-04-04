package helvidios.search.tokenizer;

import java.util.*;
import org.jsoup.Jsoup;

public class HtmlTokenizer extends TextTokenizer {

    @Override
    public List<String> getTokens(String html){
        String text = Jsoup.parse(html).select("body").text(); // extract text from HTML page
        return super.getTokens(text);
    }
}