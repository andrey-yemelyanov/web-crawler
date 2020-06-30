package helvidios.search.index;

import java.util.Objects;

/**
 * Represents a non-positional index posting for a certain term.
 */
public class Posting implements Comparable<Posting> {
    private final int docId;
    private final int tf;
    private final Term term;

    /**
     * Initializes a new instance of {@link Posting}.
     * @param term term
     * @param docId document ID where the specified term appears
     * @param tf number of times the specified term appears in the document
     */
    public Posting(Term term, int docId, int tf){
        this.term = term;
        this.docId = docId;
        this.tf = tf;
    }

    /**
     * Returns document ID where this posting appears.
     */
    public int docId(){
        return docId;
    }

    /**
     * Returns number of times this term occurs in the associated document.
     */
    public int tf(){
        return tf;
    }

    /**
     * Returns a term associated with this posting.
     */
    public Term term(){
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
        return this.term.equals(other.term) && this.docId == other.docId && this.tf == other.tf;
    }

    @Override
    public int hashCode(){
        return Objects.hash(term, docId, tf);
    }

    @Override
    public String toString(){
        return String.format("(%s, docId=%d, tf=%d)", term.toString(), docId, tf);
    }
}