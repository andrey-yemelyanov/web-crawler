package helvidios.search.index.storage;

import static com.mongodb.client.model.Filters.*;
import java.util.*;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import helvidios.search.index.Posting;
import helvidios.search.index.Term;
import java.util.logging.*;
import java.util.stream.Collectors;

/**
 * MongoDB based implementation of {@link IndexRepository}.
 */
public class MongoDbIndexRepository implements IndexRepository {

    private final MongoClient client;
    private final MongoCollection<Document> indexCollection;
    private final MongoCollection<Document> docStatsCollection;
    private final String connectionDetails;

    private MongoDbIndexRepository(
        String host,
        int port,
        String database){

        final String indexCollectionName = "index";
        final String docStatsCollectionName = "doc-stats";

        this.client = new MongoClient(host, port);
        
        this.indexCollection = client.getDatabase(database).getCollection(indexCollectionName);
        IndexOptions indexOptions = new IndexOptions().unique(true);
        this.indexCollection.createIndex(Indexes.ascending("term"), indexOptions);

        this.docStatsCollection = client.getDatabase(database).getCollection(docStatsCollectionName);

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.OFF);

        this.connectionDetails = String.format("Index repository: MongoDB at %s:%d/%s/%s",
            host, port, database, indexCollectionName);
    }

    @Override
    public String toString(){
        return connectionDetails;
    }

    @Override
    public void addTerm(Term term, List<Posting> postingsList) {
        List<BasicDBObject> list = new ArrayList<>();
        
        for(Posting posting : postingsList){
            Map<String, Object> map = new HashMap<>();
            map.put("docId", posting.docId());
            map.put("tf", posting.tf());
            map.put("termAppearsInDocTitle", posting.termAppearsInDocTitle());
            list.add(new BasicDBObject(map));
        }

        indexCollection.insertOne(
            new Document("term", term.name())
                .append("df", postingsList.size())
                .append("idf", term.idf())
                .append("postingsList", list)
        );
    }

    @Override
    public List<Term> getVocabulary() {
        List<Term> terms = new ArrayList<>();
        for(Document doc : indexCollection.find().sort(new BasicDBObject("term", 1))){
            terms.add(new Term(
                doc.getString("term"), 
                doc.getInteger("df"),
                doc.getDouble("idf")));
        }
        return terms;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Posting> getPostingsList(Term term) {
        Document doc = indexCollection.find(eq("term", term.name())).first();
        if(doc == null) return Arrays.asList();
        List<Document> postingsList = (List<Document>) doc.get("postingsList");
        final Term t = new Term(term.name(), doc.getInteger("df"), doc.getDouble("idf"));
        return postingsList.stream()
                           .map(obj -> new Posting(t, obj.getInteger("docId"), obj.getInteger("tf"), obj.getBoolean("termAppearsInDocTitle")))
                           .collect(Collectors.toList());
    }
    
    /**
     * Builder class for {@link MongoDbIndexRepository}.
     * If no additional parameters are supplied, the following default values are used:
     * HOST={@value #HOST}, PORT={@value #PORT}, DATABASE={@value #DATABASE}
     */
    public static class Builder{
        private final static String HOST = "localhost";
        private final static int PORT = 27017;
        private final static String DATABASE = "search-db";

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
         * Builds an instance of {@link MongoDbIndexRepository}.
         */
        public MongoDbIndexRepository build(){
            return new MongoDbIndexRepository(host, port, database);
        }
    }

    @Override
    public void clear() {
        indexCollection.deleteMany(new Document());
        docStatsCollection.deleteMany(new Document());
    }

    @Override
    public long size() {
        return indexCollection.count();
    }

    @Override
    public long getDocumentLength(int docId) {
        Document doc = docStatsCollection.find(eq("_id", docId)).first();
        return doc.getLong("len");
    }

    @Override
    public void setDocumentLength(int docId, long len) {
        docStatsCollection.insertOne(
            new Document("_id", docId)
                .append("len", len)
        );
    }
}