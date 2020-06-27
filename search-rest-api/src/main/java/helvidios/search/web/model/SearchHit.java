package helvidios.search.web.model;

public class SearchHit {

    private final int docId;
    private final String url;
    private final String title;
    private final double score;

    public SearchHit(int docId, String url, String title, double score) {
        this.docId = docId;
        this.url = url;
        this.score = score;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public double getScore() {
        return score;
    }

    public String getUrl() {
        return url;
    }

    public int getDocId() {
        return docId;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof SearchHit)) return false;
        SearchHit other = (SearchHit) obj;
        return this.score == other.score
            && this.url.equals(other.url)
            && this.docId == other.docId
            && this.title.equals(other.title);
    }

    @Override
    public String toString(){
        return String.format("[docId=%d, url=%s, title=%s, score=%f]", docId, url, title, score);
    }
}