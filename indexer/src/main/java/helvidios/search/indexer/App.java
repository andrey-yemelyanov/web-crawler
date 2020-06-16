package helvidios.search.indexer;

import helvidios.search.linguistics.ApacheNlpLemmatizer;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.MongoDbDocumentRepository;
import helvidios.search.tokenizer.HtmlTokenizer;
import helvidios.search.tokenizer.Tokenizer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        DocumentRepository docRepo = new MongoDbDocumentRepository.Builder().build();
        Tokenizer tokenizer = new HtmlTokenizer();
        
        try(ApacheNlpLemmatizer lemmatizer = new ApacheNlpLemmatizer()){
            try(Indexer indexer = new Indexer(docRepo, tokenizer, lemmatizer)){
                long startTime = System.currentTimeMillis();

                indexer.buildIndex();

                long endTime = System.currentTimeMillis();
                long timeElapsed = endTime - startTime;
                System.out.println("Indexing time: " + format(timeElapsed / 1000));
            }
        }
    }

    private static String format(long totalSeconds){
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
