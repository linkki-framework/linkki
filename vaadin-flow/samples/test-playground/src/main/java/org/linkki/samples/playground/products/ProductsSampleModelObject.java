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

package org.linkki.samples.playground.products;

public class ProductsSampleModelObject {

    public static final String DEFAULT_VALUE_NAME = "Value of Name";
    public static final String DEFAULT_VALUE_PROPERTY = "Value of Property";
    private String name;
    private String property;

    public ProductsSampleModelObject() {
        this(DEFAULT_VALUE_NAME, DEFAULT_VALUE_PROPERTY);
    }

    public ProductsSampleModelObject(String name, String property) {
        this.name = name;
        this.property = property;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty2() {
        return "Static Value of Property 2";
    }

}