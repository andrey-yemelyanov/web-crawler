package helvidios.search.searcher;

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

/**
 * Hello world!
 *
 */
public class App {

    private static final Logger log = LogManager.getLogger(Searcher.class.getName());
    private static final int K = 5;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception
    {
        while(true){
            
            DocumentRepository docRepo = new MongoDbDocumentRepository.Builder().build();
            IndexRepository indexRepo = new MongoDbIndexRepository.Builder().build();
            Tokenizer tokenizer = new HtmlTokenizer();
            Lemmatizer lemmatizer = new ApacheNlpLemmatizer();
            
            Searcher searcher = new Searcher(
                indexRepo, 
                docRepo, 
                tokenizer, 
                lemmatizer, 
                log);
            
            String query = getQuery();
            if(query.equals("Q")) break;
            List<Match> matches = searcher.search(query, K);

            System.out.printf("\nFound %d documents for '%s'\n", matches.size(), query);
            printMatches(matches);
        }
    }

    private static void printMatches(List<Match> matches){
        System.out.println("**********************************************************************");
        for(Match match : matches){
            System.out.println(match.toString());
            System.out.println();
        }
    }

    private static String getQuery(){
        System.out.print("\nEnter query (enter Q to exit):");
        return scanner.nextLine().trim();
    }
}
