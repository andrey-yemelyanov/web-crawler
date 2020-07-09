package helvidios.search.indexer;

import helvidios.search.index.storage.IndexRepository;
import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.tokenizer.Tokenizer;
import java.util.*;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.Logger;

public class IndexBuilder implements AutoCloseable {
    
    private final DocumentRepository docRepo;
    private final IndexRepository indexRepo;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;
    private final Logger log;
    private final StopWatch stopWatch;

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
        this.stopWatch = new StopWatch();
    }

    public void build() throws Exception{
        
        log.info("Indexing started. {} documents in corpus.", docRepo.size());
        stopWatch.start();

        Inverter inverter = new Inverter(docRepo, tokenizer, lemmatizer, log);
        List<String> blockFiles = inverter.buildPostings();

        String completeBlock;
        try(BlockWriter writer = new FileBlockWriter()){
            for(String blockFile : blockFiles){
                readers.add(new FileBlockReader(blockFile, log));
            }

            ExternalSort externalSort = new ExternalSort(readers, writer, log);
            completeBlock = externalSort.sort();
        }
        
        try(BlockReader br = new FileBlockReader(completeBlock, log)){
            Indexer indexer = new Indexer(indexRepo, br, log, docRepo.size());
            indexer.buildIndex();
        }

        stopWatch.stop();
        log.info("Indexing completed in time: {}. Total index size: {}", stopWatch.toString(), indexRepo.size());
    }

    @Override
    public void close() throws Exception {
        for(BlockReader reader : readers){
            reader.close();
        }
    }
}