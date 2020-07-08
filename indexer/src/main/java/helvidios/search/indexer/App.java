package helvidios.search.indexer;

import org.apache.logging.log4j.*;
import helvidios.search.index.storage.*;
import helvidios.search.linguistics.ApacheNlpLemmatizer;
import helvidios.search.storage.CompressedDocumentRepository;
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
    private static final Logger log = LogManager.getLogger(App.class.getName());

    public static void main( String[] args ) throws Exception
    {
        if(args.length == 0) throw new IllegalArgumentException(
            "Command line argument for database name is required. Usage: run-indexer.bat [dbName]");

        final String dbName = args[0];

        DocumentRepository docRepo = new CompressedDocumentRepository(
                                        new MongoDbDocumentRepository.Builder()
                                                                     .setDatabase(dbName)
                                                                     .build(), log);

        IndexRepository indexRepo = new MongoDbIndexRepository.Builder()
                                                              .setDatabase(dbName)
                                                              .build();
        indexRepo.clear();

        System.out.printf("Index repo: %s\nDoc repo: %s\n", indexRepo.toString(), docRepo.toString());
        System.out.println("Indexing started. Check logs for details...");
        log.info("Indexing started.\nIndex repo: {}\nDoc repo: {}", indexRepo.toString(), docRepo.toString());

        Tokenizer tokenizer = new HtmlTokenizer();
        try(ApacheNlpLemmatizer lemmatizer = new ApacheNlpLemmatizer()){
            try(IndexBuilder indexBuilder = new IndexBuilder(docRepo, indexRepo, tokenizer, lemmatizer, log)){
                indexBuilder.build();
            }
        }

        System.out.println("Indexing completed!");
    }
}
