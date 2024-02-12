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

package org.linkki.samples.playground.ts.aspects;

import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.aspects.types.PlaceholderType;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class BindPlaceholderPmo {

    private String placeholder = "I can be changed";

    private String staticPlaceholderText = "";
    private String dynamicPlaceholderText = "";

    @UITextField(position = 10, label = "Placeholder Value")
    public String getPlaceholderText() {
        return placeholder;
    }

    public void setPlaceholderText(String placeholder) {
        this.placeholder = placeholder;
    }

    @UITextField(position = 20, label = "Dynamic Placeholder")
    @BindPlaceholder(placeholderType = PlaceholderType.DYNAMIC)
    public String getPlaceholderDynamicText() {
        return dynamicPlaceholderText;
    }

    public void setPlaceholderDynamicText(String dynamicPlaceholderText) {
        this.dynamicPlaceholderText = dynamicPlaceholderText;
    }

    public String getPlaceholderDynamicTextPlaceholder() {
        return placeholder;
    }

    @UITextField(position = 22, label = "Static Placeholder")
    @BindPlaceholder(value = "A nice static placeholder", placeholderType = PlaceholderType.STATIC)
    public String getPlaceholderStaticText() {
        return staticPlaceholderText;
    }

    public void setPlaceholderStaticText(String staticPlaceholderText) {
        this.staticPlaceholderText = staticPlaceholderText;
    }
}