package helvidios.search.web;

public class SearchException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public SearchException(Exception ex){
        super("Error while searching." + ex.getMessage());
    }
}