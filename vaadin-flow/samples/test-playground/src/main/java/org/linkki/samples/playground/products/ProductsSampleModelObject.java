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

package org.linkki.samples.playground.products;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;

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

    public MessageList validate() {
        if (StringUtils.isEmpty(property)) {
            return new MessageList();
        } else {
            return new MessageList(
                    Message.newInfo("property1", "Validation: Property1 has value \"" + property + "\""),
                    Message.newInfo("property2", "Validation: Property2 has value \"" + getProperty2() + "\""),
                    Message.newWarning("warning", "Validation: Should be displayed before the infos"));
        }
    }

}