package helvidios.search.indexer;

import java.io.*;

class FileBlockWriter implements BlockWriter {

    private final OutputStream outputStream;
    private final String filePath;

    FileBlockWriter() throws IOException {
        File tempFile = File.createTempFile("block-", "-bin");
        filePath = tempFile.getPath();
        outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
    }

    public String filePath(){
        return filePath;
    }

    @Override
    public void writePosting(TermDocIdPair posting) throws IOException {
        // posting format in binary file:
        // byte 0: byte length of the term that follows
        // bytes 1 to byteLen(term): term
        // bytes byteLen(term) + 1 to byteLen(term) + 5: docId
        // last byte stores boolean: termAppearsInDocTitle
        writeTerm(posting.term());
        writeDocId(posting.docId());
        writeBoolean(posting.termAppearsInDocTitle());
    }

    private void writeBoolean(boolean val) throws IOException {
        byte byteVal = val ? (byte) 1 : (byte) 0;
        outputStream.write(byteVal);
    }

    private void writeTerm(String term) throws IOException {
        byte[] termBytes = term.getBytes();
        outputStream.write(termBytes.length);
        outputStream.write(termBytes);
    }

    private void writeDocId(int docId) throws IOException {
        byte[] docIdBytes = new byte[]{
            (byte) (docId >> 24),
            (byte) (docId >> 16),
            (byte) (docId >> 8),
            (byte) docId
        };

        outputStream.write(docIdBytes);
    }

    @Override
    public void close() throws IOException {
        outputStream.flush();
        outputStream.close();
    }
}