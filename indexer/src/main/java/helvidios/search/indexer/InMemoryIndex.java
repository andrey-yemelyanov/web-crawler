package helvidios.search.indexer;

import java.util.*;

class InMemoryIndex {

    private final Map<String, List<Term>> index = new HashMap<>();

    void append(Map<String, List<Term>> subIndex){
        for(String key : subIndex.keySet()){
            index.put(key, mergePostingsLists(
                index.computeIfAbsent(key, (k) -> new LinkedList<>()), 
                subIndex.get(key)));
        }
    }

    private List<Term> mergePostingsLists(List<Term> list1, List<Term> list2){
        List<Term> merged = new LinkedList<>();
        
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

    Map<String, List<Term>> getIndex(){
        return index;
    }

    @Override
    public String toString(){
        return index.toString();
    }
}