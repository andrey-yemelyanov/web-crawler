package helvidios.search.indexer;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.logging.log4j.*;
import helvidios.search.index.storage.*;
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
    private static final Logger LOGGER = LogManager.getLogger(Indexer.class.getName());

    public static void main( String[] args ) throws Exception
    {
        DocumentRepository docRepo = new MongoDbDocumentRepository.Builder().build();
        // docRepo.clear();
        // HtmlDocument doc1 = new HtmlDocument(
        //     "https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html",
        //     new String(Files.readAllBytes(Paths.get("Collections.html"))),
        //     "Java Collections");
        // HtmlDocument doc2 = new HtmlDocument(
        //     "https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html",
        //     new String(Files.readAllBytes(Paths.get("Stream.html"))),
        //     "Java Stream");
        // HtmlDocument doc3 = new HtmlDocument(
        //     "https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html",
        //     new String(Files.readAllBytes(Paths.get("Class.html"))),
        //     "Java Class");
        // docRepo.insert(doc1);
        // docRepo.insert(doc2);
        // docRepo.insert(doc3);

        IndexRepository indexRepo = new MongoDbIndexRepository.Builder().build();
        indexRepo.clear();

        Tokenizer tokenizer = new HtmlTokenizer();
        
        try(ApacheNlpLemmatizer lemmatizer = new ApacheNlpLemmatizer()){
            try(IndexBuilder indexer = new IndexBuilder(docRepo, indexRepo, tokenizer, lemmatizer, LOGGER)){
                long startTime = System.currentTimeMillis();
                indexer.build();
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
