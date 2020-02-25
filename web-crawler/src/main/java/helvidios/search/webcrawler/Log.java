package helvidios.search.webcrawler;

/**
 * Represents a generic logging component.
 */
public interface Log{
    /**
     * Logs information message.
     * @param msg message
     */
    public void info(String msg);

    /**
     * Logs error message.
     * @param err error
     */
    public void err(String err);
}