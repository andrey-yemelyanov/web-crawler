package helvidios.search.indexer;

import java.util.Objects;

public class TermDocIdPair implements Comparable<TermDocIdPair> {
    private final String term;
    private final int docId;
    private final boolean termAppearsInDocTitle;

    public TermDocIdPair(String term, int docId, boolean termAppearsInDocTitle) {
        this.term = term;
        this.docId = docId;
        this.termAppearsInDocTitle = termAppearsInDocTitle;
    }

    public TermDocIdPair(String term, int docId) {
        this(term, docId, false);
    }

    public boolean termAppearsInDocTitle() {
        return termAppearsInDocTitle;
    }

    public String term() {
        return term;
    }
    
    public int docId() { return docId; }

    @Override
    public int compareTo(TermDocIdPair other) {
        if(this.term().equals(other.term())) {
            return Integer.compare(this.docId(), other.docId());
        }
        return this.term().compareTo(other.term());
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof TermDocIdPair)) return false;
        return this.compareTo((TermDocIdPair) obj) == 0; 
    }

    @Override
    public int hashCode(){
        return Objects.hash(term, docId);
    }

    @Override
    public String toString(){
        return String.format("(term=%s, docId=%d)", term, docId);
    }
}