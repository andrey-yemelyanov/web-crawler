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
    private final Map<Integer, Double> vectorMagnitude = new HashMap<>();

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
        writeVectorMagnitude();
        log.info("Index successfully built! Size = {} terms", indexRepo.size());
    }

    private void writePostingsList(Term term, List<Posting> postingsList){
        log.info("Writing '{}' to index,  postings size = {}...", term.toString(), postingsList.size());
        computeTfIdfScores(postingsList);
        indexRepo.addTerm(term, postingsList);
        log.info("Successfully written '{}' to index.", term.toString());

        accumulateVectorMagnitude(postingsList);
    }

    private void accumulateVectorMagnitude(List<Posting> postingsList){
        for(Posting posting : postingsList){
            final int docId = posting.docId();
            vectorMagnitude.put(
                docId,
                vectorMagnitude.getOrDefault(docId, 0.0) + Math.pow(posting.tfIdfScore(), 2)
            );
        }
    }

    private void writeVectorMagnitude(){
        // compute final vector magnitude for each docId and store it in the index
        for(int docId : vectorMagnitude.keySet()){
            indexRepo.addDocumentVectorMagnitude(docId, Math.sqrt(vectorMagnitude.get(docId)));
        }
    }

    private void computeTfIdfScores(List<Posting> postingsList){
        final double idf = Math.log10(((double) nDocs) / postingsList.size());
        for(Posting posting : postingsList){
            log.info("Computing tf-idf score for term {} in docId={}: tf={}, nDocs={}, df={}", 
                posting.term().name(), posting.docId(), posting.tf(), nDocs, postingsList.size());
            final double tf = 1 + Math.log10(posting.tf());
            final double tfIdfScore = tf * idf;
            posting.setTfIdfScore(tfIdfScore);
            log.info("tf-idf=(1+log10({}))*(log10({}/{}))={}", 
                posting.tf(), nDocs, postingsList.size(), tfIdfScore);
        }
    }
}