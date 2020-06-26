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
}