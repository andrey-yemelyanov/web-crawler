package helvidios.search.indexer;

import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;
import org.apache.commons.lang3.time.StopWatch;
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
    private final StopWatch stopWatch;
    private final StopWatch overallStopWatch;

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
        this.stopWatch = new StopWatch();
        this.overallStopWatch = new StopWatch();
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
        overallStopWatch.start();
        
        while(it.hasNext()){
            
            stopWatch.reset();
            stopWatch.start();
            HtmlDocument doc = it.next();
            stopWatch.stop();
            log.info("Retrieved {} from docRepo in {}", doc.toString(), stopWatch.toString());

            stopWatch.reset();
            stopWatch.start();
            final int nLemmas = invert(doc);
            nDocs++;
            stopWatch.stop();
            log.info("Inverted document {} ({} lemmas) in {}", doc.toString(), nLemmas, stopWatch.toString());

            if(postings.size() >= BLOCK_SIZE){
                blocks.add(writeBlock());
            }
        }

        // write final block to the file
        blocks.add(writeBlock());

        overallStopWatch.stop();

        log.info("Inverter completed. Processed {} docs in {}", nDocs, overallStopWatch.toString());
        log.info("Created {} blocks.\n{}", blocks.size(), blocks.toString());

        return blocks;
    }

    private String writeBlock() throws Exception{

        stopWatch.reset();
        stopWatch.start();

        Collections.sort(postings);

        try(BlockWriter blockWriter = new FileBlockWriter()){

            for(TermDocIdPair posting : postings){
                blockWriter.writePosting(posting);
            }

            postings.clear(); // clear postings for the next block

            stopWatch.stop();
            log.info("Block successfully written to file {} in {}", blockWriter.filePath(), stopWatch.toString());

            return blockWriter.filePath();
        }
    }

    private int invert(HtmlDocument doc) throws Exception {
        final StopWatch sw = new StopWatch();
        sw.start();
        List<String> tokens = tokenizer.getTokens(doc.getContent());
        sw.stop();
        log.info("Extracted {} tokens in {}", tokens.size(), sw.toString());

        sw.reset();
        sw.start();
        List<String> docTerms = lemmatizer.getLemmas(tokens);
        sw.stop();
        log.info("Extracted {} lemmas in {}", tokens.size(), sw.toString());

        Set<String> titleTerms = new HashSet<>(lemmatizer.getLemmas(tokenizer.getTokens(doc.getTitle())));
        
        for(String term : docTerms){
            postings.add(new TermDocIdPair(term, doc.getId(), titleTerms.contains(term)));
        }

        for(String term : titleTerms){
            postings.add(new TermDocIdPair(term, doc.getId(), true));
        }

        return docTerms.size() + titleTerms.size();
    }
}