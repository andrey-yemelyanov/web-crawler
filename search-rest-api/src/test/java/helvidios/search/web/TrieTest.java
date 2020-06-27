package helvidios.search.web;

import helvidios.search.web.util.Trie;
import helvidios.search.web.util.Vocabulary;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TrieTest {
    
    @Test
    public void populateTrie(){
        Vocabulary v = new Trie();
        
        v.add("a");
        v.add("abc");
        v.add("abcd");
        v.add("frodo");

        assertThat(v.size()).isEqualTo(4);

        assertThat(v.containsPrefix("a")).isTrue();
        assertThat(v.containsPrefix("ab")).isTrue();
        assertThat(v.containsPrefix("abc")).isTrue();
        assertThat(v.containsPrefix("abcd")).isTrue();
        assertThat(v.containsPrefix("abk")).isFalse();
        assertThat(v.containsPrefix("ad")).isFalse();
        assertThat(v.containsPrefix("z")).isFalse();

        assertThat(v.containsWord("a")).isTrue();
        assertThat(v.containsWord("ab")).isFalse();
        assertThat(v.containsWord("abe")).isFalse();
        assertThat(v.containsWord("abc")).isTrue();
        assertThat(v.containsWord("abcd")).isTrue();
        assertThat(v.containsWord("abcde")).isFalse();
        assertThat(v.containsWord("frodo")).isTrue();

        assertThat(v.getByPrefix("a")).isEqualTo(Arrays.asList(
            "a", "abc", "abcd"
        ));

        assertThat(v.getByPrefix("ab")).isEqualTo(Arrays.asList(
            "abc", "abcd"
        ));

        assertThat(v.getByPrefix("abc")).isEqualTo(Arrays.asList(
            "abc", "abcd"
        ));

        assertThat(v.getByPrefix("abcd")).isEqualTo(Arrays.asList(
            "abcd"
        ));

        assertThat(v.getByPrefix("fr")).isEqualTo(Arrays.asList(
            "frodo"
        ));

        assertThat(v.getByPrefix("abk")).isEqualTo(Arrays.asList(
            
        ));
    }

}