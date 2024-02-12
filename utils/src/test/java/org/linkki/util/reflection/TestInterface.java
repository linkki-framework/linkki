/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.util.reflection;

@FunctionalInterface
public interface TestInterface {

    String PROPERTY_RO_DEFAULT_BOOLEAN = "DefaultBooleanProperty";
    String NON_PROPERTY = "nonProperty";
    String NON_PROPERTY_GETTER_HAS_ARGUMENT = "helloString";
    String RO_DEFAULT_METHOD = "roDefaultMethod";
    String PROPERTY_DO_SOMETHING = "doSomething";
    boolean DEFAULT_PROPERTY_VALUE = true;

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
