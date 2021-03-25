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

import java.util.Optional;

import org.linkki.core.defaults.ui.aspects.types.AlignmentType;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
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
import org.linkki.core.ui.layout.annotation.UIFormSection;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.ips.decimalfield.UIDecimalField;
import org.linkki.samples.playground.dynamicannotations.DynamicAnnotationsLayout;
import org.linkki.samples.playground.nls.NlsText;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;

public abstract class AbstractAllUiElementsSectionPmo {

    public static final String CSS_NAME = "playground";
    public static final String PROPERTY_ALL_ELEMENTS_REQUIRED = "allElementsRequired";

    private final AllUiElementsModelObject modelObject = new AllUiElementsModelObject();

    private boolean readOnly;
    private boolean required = false;

    @ModelObject
    public AllUiElementsModelObject getModelObject() {
        return modelObject;
    }

    @SectionHeader
    @UIComboBox(position = -90, label = "", width = "9em", //
            content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, itemCaptionProvider = RequiredCaptionProvider.class)
    public boolean isAllElementsRequired() {
        return required;
    }

    public void setAllElementsRequired(boolean required) {
        this.required = required;
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UITextField(position = 10, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_TEXT, required = RequiredType.DYNAMIC)
    public void text() {
        // model binding
    }

    public boolean isTextRequired() {
        return required;
    }

    public boolean isTextReadOnly() {
        return isReadOnly();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UITextArea(position = 20, height = "5em", label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_LONGTEXT, required = RequiredType.DYNAMIC)
    public void longText() {
        // model binding
    }

    public boolean isLongTextRequired() {
        return required;
    }

    public boolean isLongTextReadOnly() {
        return isReadOnly();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIIntegerField(position = 30, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_INTVALUE, required = RequiredType.DYNAMIC)
    public void intValue() {
        // model binding
    }

    public boolean isIntValueRequired() {
        return required;
    }

    public boolean isIntValueReadOnly() {
        return isReadOnly();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDoubleField(position = 40, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_DOUBLEVALUE, required = RequiredType.DYNAMIC)
    public void doubleValue() {
        // model binding
    }

    public boolean isDoubleValueRequired() {
        return required;
    }

    public boolean isDoubleValueReadOnly() {
        return isReadOnly();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDateField(position = 50, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_DATE, required = RequiredType.DYNAMIC)
    public void date() {
        // model binding
    }

    public boolean isDateRequired() {
        return required;
    }

    public boolean isDateReadOnly() {
        return isReadOnly();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIComboBox(position = 60, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_ENUMVALUE, required = RequiredType.DYNAMIC)
    public void enumValueComboBox() {
        // model binding
    }

    public boolean isEnumValueComboBoxRequired() {
        return required;
    }

    public boolean isEnumValueComboBoxReadOnly() {
        return isReadOnly();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UICheckBox(position = 70, caption = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_BOOLEANVALUE, required = RequiredType.DYNAMIC)
    public void booleanValue() {
        // model binding
    }

    public boolean isBooleanValueRequired() {
        return required;
    }

    public boolean isBooleanValueReadOnly() {
        return isReadOnly();
    }

    @UILabel(position = 80, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_SECRET, styleNames = {
            "firstStyleName", "anotherStyleName" })
    public void textLabel() {
        // model binding
    }

    /**
     * No label in Nls to test default behavior of no label
     */
    @UILabel(position = 81, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_BIG_DECIMAL)
    public void bigDecimalLabel() {
        // model binding
    }

    @UILabel(position = 82, label = NlsText.I18n, htmlContent = true)
    public String getHtmlContentLabel() {
        return "<i style=\"color: red;\">HTML</i> <b>Content</b>";
    }

    @UILabel(position = 83, label = NlsText.I18n)
    public String getNotHtmlContentLabel() {
        return "<b>NOT</b> HTML Content";
    }

    @BindIcon(value = VaadinIcon.ABACUS)
    @UILabel(position = 84, label = NlsText.I18n, htmlContent = true)
    public String getNotHtmlContentLabelMitIcon() {
        return "<i style=\\\"color: red;\\\">HTML</i> <b>Content</b> mit Icon";
    }

    @BindIcon(value = VaadinIcon.ABACUS)
    @UILabel(position = 85, label = NlsText.I18n)
    public String getHtmlContentLabelMitIcon() {
        return "<div><b>NOT</b> HTML Content mit Icon</div>";
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UICustomField(position = 90, label = NlsText.I18n, uiControl = PasswordField.class, modelAttribute = AllUiElementsModelObject.PROPERTY_SECRET, required = RequiredType.DYNAMIC)
    public void secret() {
        // model binding
    }

    public boolean isSecretRequired() {
        return required;
    }

    public boolean isSecretReadOnly() {
        return isReadOnly();
    }

    @UINestedComponent(position = 100, label = NlsText.I18n)
    public ButtonPmo buttons() {
        return new ButtonPmo();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDecimalField(position = 110, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_DECIMALVALUE, required = RequiredType.DYNAMIC)
    public void decimalValue() {
        // model binding
    }

    public boolean isDecimalValueRequired() {
        return required;
    }

    public boolean isDecimalValueReadOnly() {
        return isReadOnly();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIRadioButtons(position = 120, label = NlsText.I18n, buttonAlignment = AlignmentType.HORIZONTAL, modelAttribute = AllUiElementsModelObject.PROPERTY_ENUMVALUE, required = RequiredType.DYNAMIC)
    public void enumValueRadioButton() {
        // model binding
    }

    public boolean isEnumValueRadioButtonRequired() {
        return required;
    }

    public boolean isEnumValueRadioButtonReadOnly() {
        return isReadOnly();
    }

    @UICheckBox(position = 130, caption = NlsText.I18n)
    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @BindIcon(iconType = IconType.DYNAMIC)
    @UILink(position = 140, label = NlsText.I18n, caption = "Link to Dynamic Annotations", captionType = CaptionType.STATIC)
    public String getLink() {
        return "main#!/sheet=" + DynamicAnnotationsLayout.ID;
    }

    public VaadinIcon getLinkIcon() {
        return Optional.ofNullable(getModelObject().getEnumValue()).map(Direction::getIcon).orElse(null);
    }

    @UISection(caption = NlsText.I18n)
    @BindStyleNames(AbstractAllUiElementsSectionPmo.CSS_NAME)
    public static class AllUiElementsUiSectionPmo extends AbstractAllUiElementsSectionPmo {
        // no content needed
    }

    @UIFormSection(caption = NlsText.I18n)
    public static class AllUiElementsUiFormSectionPmo extends AbstractAllUiElementsSectionPmo {
        // no content needed
    }

    public static class RequiredCaptionProvider implements ItemCaptionProvider<Boolean> {

        public static final String REQUIRED = "required";
        public static final String NOT_REQUIRED = "not required";

        @Override
        public String getCaption(Boolean required) {
            return required ? REQUIRED : NOT_REQUIRED;
        }

    }
}
