package helvidios.search.indexer;

import java.util.*;
import org.apache.logging.log4j.Logger;
import helvidios.search.index.Posting;
import helvidios.search.index.Term;
import helvidios.search.index.storage.IndexRepository;

class Indexer {
    
    private final IndexRepository indexRepo;
    private final BlockReader blockReader;
    private final Logger log;

    Indexer(IndexRepository indexRepo, BlockReader blockReader, Logger log){
        this.indexRepo = indexRepo;
        this.blockReader = blockReader;
        this.log = log;
    }

    void buildIndex(){
        Iterator<TermDocIdPair> it = blockReader.iterator();
        TermDocIdPair currentPosting = it.next();
        Term currentTerm = new Term(currentPosting.term());
        int freq = 1;
        List<Posting> postings = new ArrayList<>();
        
        while(it.hasNext()){
            TermDocIdPair posting = it.next();
            if(posting.equals(currentPosting)){ // two equivalent postings (same term and docId), just increment the frequency counter
                freq++;
            }else if(posting.term().equals(currentPosting.term())){ // same term, but a different docId
                postings.add(new Posting(currentTerm, currentPosting.docId(), freq));
                currentPosting = posting;
                freq = 1;
            }else{  // postings with different terms and docIds
                postings.add(new Posting(currentTerm, currentPosting.docId(), freq));
                log.info("Writing '{}' to index,  postings size = {}...", currentTerm.toString(), postings.size());
                indexRepo.addTerm(currentTerm, postings);
                log.info("Successfully written '{}' to index.", currentTerm.toString());
                postings = new ArrayList<>();
                currentPosting = posting;
                currentTerm = new Term(currentPosting.term());
                freq = 1;
            }
        }

        postings.add(new Posting(currentTerm, currentPosting.docId(), freq));
        log.info("Writing '{}' to index,  postings size = {}...", currentTerm.toString(), postings.size());
        indexRepo.addTerm(currentTerm, postings);
        log.info("Successfully written '{}' to index.", currentTerm.toString());
        log.info("Index successfully built! Size = {} terms", indexRepo.size());
    }
}