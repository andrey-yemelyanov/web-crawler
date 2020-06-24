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
     * Returns a sorted postings list for a given term.
     */
    public List<Posting> getPostingsList(Term term);

    /**
     * Clears this index storage.
     */
    public void clear();

    /**
     * Returns number of unique terms in this index.
     */
    public long size();

    /**
     * Returns total number of words in a document. The words are not necessarily distinct.
     * @param docId document ID
     */
    public long getDocumentLength(int docId);

    /**
     * Stores number of words occurring in a document in the index.
     * @param docId document ID
     * @param len number of words occurring in the document
     */
    public void setDocumentLength(int docId, long len);
}