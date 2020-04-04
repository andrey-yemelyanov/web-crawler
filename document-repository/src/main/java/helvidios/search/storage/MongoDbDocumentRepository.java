package helvidios.search.storage;

import java.util.Date;
import java.util.Iterator;
import java.util.logging.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;

/**
 * MongoDB based implementation of {@link DocumentRepository}.
 */
public class MongoDbDocumentRepository implements DocumentRepository {

    private final MongoClient client;
    private final MongoCollection<Document> collection;

    private MongoDbDocumentRepository(
        String host, 
        int port, 
        String database){

        this.client = new MongoClient(host, port);
        this.collection = client.getDatabase(database).getCollection("documents");

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.OFF);
    }

    public void insert(HtmlDocument doc) {
        collection.insertOne(
            new Document("_id", doc.getId())
                 .append("url", doc.getUrl())
                 .append("content", doc.getContent())
                 .append("insertedDate", new Date()));
    }

    private HtmlDocument toHtmlDocument(Document doc){
        if(doc == null) return null;
        return new HtmlDocument(
                doc.get("url").toString(), 
                doc.get("content").toString());
    }

    public HtmlDocument get(int id) {
        Document doc = collection.find(eq("_id", id)).first();
        return toHtmlDocument(doc);
    }

    public boolean contains(String url) {
        return get(url) != null;
    }

    public HtmlDocument get(String url) {
        Document doc = collection.find(eq("url", url)).first();
        return toHtmlDocument(doc);
    }

    public void clear() {
        collection.deleteMany(new Document());
    }

    public Iterator<HtmlDocument> iterator() {
        return new Iterator<HtmlDocument>() {
            private Iterator<Document> it = collection.find().iterator();

            public boolean hasNext() {
                return it.hasNext();
            }

            public HtmlDocument next() {
                Document doc = it.next();
                return toHtmlDocument(doc);
            }
        };
    }

    public long size() {
        return collection.count();
    }

    /**
     * Builder class for {@link MongoDbDocumentRepository}.
     */
    public static class Builder{
        private final static String HOST = "localhost";
        private final static int PORT = 27017;
        private final static String DATABASE = "document-db";

        private String host = HOST;
        private int port = PORT;
        private String database = DATABASE;

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
         * Builds an instance of {@link MongoDbDocumentRepository}.
         */
        public MongoDbDocumentRepository build(){
            return new MongoDbDocumentRepository(host, port, database);
        }
    }
}