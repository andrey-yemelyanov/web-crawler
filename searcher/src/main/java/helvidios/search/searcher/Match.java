package helvidios.search.searcher;

/**
 * Represents a search hit.
 */
public class Match {
    
    private final int docId;
    private final String url;
    private final String title;
    private final String content;
    private final double score;

    private Match(
        int docId, 
        String url, 
        String title, 
        String content,
        double score){
        this.docId = docId;
        this.url = url;
        this.title = title;
        this.content = content;
        this.score = score;
    }

    /**
     * Returns document ID where the match occurs.
     */
    public int docId(){ 
        return docId;
    }

    /**
     * Returns document URL where the match occurs.
     */
    public String url(){
        return url;
    }

    /**
     * Returns document title.
     */
    public String title(){
        return title;
    }

    /**
     * Returns document content in HTML format.
     */
    public String content(){
        return content;
    }

    /**
     * Returns the score of this match document.
     */
    public double score(){
        return score;
    }

    public static class Builder{
        private int docId = 0;
        private String url = "unknown-url";
        private String title = "unknown-title";
        private String content = "no-content";
        private double score = 0.0;

        public Builder docId(int docId){
            this.docId = docId;
            return this;
        }

        public Builder documentUrl(String url){
            this.url = url;
            return this;
        }

        public Builder documentTitle(String title){
            this.title = title;
            return this;
        }

        public Builder documentContent(String content){
            this.content = content;
            return this;
        }

        public Builder documentScore(double score){
            this.score = score;
            return this;
        }

        public Match build(){
            return new Match(docId, url, title, content, score);
        }
    }
}