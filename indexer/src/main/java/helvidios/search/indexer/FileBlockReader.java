package helvidios.search.indexer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.logging.log4j.Logger;

public class FileBlockReader implements BlockReader {

    private final InputStream inputStream;
    private final Logger log;
    private final String filePath;

    public FileBlockReader(String filePath, Logger log) throws Exception {
        inputStream = new BufferedInputStream(new FileInputStream(filePath));
        this.log = log;
        this.filePath = filePath;
    }

    @Override
    public Iterator<TermDocIdPair> iterator() {
        return new Iterator<TermDocIdPair>(){

            @Override
            public boolean hasNext() {
                try{
                    return inputStream.available() != 0;
                }
                catch(Exception ex){
                    log.error(ex);
                    return false;
                }
            }

            @Override
            public TermDocIdPair next() {
                try{
                    // byte 0 stores the byte length of the term
                    int termLen = inputStream.read();

                    // read termLen bytes for the term
                    byte[] termBytes = new byte[termLen];
                    for(int i = 0; i < termLen; i++){
                        termBytes[i] = (byte) inputStream.read();
                    }
                    String term = new String(termBytes);
                    
                    // read 4 bytes for the docId
                    byte[] docIdBytes = new byte[]{
                        (byte) inputStream.read(),
                        (byte) inputStream.read(),
                        (byte) inputStream.read(),
                        (byte) inputStream.read()
                    };
                    int docId = ((docIdBytes[0] & 0xFF) << 24) | 
                                ((docIdBytes[1] & 0xFF) << 16) | 
                                ((docIdBytes[2] & 0xFF) << 8 ) | 
                                ((docIdBytes[3] & 0xFF) << 0 );

                    // read boolean byte
                    boolean termAppearsInDocTitle = inputStream.read() == 1;

                    return new TermDocIdPair(term, docId, termAppearsInDocTitle);
                }
                catch(Exception ex){
                    log.error(ex);
                    return null;
                }
            }

        };
    }

    @Override
    public void close() throws Exception {
        inputStream.close();
    }

    @Override
    public String filePath() {
        return filePath;
    }
    
}