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
package org.linkki.util.reflection;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class TestObject implements TestInterface {
    public static final String PROPERTY_STRING = "stringProperty";
    public static final String PROPERTY_INT = "intProperty";
    public static final String PROPERTY_BOOLEAN = "booleanProperty";
    public static final String PROPERTY_READ_ONLY_LONG = "readOnlyLongProperty";
    public static final String PROPERTY_EXCEPTION = "throwException";
    public static final String PROPERTY_WITH_NON_VOID_SETTER = "withNonVoidSetter";
    public static final String PROPERTY_STRING_PROPERTY_WITH_IS = "stringPropertyWithIs";

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

    public String getWithNonVoidSetter() {
        return "";
    }

    public String setWithNonVoidSetter(String value) {
        return value;
    }

    public String isStringPropertyWithIs() {
        return "";
    }
}