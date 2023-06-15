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

import java.util.List;

import org.linkki.core.defaults.ui.aspects.types.EnabledType;
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
import org.linkki.core.ui.element.annotation.UIDateTimeField;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.ui.element.annotation.UIMultiSelect;
import org.linkki.core.ui.element.annotation.UIRadioButtons;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.element.annotation.UIYesNoComboBox;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.ips.decimalfield.UIDecimalField;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorModelObject.SampleEnum;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.PasswordField;

public abstract class AbstractBasicElementsLayoutBehaviorPmo {

    public static final String TEXT_FIELD_LONG_LABEL = "TextFieldWithALongExtendedLabel toTestLabelOverflowBehavior";

    private final BasicElementsLayoutBehaviorModelObject modelObject = new BasicElementsLayoutBehaviorModelObject();

    // behaviors to be tested
    private boolean readOnly;
    private boolean required;
    private boolean visible = true;
    private boolean enabled = true;

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

    @SectionHeader
    @UICheckBox(position = -60, caption = "enabled")
    public boolean isAllElementsEnabled() {
        return enabled;
    }

    public void setAllElementsEnabled(boolean enabled) {
        this.enabled = enabled;
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
    @UITextField(position = 10, label = TEXT_FIELD_LONG_LABEL, //
            width = "50%", modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_TEXT, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
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

    public boolean isTextEnabled() {
        return isAllElementsEnabled();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UITextArea(position = 20, height = "5em", label = "TextArea", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_LONGTEXT, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
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

    public boolean isLongTextEnabled() {
        return isAllElementsEnabled();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIIntegerField(position = 30, label = "IntegerField", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_INTVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
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

    public boolean isIntValueEnabled() {
        return isAllElementsEnabled();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDoubleField(position = 40, label = "DoubleField", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_DOUBLEVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
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

    public boolean isDoubleValueEnabled() {
        return isAllElementsEnabled();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDecimalField(position = 45, label = "DecimalField", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_DECIMALVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
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

    public boolean isDecimalValueEnabled() {
        return isAllElementsEnabled();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDateField(position = 50, label = "DateField", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_DATE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
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

    public boolean isDateEnabled() {
        return isAllElementsEnabled();
    }


    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDateTimeField(position = 52, label = "DateTimeField", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_DATE_TIME, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
    public void dateTime() {
        // model binding
    }

    public boolean isDateTimeRequired() {
        return isAllElementsRequired();
    }

    public boolean isDateTimeReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isDateTimeVisible() {
        return isAllElementsVisible();
    }

    public boolean isDateTimeEnabled() {
        return isAllElementsEnabled();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UICustomField(position = 55, label = "CustomField", uiControl = PasswordField.class, //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_SECRET, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
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

    public boolean isSecretEnabled() {
        return isAllElementsEnabled();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIComboBox(position = 60, label = "ComboBox", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_ENUMVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
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

    public boolean isEnumValueComboBoxEnabled() {
        return isAllElementsEnabled();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIYesNoComboBox(position = 61, label = "YesNoComboBox", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_BOOLEANVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
    public void yesNoComboBox() {
        // model binding
    }

    public boolean isYesNoComboBoxRequired() {
        return isAllElementsRequired();
    }

    public boolean isYesNoComboBoxReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isYesNoComboBoxVisible() {
        return isAllElementsVisible();
    }

    public boolean isYesNoComboBoxEnabled() {
        return isAllElementsEnabled();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIMultiSelect(position = 62, label = "MutliSelect", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_ENUMVALUES, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
    public void enumValuesMultiSelect() {
        // model binding
    }

    public boolean isEnumValuesMultiSelectRequired() {
        return isAllElementsRequired();
    }

    public boolean isEnumValuesMultiSelectReadOnly() {
        return isAllElementsReadOnly();
    }

    public boolean isEnumValuesMultiSelectVisible() {
        return isAllElementsVisible();
    }

    public boolean isEnumValuesMultiSelectEnabled() {
        return isAllElementsEnabled();
    }

    public List<SampleEnum> getEnumValuesMultiSelectAvailableValues() {
        return List.of(SampleEnum.values());
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIRadioButtons(position = 65, label = "RadioButtons", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_ENUMVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
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

    public boolean isEnumValueRadioButtonEnabled() {
        return isAllElementsEnabled();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UICheckBox(position = 70, caption = "I am a CheckBox", label = "Checkbox", //
            modelAttribute = BasicElementsLayoutBehaviorModelObject.PROPERTY_BOOLEANVALUE, //
            required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
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

    public boolean isBooleanValueEnabled() {
        return isAllElementsEnabled();
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

    @UIButton(position = 150, caption = "I am a Button", label = "Button", visible = VisibleType.DYNAMIC,
            enabled = EnabledType.DYNAMIC)
    public void button() {
        Notification.show("Button clicked", 1000, Position.MIDDLE);
    }

    public boolean isButtonVisible() {
        return isAllElementsVisible();
    }

    public boolean isButtonEnabled() {
        return isAllElementsEnabled();
    }


}
