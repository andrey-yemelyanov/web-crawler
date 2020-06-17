package helvidios.search.index.storage;

import helvidios.search.index.Posting;
import helvidios.search.index.Term;
import java.util.*;

/**
 * Represents persistent storage for the index.
 */
public interface IndexRepository {

    /**
     * Adds a term along with its corresponding postings list to the repository.
     * If a term with the same name already exists, it is overwritten.
     */
    public void addTerm(Term term, List<Posting> postingsList);

    /**
     * Returns a list of all the terms in the index.
     */
    public List<Term> getVocabulary();

    /**
     * Returns a postings list for a given term.
     */
    public List<Posting> getPostingsList(Term term);

    /**
     * Clears this index storage.
     */
    public void clear();

    /**
     * Returns vocabulary size of this index.
     */
    public long size();
}