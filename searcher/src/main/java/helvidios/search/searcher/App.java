package helvidios.search.searcher;

import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import helvidios.search.index.storage.IndexRepository;
import helvidios.search.index.storage.MongoDbIndexRepository;
import helvidios.search.linguistics.ApacheNlpLemmatizer;
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
    private static final int K = 10;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        if (args.length == 0)
            throw new IllegalArgumentException(
                    "Command line argument for database name is required. Usage: run-searcher.bat [dbName]");

        final String dbName = args[0];

        System.out.println("Welcome to the SEARCH console!");

        DocumentRepository docRepo = new MongoDbDocumentRepository.Builder().setDatabase(dbName).build();

        IndexRepository indexRepo = new MongoDbIndexRepository.Builder().setDatabase(dbName).build();

        System.out.println(docRepo.toString());
        System.out.println(indexRepo.toString());

        Tokenizer tokenizer = new HtmlTokenizer();

        try (ApacheNlpLemmatizer lemmatizer = new ApacheNlpLemmatizer()) {
            Searcher searcher = new IndexSearcher(indexRepo, docRepo, tokenizer, lemmatizer, log);

            while (true) {

                String query = getQuery();
                if (query.equals("q:"))
                    break;
                List<Match> matches = searcher.search(query, K);

                System.out.printf("\nFound %d documents for query '%s'\n", matches.size(), query);
                printMatches(matches);
            }
        }
    }

    private static void printMatches(List<Match> matches) {
        System.out.println("**********************************************************************");
        int i = 1;
        for (Match match : matches) {
            System.out.print((i++) + ": ");
            System.out.println(match.toString());
            System.out.println();
        }
    }

    private static String getQuery() {
        System.out.print("\nEnter query (enter q: to exit):");
        return scanner.nextLine().trim();
    }
}
