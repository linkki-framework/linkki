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

package org.linkki.samples.playground.dynamicannotations;

import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIFormSection;

import com.vaadin.flow.component.notification.Notification;

@UIFormSection(caption = "@UIFormSection")
public class FormSectionPmo {

    private String text1 = "text1";
    private String text2 = "text2";
    private String text3 = "text3";
    private boolean required;

    @UITextField(position = 10, label = "Text field")
    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    @UITextField(position = 20, label = "TextFieldWithLongLabel")
    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    @UITextField(position = 30, label = "Required field", required = RequiredType.DYNAMIC)
    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    @UICheckBox(position = 40, label = "Checkbox caption")
    public boolean isText3Required() {
        return required;
    }

    public void setText3Required(boolean required) {
        this.required = required;
    }

    @UILabel(position = 50, label = "label with label :D")
    public String getLabel() {
        return "label";
    }

    @UIButton(position = 60, caption = "Button caption")
    public void button() {
        Notification.show("Button clicked");
    }
}
