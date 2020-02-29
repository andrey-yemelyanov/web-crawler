package helvidios.search.webcrawler.exceptions;

/**
 * Indicates that a timeout occurred during an operation on the URL queue.
 */
public class QueueTimeoutException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Initializes a new instance of {@link QueueTimeoutException}.
     * @param errorMessage
     */
    public QueueTimeoutException(String errorMessage) {
        super(errorMessage);
    }
}