package helvidios.search.index;

/**
 * Represents a normalized word in the index.
 */
public class Term {
    private final String name;
    private final int df;

    /**
     * Initializes a new instance of {@link Term}.
     * @param name String representation of the term
     * @param df document frequency of this term
     */
    public Term(String name, int df){
        this.name = name;
        this.df = df;
    }

    /**
     * Initializes a new instance of {@link Term}.
     * @param name String representation of the term
     */
    public Term(String name){
        this(name, 0);
    }

    /**
     * Returns the document frequency of this term.
     */
    public int df(){
        return df;
    }

    /**
     * Returns string representation of the term.
     */
    public String name(){
        return name;
    }


    @Override
    public String toString(){
        return String.format("term(%s)", name);
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Term)) return false;
        Term other = (Term) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }
}