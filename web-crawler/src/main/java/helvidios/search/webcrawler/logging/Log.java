package helvidios.search.webcrawler.logging;

/**
 * Represents a generic logging component.
 */
public interface Log{
    /**
     * Logs an information message.
     * @param msg message
     */
    public void info(String msg);

    /**
     * Logs an error message along with the stack trace.
     * @param err error
     * @param ex exception whose stack trace will be logged
     */
    public void err(String err, Exception ex);

    /**
     * Logs a debug message.
     * @param msg
     */
    public void debug(String msg);
}