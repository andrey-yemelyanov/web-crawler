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

    Indexer(IndexRepository indexRepo, BlockReader blockReader, Logger log, long nDocumentsInCorpus){
        this.indexRepo = indexRepo;
        this.blockReader = blockReader;
        this.log = log;
        this.nDocs = nDocumentsInCorpus;
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
                writePostingsList(currentTerm, postings);
                postings = new ArrayList<>();
                currentPosting = posting;
                currentTerm = new Term(currentPosting.term());
                freq = 1;
            }
        }

        postings.add(new Posting(currentTerm, currentPosting.docId(), freq));
        writePostingsList(currentTerm, postings);
        log.info("Index successfully built! Size = {} terms", indexRepo.size());
    }

    private void writePostingsList(Term term, List<Posting> postingsList){
        log.info("Writing '{}' to index,  postings size = {}...", term.toString(), postingsList.size());
        computeTfIdfScores(postingsList);
        indexRepo.addTerm(term, postingsList);
        log.info("Successfully written '{}' to index.", term.toString());
    }

    private void computeTfIdfScores(List<Posting> postingsList){
        final double idf = Math.log10(((double) nDocs) / postingsList.size());
        for(Posting posting : postingsList){
            final double tf = 1 + Math.log10(posting.tf());
            final double tfIdfScore = tf * idf;
            posting.setTfIdfScore(tfIdfScore);
        }
    }
}