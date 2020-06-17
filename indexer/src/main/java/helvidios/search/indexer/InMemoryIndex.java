package helvidios.search.indexer;

import java.util.*;
import helvidios.search.index.Posting;
import helvidios.search.index.Term;

class InMemoryIndex {

    private final Map<Term, List<Posting>> index = new HashMap<>();

    void append(Map<Term, List<Posting>> subIndex){
        for(Term term : subIndex.keySet()){
            index.put(term, mergePostingsLists(
                index.computeIfAbsent(term, (k) -> new LinkedList<>()), 
                subIndex.get(term)));
        }
    }

    private List<Posting> mergePostingsLists(List<Posting> list1, List<Posting> list2){
        List<Posting> merged = new LinkedList<>();
        
        int i = 0, j = 0;
        while(i < list1.size() && j < list2.size()){
            if(list1.get(i).getDocId() < list2.get(j).getDocId()){
                merged.add(list1.get(i++));
            }else{
                merged.add(list2.get(j++));
            }
        }

        while(i < list1.size()){
            merged.add(list1.get(i++));
        }

        while(j < list2.size()){
            merged.add(list2.get(j++));
        }

        return merged;
    }

    List<Term> getVocabulary(){
        return new ArrayList<>(index.keySet());
    }

    List<Posting> getPostingsList(Term term){
        if(!index.containsKey(term)) return new ArrayList<>();
        return index.get(term);
    }

    long size(){
        return index.size();
    }

    @Override
    public String toString(){
        return index.toString();
    }
}