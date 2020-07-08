package helvidios.search.storage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.logging.log4j.Logger;

/**
 * Represents document repository which stores HTML documents compressed using
 * GZIP.
 */
public class CompressedDocumentRepository implements DocumentRepository {

    private final DocumentRepository docRepo;
    private final Logger log;

    /**
     * Initializes a new instance of compressed document repository.
     * 
     * @param docRepo backing document repository
     */
    public CompressedDocumentRepository(DocumentRepository docRepo, Logger log) {
        this.docRepo = Objects.requireNonNull(docRepo);
        this.log = log;
    }

    private HtmlDocument compress(HtmlDocument doc) {
        if(doc == null) return null;
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            try(GZIPOutputStream gzip = new GZIPOutputStream(bos)){
                gzip.write(doc.getContent().getBytes("UTF-8"));
                gzip.finish();
                return new HtmlDocument(
                    doc.getUrl(), 
                    Base64.getEncoder().encodeToString(bos.toByteArray()), 
                    doc.getTitle());
            }
        }catch (IOException e) {
            log.error(e);
            throw new RuntimeException(String.format(
                "Unable to compress HTML document %d '%s'. %s. See log for more details.", 
                    doc.getId(), doc.getTitle(), e.getMessage()));
        }
    }

    private HtmlDocument decompress(HtmlDocument doc) {
        if(doc == null) return null;
        byte[] contentBytes = Base64.getDecoder().decode(doc.getContent());
        try(ByteArrayInputStream bis = new ByteArrayInputStream(contentBytes)){
            try(GZIPInputStream gis = new GZIPInputStream(bis)){
                try(BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"))){
                    return new HtmlDocument(doc.getUrl(), br.lines().collect(Collectors.joining("\n")), doc.getTitle());
                }
            }
        }catch (IOException e) {
            log.error(e);
            throw new RuntimeException(String.format(
                "Unable to decompress HTML document %d '%s'. %s. See log for more details.", 
                    doc.getId(), doc.getTitle(),  e.getMessage()));
        }
    }

    @Override
    public void insert(HtmlDocument doc) {
        docRepo.insert(compress(doc));
    }

    @Override
    public HtmlDocument get(int id) {
        return decompress(docRepo.get(id));
    }

    @Override
    public boolean contains(String url) {
        return docRepo.contains(url);
    }

    @Override
    public HtmlDocument get(String url) {
        return decompress(docRepo.get(url));
    }

    @Override
    public void clear() {
        docRepo.clear();
    }

    @Override
    public Iterator<HtmlDocument> iterator() {
        return new Iterator<HtmlDocument>() {
            private Iterator<HtmlDocument> it = docRepo.iterator();

            public boolean hasNext() {
                return it.hasNext();
            }

            public HtmlDocument next() {
                return decompress(it.next());
            }
        };
    }

    @Override
    public long size() {
        return docRepo.size();
    }
    
}