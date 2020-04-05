package helvidios.search.storage;

public class DocId {

    private final int id;

    public DocId(String url){
        id = url.hashCode();
    }

    public DocId(int id){
        this.id = id;
    }

    public int get(){
        return id;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof DocId)) return false;
        DocId other = (DocId) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public String toString(){
        return String.format("[id=%d]", id);
    }
}