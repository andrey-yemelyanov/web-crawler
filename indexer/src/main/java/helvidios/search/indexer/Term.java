package helvidios.search.indexer;

public class Term {
    private final String term;
    private final int docId;
    private final int termId;
    private final int freq;

    public Term(String term, int docId, int freq){
        this.term = term;
        this.docId = docId;
        this.freq = freq;
        this.termId = term.hashCode();
    }

    public String getTerm(){
        return term;
    }

    public int getDocId(){
        return docId;
    }

    public int getTermId(){
        return termId;
    }

    public int getFreq(){
        return freq;
    }

    @Override
    public String toString(){
        return String.format("(%s->docId=%d|freq=%d)", getTerm(), getDocId(), getFreq());
    }
}