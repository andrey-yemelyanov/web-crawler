package helvidios.search.storage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedDocumentRepository implements DocumentRepository {

    private final DocumentRepository docRepo;

    public CompressedDocumentRepository(DocumentRepository docRepo) {
        this.docRepo = Objects.requireNonNull(docRepo);
    }

    private HtmlDocument compress(HtmlDocument doc) {
        if(doc == null) return null;
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream(doc.getContent().length())){
            try(GZIPOutputStream gzip = new GZIPOutputStream(bos)){
                gzip.write(doc.getContent().getBytes(StandardCharsets.UTF_8));
                gzip.close();
                byte[] compressed = bos.toByteArray();
                bos.close();
                return new HtmlDocument(doc.getUrl(), new String(compressed), doc.getTitle());
            }
        }catch(Exception ex){
            ex.printStackTrace();
            return doc;
        }
    }

    private HtmlDocument decompress(HtmlDocument doc){
        if(doc == null) return null;
        try(ByteArrayInputStream bis = new ByteArrayInputStream(doc.getContent().getBytes(StandardCharsets.UTF_8))){
            try(GZIPInputStream gis = new GZIPInputStream(bis)){
                try(BufferedReader br = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8))){
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                    }
                    return new HtmlDocument(doc.getUrl(), sb.toString(), doc.getTitle());
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            return doc;
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
                HtmlDocument doc = it.next();
                return decompress(doc);
            }
        };
    }

    @Override
    public long size() {
        return docRepo.size();
    }
    
}