package helvidios.search.indexer;

import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.storage.*;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;
import java.util.function.Supplier;
import org.apache.logging.log4j.Logger;

class Inverter {

    private final DocumentRepository docRepo;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;
    private final List<TermDocIdPair> postings;
    private final Logger log;

    private static final int BLOCK_SIZE = 1 * 1000 * 1000;

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

    List<String> index() throws Exception {
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
        List<String> tokens = tokenizer.getTokens(doc.getContent());
        List<String> terms = lemmatizer.getLemmas(tokens);
        for(String term : terms){
            postings.add(new TermDocIdPair(term, doc.getId()));
        }
    }
}