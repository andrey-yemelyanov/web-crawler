package helvidios.search.tokenizer;

import java.util.*;

/**
 * Breaks text content into tokens based on a set of rules.
 * A token is an instance of a sequence of characters in some particular
 * document that are grouped together as a useful semantic unit for processing.
 */
public interface Tokenizer {

    /**
     * Returns a list of tokens in the specified text document.
     * @param content text content
     */
    List<String> getTokens(String content);
}