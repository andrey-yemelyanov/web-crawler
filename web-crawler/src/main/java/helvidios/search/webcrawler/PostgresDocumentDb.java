package helvidios.search.webcrawler;

public class PostgresDocumentDb implements DocumentDb{

    private int nDocs;

    @Override
    public String save(Document doc) {
        synchronized(this){
            nDocs++;
        }
        return null;
    }

    @Override
    public void delete(String docId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Document get(String docId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int nDocs() {
        synchronized(this){
            return nDocs;
        }
    }

}