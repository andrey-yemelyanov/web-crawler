package helvidios.search.index;

/**
 * Represents a normalized word in the index.
 */
public class Term {
    private final String term;
    private final int termId;

    /**
     * Initializes a new instance of {@link Term}.
     * @param term String representation of the term
     */
    public Term(String term){
        this.term = term;
        this.termId = term.hashCode();
    }

    /**
     * Returns string representation of the term.
     */
    public String getTerm(){
        return term;
    }

    /**
     * Returns unique term identifier.
     */
    public int getTermId(){
        return termId;
    }

    @Override
    public String toString(){
        return String.format("(%d)%s", getTermId(), getTerm());
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Term)) return false;
        Term other = (Term) obj;
        return this.termId == other.termId;
    }

    @Override
    public int hashCode(){
        return termId;
    }
}