package helvidios.search.linguistics;

import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import java.util.*;

/**
 * Removes any changes in form of the word like tense, 
 * gender, mood, etc. and returns dictionary or base form of word.
 * E.g. cars -> car, threw -> throw
 */
public class ApacheNlpLemmatizer implements AutoCloseable, Lemmatizer {
    private final InputStream posModelInputStream;
    private final InputStream dictLemmatizerInputStream;
    private final DictionaryLemmatizer lemmatizer;
    private final POSTaggerME posTagger;

    /**
     * Initializes a new instance of {@link ApacheNlpLemmatizer}.
     * 
     * @throws IOException
     */
    public ApacheNlpLemmatizer() throws IOException {
        posModelInputStream = getFile("en-pos-maxent.bin");
        dictLemmatizerInputStream = getFile("en-lemmatizer.dict");

        POSModel posModel = new POSModel(posModelInputStream);
        // initializing the parts-of-speech tagger with model
        posTagger = new POSTaggerME(posModel);
        // loading the lemmatizer with dictionary
        lemmatizer = new DictionaryLemmatizer(dictLemmatizerInputStream);
    }

    private InputStream getFile(String path){
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }

    /**
     * Returns a normalized representation of supplied tokens in their dictionary form.
     * @param tokens tokens in English
     */
    public synchronized List<String> getLemmas(List<String> tokens){
        String[] tokensArray = tokens.toArray(new String[0]);
        String tags[] = posTagger.tag(tokensArray);
        String[] lemmas = lemmatizer.lemmatize(tokensArray, tags);
        List<String> lemmaList = new ArrayList<>();
        for(int i = 0; i < tokensArray.length;i++){
            if(lemmas[i].equals("O")) lemmaList.add(tokensArray[i]);
            else lemmaList.add(lemmas[i]);
        }
        return lemmaList;
    }

    @Override
    public void close() throws Exception {
        try{if(posModelInputStream != null) posModelInputStream.close();}catch(Exception ex){}
        if(dictLemmatizerInputStream != null) dictLemmatizerInputStream.close();
    }
}