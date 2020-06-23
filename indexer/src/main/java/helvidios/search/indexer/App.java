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
    private static final Logger log = LogManager.getLogger(App.class.getName());

    public static void main( String[] args ) throws Exception
    {
        final String dbName = "javadocs-search-db";

        DocumentRepository docRepo = new MongoDbDocumentRepository.Builder()
                                                                  .setDatabase(dbName)
                                                                  .build();
        
        IndexRepository indexRepo = new MongoDbIndexRepository.Builder()
                                                              .setDatabase(dbName)
                                                              .build();
        indexRepo.clear();

        Tokenizer tokenizer = new HtmlTokenizer();
        try(ApacheNlpLemmatizer lemmatizer = new ApacheNlpLemmatizer()){
            try(IndexBuilder indexBuilder = new IndexBuilder(docRepo, indexRepo, tokenizer, lemmatizer, log)){
                indexBuilder.build();
            }
        }
    }
}
