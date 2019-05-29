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

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UICustomField;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.nls.NlsText;

import com.vaadin.ui.PasswordField;

@UISection(caption = NlsText.I18n)
public class AllUiElementsPmo {

    private final AllUiElementsModelObject modelObject = new AllUiElementsModelObject();

    @ModelObject
    public AllUiElementsModelObject getModelObject() {
        return modelObject;
    }

    @UITextField(position = 10, label = NlsText.I18n)
    public void text() {
        // model binding
    }

    @UITextArea(position = 20, label = NlsText.I18n)
    public void longText() {
        // model binding
    }

    @UIIntegerField(position = 30, label = NlsText.I18n)
    public void intValue() {
        // model binding
    }

    @UIDoubleField(position = 40, label = NlsText.I18n)
    public void doubleValue() {
        // model binding
    }

    @UIDateField(position = 50, label = NlsText.I18n)
    public void date() {
        // model binding
    }

    @UIComboBox(position = 60, label = NlsText.I18n)
    public void enumValue() {
        // model binding
    }

    @UICheckBox(position = 70, caption = NlsText.I18n)
    public void booleanValue() {
        // model binding
    }

    @UILabel(position = 80, label = NlsText.I18n, modelAttribute = "secret")
    public void textLabel() {
        // model binding
    }

    @UICustomField(position = 90, label = NlsText.I18n, uiControl = PasswordField.class)
    public void secret() {
        // model binding
    }

    @UIButton(position = 100, caption = NlsText.I18n)
    public void action() {
        getModelObject().setIntValue(getModelObject().getIntValue() + 1);
    }

}
