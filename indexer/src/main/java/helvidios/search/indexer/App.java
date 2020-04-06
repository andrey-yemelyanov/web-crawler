package helvidios.search.indexer;

import java.util.*;
import java.util.concurrent.TimeUnit;
import helvidios.search.linguistics.ApacheNlpLemmatizer;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.HtmlDocument;
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
        long startTime = System.currentTimeMillis();

        DocumentRepository docRepo = new MongoDbDocumentRepository.Builder().build();
        Tokenizer tokenizer = new HtmlTokenizer();
        ApacheNlpLemmatizer lemmatizer = new ApacheNlpLemmatizer();

        System.out.println("Indexing started...");

        Map<String, SortedSet<Integer>> index = new HashMap<>();
        Iterator<HtmlDocument> it = docRepo.iterator();
        while(it.hasNext()){
            HtmlDocument doc = it.next();
            List<String> tokens = tokenizer.getTokens(doc.getContent());
            for(String term : lemmatizer.getLemmas(tokens)){
                index.computeIfAbsent(term, key -> new TreeSet<>()).add(doc.getId());
            }
            System.out.printf("Indexed doc %s\n", doc);
        }

        for(String term : index.keySet()){
            System.out.println(term);
        }

        System.out.printf("Done! Index size: %d\n", index.size());

        long endTime = System.currentTimeMillis();
		long timeElapsed = endTime - startTime;
        System.out.println("Indexing time: " + format(timeElapsed / 1000));

        lemmatizer.close();
    }

    private static String format(long totalSeconds){
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
