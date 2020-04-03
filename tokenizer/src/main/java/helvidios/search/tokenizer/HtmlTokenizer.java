package helvidios.search.tokenizer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;

public class HtmlTokenizer {
    public static Map<String, Long> getTokens(String html){
        String htmlText = Jsoup.parse(html).select("body").text();
        return Arrays.asList(htmlText.split("[^a-zA-Z0-9']+")) // split HTML text on non-alphanumeric chars
                     .stream()
                     .filter(token -> token != null && token.length() > 0) // remove empty tokens
                     .map(token -> token.toLowerCase()) // convert token to lowercase
                     .filter(token -> !Stopwords.contains(token)) // remove stopwords
                     .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}