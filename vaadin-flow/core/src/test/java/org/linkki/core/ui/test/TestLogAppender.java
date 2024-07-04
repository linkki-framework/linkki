package org.linkki.core.ui.test;

import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

/**
 * Utility class to test logs.
 * <p>
 * Example usage:
 * 
 * <pre><code>
 *     {@literal @}BeforeEach
 *     void setUpLogAppender() {
 *        var logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);
 *        testLogAppender = new TestLogAppender();
 *        testLogAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
 *        logger.setLevel(Level.DEBUG);
 *        logger.addAppender(testLogAppender);
 *        testLogAppender.start();
 *     }
 * </code></pre>
 */
public class TestLogAppender extends ListAppender<ILoggingEvent> {

    public List<ILoggingEvent> getLoggedEvents(Level level) {
        return this.list.stream().filter(e -> e.getLevel().equals(level)).toList();
    }
}
