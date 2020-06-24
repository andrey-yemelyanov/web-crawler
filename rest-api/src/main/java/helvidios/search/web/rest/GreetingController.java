package helvidios.search.web.rest;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import helvidios.search.index.storage.IndexRepository;
import helvidios.search.index.storage.MongoDbIndexRepository;
import helvidios.search.linguistics.ApacheNlpLemmatizer;
import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.MongoDbDocumentRepository;
import helvidios.search.tokenizer.HtmlTokenizer;
import helvidios.search.tokenizer.Tokenizer;
import java.util.stream.*;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
    
    @GetMapping("/greeting")
    public List<String> greeting(@RequestParam(value = "name", defaultValue = "World") String name){
        IndexRepository indexRepo = new MongoDbIndexRepository.Builder()
                                                              .setDatabase("javadocs-search-db")
                                                              .build();
        
        return indexRepo.getVocabulary().stream().map(term -> term.name()).collect(Collectors.toList());
    }
}