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

    private MongoDbStorage(
        String host, 
        int port, 
        Log log,
        String database,
        String collection) {
        
        client = new MongoClient(host, port);
        MongoDatabase db = client.getDatabase(database);
        this.collection = db.getCollection(collection);
        this.log = log;

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.OFF); 
    }

    /**
     * Builder class for {@link MongoDbStorage}.
     */
    public static class Builder{
        private final static String HOST = "localhost";
        private final static int PORT = 27017;
        private final static String DATABASE = "document-db";
        private final static String COLLECTION = "documents";

        private String host = HOST;
        private int port = PORT;
        private Log log;
        private String database = DATABASE;
        private String collection = COLLECTION;

        /**
         * Initializes a new instance of {@link Builder}.
         * @param log logging component
         */
        public Builder(Log log){
            this.log = log;
        }

        /**
         * Sets database server host. Default is {@value #HOST}.
         * @param host hostname
         */
        public Builder setHost(String host){
            this.host = host;
            return this;
        }

        /**
         * Sets database server port number. Default is {@value #PORT}.
         * @param port port number
         */
        public Builder setPort(int port){
            this.port = port;
            return this;
        }

        /**
         * Sets MongoDB database name to connect to. Default is {@value #DATABASE}.
         * @param database database name
         */
        public Builder setDatabase(String database){
            this.database = database;
            return this;
        }

        /**
         * Sets MongoDB collection to work with. Default is {@value #COLLECTION}.
         * @param collection collection name
         */
        public Builder setCollection(String collection){
            this.collection = collection;
            return this;
        }

        /**
         * Builds an instance of {@link MongoDbStorage}.
         */
        public MongoDbStorage build(){
            return new MongoDbStorage(host, port, log, database, collection);
        }
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