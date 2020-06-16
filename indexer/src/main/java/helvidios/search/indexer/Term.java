package helvidios.search.indexer;

public class Term {
    private final String term;
    private final int docId;

    public Term(String term, int docId){
        this.term = term;
        this.docId = docId;
    }

    public String getTerm(){
        return term;
    }

    public int getDocId(){
        return docId;
    }
}