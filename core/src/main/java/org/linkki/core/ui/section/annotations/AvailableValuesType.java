/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section.annotations;

/**
 * The type of available values of a property.
 *
 * @author widmaier
 */
public enum AvailableValuesType {

    /**
     * Retrieve the values from an enum class and add the null value. Only works if the returned
     * type of the property is an {@link Enum}.
     */
    ENUM_VALUES_INCL_NULL,

    /**
     * Retrieve the values from an enum class. Only works if the returned type of the property is an
     * {@link Enum}.
     */
    ENUM_VALUES_EXCL_NULL,

    /**
     * When retrieving the content for the input field a method called
     * get[PropertyName]AvailableValues() is called.
     */
    DYNAMIC,

    /** No values can be retrieved, i.e. the collection of available values always is empty. */
    NO_VALUES;

}
