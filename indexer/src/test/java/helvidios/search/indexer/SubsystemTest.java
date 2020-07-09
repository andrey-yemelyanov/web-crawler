package helvidios.search.indexer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.junit.Test;
import helvidios.search.index.storage.IndexRepository;
import helvidios.search.index.storage.MongoDbIndexRepository;
import helvidios.search.linguistics.ApacheNlpLemmatizer;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.HtmlTokenizer;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;

public class SubsystemTest {
    
    private static final Logger log = LogManager.getLogger(SubsystemTest.class.getName());

    @Test
    //@Ignore
    public void populateDocRepo() throws Exception {

        final boolean compressDocs = true;
        final String dbName = "wiki-compressed-doc-db";

        final DocumentRepository mongoDbDocRepo = new MongoDbDocumentRepository.Builder()
                                                                               .setDatabase(dbName)
                                                                               .build();

        DocumentRepository docRepo = mongoDbDocRepo;

        if(compressDocs){
            docRepo = new CompressedDocumentRepository(mongoDbDocRepo, log);
        }

        docRepo.clear();

        List<String> urls = Arrays.asList(
            //"https://en.wikipedia.org/wiki/Wikipedia",
            //"https://en.wikipedia.org/wiki/List_of_compositions_by_Franz_Schubert",
            "https://en.wikipedia.org/wiki/List_of_Private_Passions_episodes"
        );

        for(String url : urls){
            String htmlContent = Jsoup.connect(url).get().html();
            String title = Jsoup.parse(htmlContent).title().trim();
            System.out.printf("Downloaded '%s' from %s\n", title, url);
            HtmlDocument doc = new HtmlDocument(url, htmlContent, title);
            docRepo.insert(doc);
        }

        System.out.println("DocRepo populated. Downloaded " + docRepo.size() + " documents.");

        IndexRepository indexRepo = new MongoDbIndexRepository.Builder().setDatabase(dbName).build();
        indexRepo.clear();

        System.out.printf("Indexing started.\nIndex repo: %s\nDoc repo: %s\n", indexRepo.toString(), docRepo.toString());

        Tokenizer tokenizer = new HtmlTokenizer();
        try(ApacheNlpLemmatizer lemmatizer = new ApacheNlpLemmatizer()){
            try(IndexBuilder indexBuilder = new IndexBuilder(docRepo, indexRepo, tokenizer, lemmatizer, log)){
                indexBuilder.build();
            }
        }

        System.out.println("Indexing completed!");
    }
}