/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.binding.dispatcher.reflection.accessor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class TestObject implements TestInterface {
    public static final String STRING_PROPERTY = "stringProperty";
    public static final String INT_PROPERTY = "intProperty";
    public static final String BOOLEAN_PROPERTY = "booleanProperty";
    public static final String READ_ONLY_LONG_PROPERTY = "readOnlyLongProperty";
    public static final String EXCEPTION_PROPERTY = "throwException";
    @CheckForNull
    private String stringValue;
    private int intValue = 42;
    private boolean booleanValue;

    @CheckForNull
    public String getStringProperty() {
        return stringValue;
    }

    public void setStringProperty(String value) {
        this.stringValue = value;
    }

    public int getIntProperty() {
        return intValue;
    }

    public void setIntProperty(int value) {
        intValue = value;
    }

    public boolean isBooleanProperty() {
        return booleanValue;
    }

    public void setBooleanProperty(boolean value) {
        this.booleanValue = value;
    }

    public long getReadOnlyLongProperty() {
        return 42;
    }

    public String getThrowException() {
        throw new IllegalArgumentException("test exception");
    }

    public void setThrowException(String value) {
        throw new IllegalArgumentException("test exception");
    }

    public void throwException() {
        throw new IllegalArgumentException("test exception");
    }
    @Override
    public void doSomething() {
        setBooleanProperty(!isBooleanProperty());
    }

    public void getVoid() {
        // do nothing
    }

}