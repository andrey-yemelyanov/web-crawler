package helvidios.search.index;

import java.util.Objects;

/**
 * Represents an index posting for a certain term.
 */
public class Posting implements Comparable<Posting> {
    private final int docId;
    private final int freq;
    private final Term term;

    /**
     * Initializes a new instance of {@link Posting}.
     * @param term term
     * @param docId document ID where the specified term appears
     * @param freq number of times the specified term appears in the document
     */
    public Posting(Term term, int docId, int freq){
        this.term = term;
        this.docId = docId;
        this.freq = freq;
    }

    /**
     * Returns document ID where the term appears.
     */
    public int getDocId(){
        return docId;
    }

    /**
     * Returns number of times this term occurs in the associated document.
     */
    public int getFreq(){
        return freq;
    }

    /**
     * Returns a term associated with this posting.
     */
    public Term getTerm(){
        return term;
    }

    @Override
    public int compareTo(Posting other) {
        return Integer.compare(this.docId, other.docId);
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Posting)) return false;
        Posting other = (Posting) obj;
        return this.term.equals(other.term) && this.docId == other.docId && this.freq == other.freq;
    }

    @Override
    public int hashCode(){
        return Objects.hash(term, docId, freq);
    }
}