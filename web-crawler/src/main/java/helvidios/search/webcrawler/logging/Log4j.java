package helvidios.search.webcrawler.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Log4j2 based logging component.
 */
public class Log4j implements Log {
    private static final Logger LOGGER = LogManager.getLogger(Log4j.class.getName());

    @Override
    public void info(String msg) {
        LOGGER.info(msg);
    }

    @Override
    public void err(String err, Exception ex) {
        LOGGER.error(err, ex);
    }

    @Override
    public void debug(String msg) {
        LOGGER.debug(msg);
    }
}