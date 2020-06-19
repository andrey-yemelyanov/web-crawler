package helvidios.search.indexer;

import helvidios.search.index.storage.IndexRepository;
import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;
import org.apache.logging.log4j.Logger;

public class IndexBuilder implements AutoCloseable {
    
    private final DocumentRepository docRepo;
    private final IndexRepository indexRepo;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;
    private final Logger log;

    private List<BlockReader> readers = new ArrayList<>();

    public IndexBuilder(
        DocumentRepository docRepo,
        IndexRepository indexRepo,
        Tokenizer tokenizer,
        Lemmatizer lemmatizer,
        Logger log){
        this.docRepo = docRepo;
        this.indexRepo = indexRepo;
        this.tokenizer = tokenizer;
        this.lemmatizer = lemmatizer;
        this.log = log;
    }

    public void build() throws Exception{
        
        log.info("Indexing started. {} documents in corpus.", docRepo.size());
        long startTime = System.currentTimeMillis();

        Inverter inverter = new Inverter(docRepo, tokenizer, lemmatizer, log);
        List<String> blockFiles = inverter.buildPostings();

        for(String blockFile : blockFiles){
            readers.add(new FileBlockReader(blockFile, log));
        }
        
        String completeBlock;
        try(BlockWriter writer = new FileBlockWriter()){
            ExternalSort externalSort = new ExternalSort(readers, writer);
            completeBlock = externalSort.sort();
        }
        
        try(BlockReader br = new FileBlockReader(completeBlock, log)){
            Indexer indexer = new Indexer(indexRepo, br, log);
            indexer.buildIndex();
        }

        long endTime = System.currentTimeMillis();
        long timeElapsed = (endTime - startTime) / 1000;
        log.info("Indexing completed in time: {}. Total index size: {}", format(timeElapsed), indexRepo.size());
    }

    private static String format(long totalSeconds){
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void close() throws Exception {
        for(BlockReader reader : readers){
            reader.close();
        }
    }
}