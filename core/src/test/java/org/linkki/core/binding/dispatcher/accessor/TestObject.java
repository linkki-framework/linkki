package org.linkki.core.binding.dispatcher.accessor;

import static org.junit.Assert.assertTrue;

public class TestObject implements TestInterface {
    public static final String STRING_PROPERTY = "stringProperty";
    public static final String INT_PROPERTY = "intProperty";
    public static final String BOOLEAN_PROPERTY = "booleanProperty";
    public static final String READ_ONLY_LONG_PROPERTY = "readOnlyLongProperty";
    @SuppressWarnings("null")
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

    public long getReadOnlyLongProperty() {
        return 42;
    }

    @Override
    public void doSomething() {
        // do nothing :)
    }
}