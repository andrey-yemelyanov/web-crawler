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
        long startTime = System.nanoTime();

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
                index.putIfAbsent(term, new TreeSet<Integer>());
                index.get(term).add(doc.getId());
            }
            System.out.printf("Indexed doc %s\n", doc);
        }

        for(String term : index.keySet()){
            System.out.println(term);
        }

        System.out.printf("Done! Index size: %d\n", index.size());

        long endTime = System.nanoTime();
        long durationInNano = (endTime - startTime);
        System.out.println("Indexing time: " + TimeUnit.SECONDS.toSeconds(durationInNano) + " seconds.");

        lemmatizer.close();
    }
}
