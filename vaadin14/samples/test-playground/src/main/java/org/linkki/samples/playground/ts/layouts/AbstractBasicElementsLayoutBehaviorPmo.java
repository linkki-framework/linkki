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

package org.linkki.samples.playground.ts.layouts;

import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UICustomField;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.ui.element.annotation.UIRadioButtons;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.ips.decimalfield.UIDecimalField;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.PasswordField;

public abstract class AbstractBasicElementsLayoutBehaviorPmo {

    private final BasicElementsLayoutBehaviorModelObject modelObject = new BasicElementsLayoutBehaviorModelObject();

    // behaviors to be tested
    private boolean readOnly;
    private boolean required;
    private boolean visible = true;

    @ModelObject
    public BasicElementsLayoutBehaviorModelObject getModelObject() {
        return modelObject;
    }

    @SectionHeader
    @UICheckBox(position = -90, caption = "required")
    public boolean isAllElementsRequired() {
        return required;
    }

    public void setAllElementsRequired(boolean required) {
        this.required = required;
    }

    @SectionHeader
    @UICheckBox(position = -80, caption = "read-only")
    public boolean isAllElementsReadOnly() {
        return readOnly;
    }

    public void setAllElementsReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @SectionHeader
    @UICheckBox(position = -70, caption = "visible")
    public boolean isAllElementsVisible() {
        return visible;
    }

    public void setAllElementsVisible(boolean visible) {
        this.visible = visible;
    }

    @UILabel(position = 5, label = "Label", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_TEXT, //
            visible = VisibleType.DYNAMIC)
    public void textLabel() {
        // model binding
    }

    public boolean isTextLabelVisible() {
        return isAllElementsVisible();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UITextField(position = 10, label = "TextField", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_TEXT, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void text() {
        // model binding
    }

    public boolean isTextRequired() {
        return isAllElementsRequired();
    }

    public boolean isTextReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isTextVisible() {
        return isAllElementsVisible();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UITextArea(position = 20, height = "5em", label = "TextArea", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_LONGTEXT, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void longText() {
        // model binding
    }

    public boolean isLongTextRequired() {
        return isAllElementsRequired();
    }

    public boolean isLongTextReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isLongTextVisible() {
        return isAllElementsVisible();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIIntegerField(position = 30, label = "IntegerField", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_INTVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void intValue() {
        // model binding
    }

    public boolean isIntValueRequired() {
        return isAllElementsRequired();
    }

    public boolean isIntValueReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isIntValueVisible() {
        return isAllElementsVisible();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDoubleField(position = 40, label = "DoubleField", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_DOUBLEVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void doubleValue() {
        // model binding
    }

    public boolean isDoubleValueRequired() {
        return isAllElementsRequired();
    }

    public boolean isDoubleValueReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isDoubleValueVisible() {
        return isAllElementsVisible();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDecimalField(position = 45, label = "DecimalField", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_DECIMALVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void decimalValue() {
        // model binding
    }

    public boolean isDecimalValueRequired() {
        return isAllElementsRequired();
    }

    public boolean isDecimalValueReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isDecimalValueVisible() {
        return isAllElementsVisible();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDateField(position = 50, label = "DateField", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_DATE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void date() {
        // model binding
    }

    public boolean isDateRequired() {
        return isAllElementsRequired();
    }

    public boolean isDateReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isDateVisible() {
        return isAllElementsVisible();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UICustomField(position = 55, label = "CustomField", uiControl = PasswordField.class, //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_SECRET, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void secret() {
        // model binding
    }

    public boolean isSecretRequired() {
        return isAllElementsRequired();
    }

    public boolean isSecretReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isSecretVisible() {
        return isAllElementsVisible();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIComboBox(position = 60, label = "ComboBox", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_ENUMVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void enumValueComboBox() {
        // model binding
    }

    public boolean isEnumValueComboBoxRequired() {
        return isAllElementsRequired();
    }

    public boolean isEnumValueComboBoxReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isEnumValueComboBoxVisible() {
        return isAllElementsVisible();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIRadioButtons(position = 65, label = "RadioButtons", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_ENUMVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void enumValueRadioButton() {
        // model binding
    }

    public boolean isEnumValueRadioButtonRequired() {
        return isAllElementsRequired();
    }

    public boolean isEnumValueRadioButtonReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isEnumValueRadioButtonVisible() {
        return isAllElementsVisible();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UICheckBox(position = 70, caption = "I am a CheckBox", label = "Checkbox", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_BOOLEANVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void booleanValue() {
        // model binding
    }

    public boolean isBooleanValueRequired() {
        return isAllElementsRequired();
    }

    public boolean isBooleanValueReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isBooleanValueVisible() {
        return isAllElementsVisible();
    }

    @BindIcon(iconType = IconType.DYNAMIC)
    @UILink(position = 140, label = "Link", caption = "I am a Link to #", visible = VisibleType.DYNAMIC)
    public String getLink() {
        return "#";
    }

    public VaadinIcon getLinkIcon() {
        return VaadinIcon.LINK;
    }

    public boolean isLinkVisible() {
        return isAllElementsVisible();
    }

    @UIButton(position = 150, caption = "I am a Button", label = "Button", visible = VisibleType.DYNAMIC)
    public void button() {
        Notification.show("Button clicked", 1000, Position.MIDDLE);
    }

    public boolean isButtonVisible() {
        return isAllElementsVisible();
    }

}
