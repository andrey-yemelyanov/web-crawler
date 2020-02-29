package helvidios.search.webcrawler.storage;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import helvidios.search.webcrawler.HtmlDocument;
import helvidios.search.webcrawler.logging.Log;
import static com.mongodb.client.model.Filters.*;

/**
 * MongoDB based implementation of {@link DocumentRepository}.
 */
public class MongoDbStorage implements DocumentRepository {

    private final MongoClient client;
    private final MongoCollection<Document> collection;
    private final Log log;

    /**
     * Initializes a new instance of {@link MongoDbStorage}.
     * @param host hostname of the MongoDB server
     * @param port port number on which MongoDB is listening
     * @param log logging component
     */
    public MongoDbStorage(String host, int port, Log log) {
        client = new MongoClient(host, port);
        MongoDatabase db = client.getDatabase("document-db");
        collection = db.getCollection("documents");
        this.log = log;

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE); 
    }

    @Override
    public void save(HtmlDocument doc) {
        if(contains(doc.getId())){
            collection.updateOne(eq("_id", doc.getId()), 
                new Document("$set", 
                    new Document("url", doc.getUrl())
                         .append("content", doc.getContent())
                         .append("insertedDate", new Date())));
        }else{
            collection.insertOne(
            new Document("_id", doc.getId())
                 .append("url", doc.getUrl())
                 .append("content", doc.getContent())
                 .append("insertedDate", new Date()));
        }
    }

    @Override
    public boolean contains(String docId) {
        return collection.find(eq("_id", docId)).first() != null;
    }

    @Override
    public HtmlDocument get(String docId) {
        Document doc = collection.find(eq("_id", docId)).first();
        if (doc == null){
            return null;
        }
            
        try {
            return new HtmlDocument(
                doc.get("url").toString(), 
                doc.get("content").toString());
        } catch (Exception ex) {
            log.err("Unable to create HtmlDocument.", ex);
            return null;
        }
    }

    @Override
    public void delete(String docId) {
        collection.deleteOne(eq("_id", docId));
    }

    @Override
    public int size() {
        return (int) collection.count();
    }

    @Override
    public void clear() {
        collection.deleteMany(new Document());
    }
}