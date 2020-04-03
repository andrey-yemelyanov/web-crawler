package helvidios.search.tokenizer;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class StopwordsTest {
    @Test
    public void checkStopwords(){
        assertThat(Stopwords.contains("the"), is(true));
        assertThat(Stopwords.contains("these"), is(true));
        assertThat(Stopwords.contains("under"), is(true));
        assertThat(Stopwords.contains("java"), is(false));
        assertThat(Stopwords.contains("friend"), is(false));
    }
}