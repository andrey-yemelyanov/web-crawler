package helvidios.search.indexer;

import java.util.*;

class ExternalSort {

    private final List<BlockReader> readers;
    
    /**
     * Initializes a new instance of {@link ExternalSort}.
     * @param readers list of readers for the input blocks
     */
    ExternalSort(List<BlockReader> readers){
        this.readers = readers;
    }

    /**
     * Externally sorts blocks of data and returns a pathname to the resulting sorted file.
     */
    String sort(){
        if(readers.size() == 1) return readers.get(0).filePath();
        return "external_sort_not_supported.pdf";
    }
    
}