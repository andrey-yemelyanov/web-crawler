package helvidios.search.web.model;

public class SearchHit {

    private final int docId;
    private final String url;
    private final double score;

    public SearchHit(int docId, String url, double score) {
        this.docId = docId;
        this.url = url;
        this.score = score;
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
}