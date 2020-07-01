package helvidios.search.tokenizer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Breaks text into tokens.
 */
public class TextTokenizer implements Tokenizer {

    @Override
    public List<String> getTokens(String content) {
        return Arrays.asList(content.split("[^a-zA-Z0-9]+")) // split text on non-alphanumeric chars
                     .stream()
                     .filter(token -> token != null && token.length() > 0) // remove empty tokens
                     .map(token -> token.toLowerCase()) // convert token to lowercase
                     .filter(token -> !Stopwords.contains(token)) // remove stopwords
                     .collect(Collectors.toList());
    }

    @Override
    public String getText(String content) {
        return content;
    }

}