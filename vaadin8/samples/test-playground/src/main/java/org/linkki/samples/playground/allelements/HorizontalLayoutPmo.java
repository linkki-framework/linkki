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

package org.linkki.samples.playground.allelements;

import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;

import com.vaadin.ui.Notification;

@BindCaption(value = "UIHorizontalLayout")
@UIHorizontalLayout
public class HorizontalLayoutPmo {

    private String text;
    private String textWithLabel;

    @UILabel(position = 10)
    public String getLabel() {
        return "label";
    }

    @UITextField(label = "", position = 20, required = RequiredType.REQUIRED)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @UITextField(label = "with label", position = 25, required = RequiredType.REQUIRED)
    public String getBananas() {
        return textWithLabel;
    }

    public void setBananas(String text) {
        textWithLabel = text;
    }

    @UIButton(position = 30, caption = "Button")
    public void button() {
        Notification.show("Button clicked");
    }

    @UICheckBox(caption = "Checkbox", position = 40)
    public boolean getCheckbox() {
        return true;
    }
}
