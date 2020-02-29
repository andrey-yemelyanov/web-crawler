package helvidios.search.webcrawler.exceptions;

public class QueueTimeoutException extends Exception {
    private static final long serialVersionUID = 1L;

    public QueueTimeoutException(String errorMessage) {
        super(errorMessage);
    }
}