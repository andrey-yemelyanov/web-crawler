package helvidios.search.index;

import helvidios.search.index.storage.IndexRepository;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.logging.log4j.Logger;

/**
 * Represents document index.
 */
public class Index {
    
    private final IndexRepository indexRepo;
    private final Logger log;

    /**
     * Initializes a new instance of the index.
     * @param indexRepo {@link IndexRepository} where the index is stored
     * @param log logging component
     */
    public Index(IndexRepository indexRepo, Logger log){
        this.indexRepo = indexRepo;
        this.log = log;
    }

    /**
     * Returns number of unique terms in the vocabulary of this index.
     */
    public long size(){
        return indexRepo.size();
    }

    /**
     * Returns a list of words in the vocabulary of this index.
     */
    public List<String> vocabulary(){
        return indexRepo.getVocabulary()
                        .stream()
                        .map(term -> term.name())
                        .collect(Collectors.toList());
    }

    /**
     * Returns a postings list for a specific term. If the specified term does not
     * exist in the index, an empty list is returned.
     * @param term term
     */
    public List<Posting> postingsList(String term){
        log.info("Index lookup: Get postings list for term '{}'", term);
        List<Posting> postingsList = indexRepo.getPostingsList(new Term(term));
        log.info("Index lookup: Retrieved postings list for term '{}'. Postings list size={}.", term, postingsList.size());
        return postingsList;
    }

    /**
     * Returns number of words that appear in a document.
     * @param docId document ID
     */
    public long getDocumentLength(int docId){
        return indexRepo.getDocumentLength(docId);
    }
}