package org.linkki.framework.ui.dialogs;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class TestLoggingHandler extends Handler {

    private String message;

    @Override
    public void publish(LogRecord record) {
        message = record.getMessage();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
