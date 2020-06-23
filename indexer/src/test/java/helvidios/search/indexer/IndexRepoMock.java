package helvidios.search.indexer;

import java.util.*;
import helvidios.search.index.Posting;
import helvidios.search.index.Term;
import helvidios.search.index.storage.IndexRepository;

public class IndexRepoMock implements IndexRepository {

    List<Term> terms  = new ArrayList<>();
    List<List<Posting>> postings = new ArrayList<>();

    @Override
    public void addTerm(Term term, List<Posting> postingsList) {
        terms.add(term);
        postings.add(postingsList);
    }

    @Override
    public List<Term> getVocabulary() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Posting> getPostingsList(Term term) {
        // TODO Auto-generated method stub
        return null;
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
    public double documentVectorMagnitude(int docId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void addDocumentVectorMagnitude(int docId, double magnitude) {
        // TODO Auto-generated method stub

    }
    
}