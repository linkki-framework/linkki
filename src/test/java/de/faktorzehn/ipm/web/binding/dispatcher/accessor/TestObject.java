package de.faktorzehn.ipm.web.binding.dispatcher.accessor;

import static org.junit.Assert.assertTrue;

public class TestObject {
    public static final String STRING_PROPERTY = "stringProperty";
    public static final String INT_PROPERTY = "intProperty";
    public static final String BOOLEAN_PROPERTY = "booleanProperty";
    public static final String WRITE_ONLY_INT_PROPERTY = "writeOnlyIntProperty";
    public static final String READ_ONLY_LONG_PROPERTY = "readOnlyLongProperty";
    private String stringValue;

    public String getStringProperty() {
        return stringValue;
    }

    public void setStringProperty(String value) {
        this.stringValue = value;
    }

    public int getIntProperty() {
        return 42;
    }

    @SuppressWarnings("unused")
    public void setIntProperty(int value) {
        // nothing to do
    }

    public boolean isBooleanProperty() {
        return true;
    }

    public void setBooleanProperty(boolean value) {
        assertTrue(value);
    }

    @SuppressWarnings("unused")
    public void setWriteOnlyIntProperty(int i) {
        // do nothing
    }

    public long getReadOnlyLongProperty() {
        return 42;
    }
}