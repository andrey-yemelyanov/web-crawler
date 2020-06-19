package helvidios.search.indexer;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class ExternalSort {

    private static class Item implements Comparable<Item> {
        TermDocIdPair posting;
        int blockId;

        Item(TermDocIdPair posting, int blockId) {
            this.posting = posting;
            this.blockId = blockId;
        }

        @Override
        public int compareTo(Item o) {
            return this.posting.compareTo(o.posting);
        }

        @Override
        public String toString(){
            return posting + ", blockId=" + blockId;
        }
    }

    private final List<Iterator<TermDocIdPair>> readers;
    private final BlockWriter writer;
    private final List<TermDocIdPair> buffer;

    private static final int MAX_BUFFER_SIZE = 10 * 1000;

    /**
     * Initializes a new instance of {@link ExternalSort}.
     * 
     * @param readers list of readers for the input blocks
     * @param writer  writer where all merged blocks will be written
     */
    ExternalSort(List<BlockReader> readers, BlockWriter writer) {
        this.readers = readers.stream()
                              .map(reader -> reader.iterator())
                              .collect(Collectors.toList());
        
        this.writer = writer;
        this.buffer = new ArrayList<>();
    }

    /**
     * Externally sorts blocks of data and returns a pathname to the resulting
     * sorted file.
     * 
     * @throws IOException
     */
    String sort() throws IOException {

        PriorityQueue<Item> pq = new PriorityQueue<>();
        for (int i = 0; i < readers.size(); i++) {
            Iterator<TermDocIdPair> it = readers.get(i);
            if (it.hasNext()) {
                pq.add(new Item(it.next(), i));
            }
        }

        while (!pq.isEmpty()) {
            Item item = pq.remove();
            buffer.add(item.posting);
            if (buffer.size() >= MAX_BUFFER_SIZE) {
                flushBuffer();
            }
            Iterator<TermDocIdPair> it = readers.get(item.blockId);
            if (it.hasNext()) {
                pq.add(new Item(it.next(), item.blockId));
            }
        }

        flushBuffer();

        return writer.filePath();
    }

    private void flushBuffer() throws IOException {
        for(TermDocIdPair posting : buffer){
            writer.writePosting(posting);
        }
        buffer.clear();
    }
}