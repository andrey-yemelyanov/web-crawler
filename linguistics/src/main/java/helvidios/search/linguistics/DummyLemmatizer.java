package helvidios.search.linguistics;

import java.util.List;

public class DummyLemmatizer implements Lemmatizer {

    @Override
    public List<String> getLemmas(List<String> tokens) {
        return tokens;
    }

}