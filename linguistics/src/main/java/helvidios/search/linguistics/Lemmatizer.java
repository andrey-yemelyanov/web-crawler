package helvidios.search.linguistics;

import java.util.*;

public interface Lemmatizer {
    public List<String> getLemmas(List<String> tokens) throws Exception;
}