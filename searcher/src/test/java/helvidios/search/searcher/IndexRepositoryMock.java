package helvidios.search.searcher;

import java.util.*;
import helvidios.search.index.Posting;
import helvidios.search.index.Term;
import helvidios.search.index.storage.IndexRepository;

public class IndexRepositoryMock implements IndexRepository {

    private final Map<Term, List<Posting>> index = new HashMap<>();

    @Override
    public void addTerm(Term term, List<Posting> postingsList) {
        index.put(term, postingsList);
    }

    @Override
    public List<Term> getVocabulary() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Posting> getPostingsList(Term term) {
        if(!index.containsKey(term)) return Arrays.asList();
        return index.get(term);
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public long size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getDocumentLength(int docId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setDocumentLength(int docId, long len) {
        // TODO Auto-generated method stub

    }
    
}