package helvidios.search.index;

/**
 * Represents a normalized word in the index.
 */
public class Term {
    private final String name;
    private final int df;
    private final double idf;

    /**
     * Initializes a new instance of {@link Term}.
     * @param name String representation of the term
     * @param df document frequency of this term
     * @param idf inverse document frequency of this term
     */
    public Term(String name, int df, double idf){
        this.name = name;
        this.df = df;
        this.idf = idf;
    }

    /**
     * Initializes a new instance of {@link Term}.
     * @param name String representation of the term
     */
    public Term(String name){
        this(name, 0, 0.0);
    }

    /**
     * Returns inverse document frequency of this term.
     */
    public double idf(){
        return idf;
    }

    /**
     * Returns the document frequency of this term - number of documents where this term appears.
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
        return String.format("<term(%s) df=%d idf=%f>", name, df, idf);
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