package helvidios.search.indexer;

import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;
import org.apache.logging.log4j.Logger;

/**
 * Scalable inverter with support for block-based indexing.
 * When run on a document corpus, this inverter will generate a sorted list
 * of (term, docId) pairs sorted by term and docId. This postings list may be
 * stored in multiple files on disk if the size of the list exceeds {@value #BLOCK_SIZE} records.
 */
class Inverter {

    private final DocumentRepository docRepo;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;
    private final List<TermDocIdPair> postings;
    private final Logger log;

    private static final int BLOCK_SIZE = 10 * 1000 * 1000;

    Inverter(
        DocumentRepository docRepo,
        Tokenizer tokenizer, 
        Lemmatizer lemmatizer,
        Logger log){
        this.docRepo = docRepo;
        this.tokenizer = tokenizer;
        this.lemmatizer = lemmatizer;
        postings = new ArrayList<>();
        this.log = log;
    }

    /**
     * Builds a sorted list of (term, docId) pairs for each document in the corpus. 
     * Stores the resulting list on potentially multiple block files on disk. 
     * @return list of file pathnames to all the blocks
     * @throws Exception
     */
    List<String> buildPostings() throws Exception {
        List<String> blocks = new ArrayList<>();
        Iterator<HtmlDocument> it = docRepo.iterator();
        int nDocs = 0;

        log.info("Inverter started.");

        while(it.hasNext()){
            
            HtmlDocument doc = it.next();
            invert(doc);
            nDocs++;

            log.info("Inverted document {}", doc.toString());

            if(postings.size() >= BLOCK_SIZE){
                blocks.add(writeBlock());
            }
        }

        // write final block to the file
        blocks.add(writeBlock());

        log.info("Inverter completed. Processed {} docs.", nDocs);
        log.info("Created {} blocks.\n{}", blocks.size(), blocks.toString());

        return blocks;
    }

    private String writeBlock() throws Exception{

        Collections.sort(postings);

        try(BlockWriter blockWriter = new FileBlockWriter()){
            log.info("Writing a block of {} postings to file {}...", postings.size(), blockWriter.filePath());

            for(TermDocIdPair posting : postings){
                blockWriter.writePosting(posting);
            }

            postings.clear(); // clear postings for the next block

            log.info("Block successfully written to file {}", blockWriter.filePath());

            return blockWriter.filePath();
        }
    }

    private void invert(HtmlDocument doc) throws Exception {
        List<String> docTerms = lemmatizer.getLemmas(tokenizer.getTokens(doc.getContent()));
        Set<String> titleTerms = new HashSet<>(lemmatizer.getLemmas(tokenizer.getTokens(doc.getTitle())));
        
        for(String term : docTerms){
            postings.add(new TermDocIdPair(term, doc.getId(), titleTerms.contains(term)));
        }

        for(String term : titleTerms){
            postings.add(new TermDocIdPair(term, doc.getId(), true));
        }
    }
}