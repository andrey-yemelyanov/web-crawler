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
    private final long nDocs;
    private final Map<Integer, Integer> docLen = new HashMap<>();

    Indexer(IndexRepository indexRepo, BlockReader blockReader, Logger log, long nDocumentsInCorpus){
        this.indexRepo = indexRepo;
        this.blockReader = blockReader;
        this.log = log;
        this.nDocs = nDocumentsInCorpus;
    }

    void buildIndex(){
        Iterator<TermDocIdPair> it = blockReader.iterator();
        if(!it.hasNext()) return;
        TermDocIdPair currentPosting = it.next();
        Term currentTerm = new Term(currentPosting.term());
        int freq = 1;
        List<Posting> postings = new ArrayList<>();

        log.info("Indexing started.");
        
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
                writePostingsList(currentTerm, postings);
                postings = new ArrayList<>();
                currentPosting = posting;
                currentTerm = new Term(currentPosting.term());
                freq = 1;
            }
        }

        postings.add(new Posting(currentTerm, currentPosting.docId(), freq));
        writePostingsList(currentTerm, postings);
        
        writeDocumentLength();

        log.info("Index successfully built! Size = {} terms", indexRepo.size());
    }

    private void writeDocumentLength(){
        for(int docId : docLen.keySet()){
            indexRepo.setDocumentLength(docId, docLen.get(docId));
        }
    }

    private void writePostingsList(Term term, List<Posting> postingsList){
        final int df = postingsList.size();
        term = new Term(term.name(), df, computeIdf(df));

        log.info("Writing '{}' to index,  postings size = {}...", term.toString(), postingsList.size());
        indexRepo.addTerm(term, postingsList);
        log.info("Successfully written '{}' to index.", term.toString());

        // accumulate doc length from each posting
        for(Posting posting : postingsList){
            final int docId = posting.docId();
            docLen.put(docId, docLen.getOrDefault(docId, 0) + posting.tf());
        }
    }

    private double computeIdf(int df){
        return Math.log10(((double) nDocs) / df);
    }
}