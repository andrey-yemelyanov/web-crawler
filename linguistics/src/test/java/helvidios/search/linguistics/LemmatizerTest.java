package helvidios.search.linguistics;

import static org.junit.Assert.assertThat;
import java.util.*;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class LemmatizerTest {

    @Test
    public void getLemmas(){
        List<String> tokens = Arrays.asList(
            "thrown", 
            "putIfAbsent", 
            "written", 
            "operating", 
            "specifies", 
            "cars", 
            "performs", 
            "entries",
            "hashmap",
            "replaceall",
            "123",
            "concurrentmodificationexception",
            "externally",
            "proceeds",
            "removes"
        );

        Lemmatizer lemmatizer = new DummyLemmatizer();
        List<String> lemmas = lemmatizer.getLemmas(tokens);
        assertThat(lemmas, is(tokens));
    }

    @Test
    public void getLemmasApacheNlp() throws Exception {
        List<String> tokens = Arrays.asList(
            "thrown", 
            "putIfAbsent", 
            "written", 
            "operating", 
            "specifies", 
            "cars", 
            "performs", 
            "entries",
            "hashmap",
            "replaceall",
            "123",
            "concurrentmodificationexception",
            "externally",
            "proceeds",
            "removes"
        );

        try(ApacheNlpLemmatizer lemmatizer = new ApacheNlpLemmatizer()){
            List<String> lemmas = lemmatizer.getLemmas(tokens);
            assertThat(lemmas, is(Arrays.asList(
                "throw", 
                "putIfAbsent", 
                "write", 
                "operating", 
                "specify", 
                "car", 
                "performs", 
                "entry",
                "hashmap",
                "replaceall",
                "123",
                "concurrentmodificationexception",
                "externally",
                "proceed",
                "remove"
            )));
        }
    }
}