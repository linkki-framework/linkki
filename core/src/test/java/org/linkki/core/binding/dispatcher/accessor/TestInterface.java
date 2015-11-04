/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher.accessor;

@FunctionalInterface
public interface TestInterface {

    static final String RO_DEFAULT_BOOLEAN_PROPERTY = "DefaultBooleanProperty";
    static final String NON_PROPERTY = "nonProperty";
    static final String HELLO_STRING_METHOD = "helloString";
    static final String RO_DEFAULT_METHOD = "roDefaultMethod";
    static final boolean DEFAULT_PROPERTY_VALUE = true;

    void doSomething();

    default boolean isDefaultBooleanProperty() {
        return DEFAULT_PROPERTY_VALUE;
    }

    default String getRoDefaultMethod() {
        return "Hello";
    }

    default String getHelloString(String s) {
        return "Hello " + s;
    }

    default boolean hasNonProperty() {
        return DEFAULT_PROPERTY_VALUE;
    }
}
