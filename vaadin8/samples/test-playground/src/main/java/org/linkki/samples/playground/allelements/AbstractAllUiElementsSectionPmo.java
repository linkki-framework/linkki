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
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
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
import org.linkki.core.ui.element.annotation.UIYesNoComboBox;
import org.linkki.core.ui.layout.annotation.UIFormSection;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.ips.decimalfield.UIDecimalField;
import org.linkki.samples.playground.dynamicannotations.DynamicAnnotationsLayout;
import org.linkki.samples.playground.nls.NlsText;
import org.linkki.samples.playground.ui.PlaygroundView;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.PasswordField;

public abstract class AbstractAllUiElementsSectionPmo {

    public static final String CSS_NAME = "playground";
    public static final String PROPERTY_TEXT = "text";
    public static final String PROPERTY_LONG_TEXT = "longText";
    public static final String PROPERTY_INT_VALUE = "intValue";
    public static final String PROPERTY_DOUBLE_VALUE = "doubleValue";
    public static final String PROPERTY_DATE = "date";
    public static final String PROPERTY_ENUM_VALUE_COMBO_BOX = "enumValueComboBox";
    public static final String PROPERTY_YES_NO_COMBO_BOX = "yesNoComboBox";
    public static final String PROPERTY_BOOLEAN_VALUE = "booleanValue";
    public static final String PROPERTY_TEXT_LABEL = "textLabel";
    public static final String PROPERTY_BIG_DECIMAL_LABEL = "bigDecimalLabel";
    public static final String PROPERTY_SECRET = "secret";
    public static final String PROPERTY_ACTION = "action";
    public static final String PROPERTY_DECIMAL_VALUE = "decimalValue";
    public static final String PROPERTY_ENUMVALUE_RADIO_BUTTON = "enumValueRadioButton";
    public static final String PROPERTY_READ_ONLY = "readOnly";
    public static final String PROPERTY_LINK = "link";

    private final AllUiElementsModelObject modelObject = new AllUiElementsModelObject();

    private boolean readOnly;

    @ModelObject
    public AllUiElementsModelObject getModelObject() {
        return modelObject;
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UITextField(position = 10, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_TEXT, required = RequiredType.DYNAMIC)
    public void text() {
        // model binding
    }

    public boolean isTextRequired() {
        return getModelObject().isBooleanValue();
    }

    public boolean isTextReadOnly() {
        return isReadOnly();
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UITextArea(position = 20, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_LONGTEXT, required = RequiredType.DYNAMIC)
    public void longText() {
        // model binding
    }

    public boolean isLongTextRequired() {
        return getModelObject().isBooleanValue();
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
        return getModelObject().isBooleanValue();
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
        return getModelObject().isBooleanValue();
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
        return getModelObject().isBooleanValue();
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
        return getModelObject().isBooleanValue();
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
        return getModelObject().isBooleanValue();
    }

    public boolean isBooleanValueReadOnly() {
        return isReadOnly();
    }

    @BindIcon(value = VaadinIcons.PLUS)
    @UILabel(position = 80, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_SECRET)
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

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UICustomField(position = 90, label = NlsText.I18n, uiControl = PasswordField.class, modelAttribute = AllUiElementsModelObject.PROPERTY_SECRET, required = RequiredType.DYNAMIC)
    public void secret() {
        // model binding
    }

    public boolean isSecretRequired() {
        return getModelObject().isBooleanValue();
    }

    public boolean isSecretReadOnly() {
        return isReadOnly();
    }

    @UIButton(position = 100, caption = NlsText.I18n)
    public void action() {
        getModelObject().setIntValue(getModelObject().getIntValue() + 1);
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDecimalField(position = 110, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_DECIMALVALUE, required = RequiredType.DYNAMIC)
    public void decimalValue() {
        // model binding
    }

    public boolean isDecimalValueRequired() {
        return getModelObject().isBooleanValue();
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
        return getModelObject().isBooleanValue();
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
        return "#!/" + PlaygroundView.PARAM_SHEET + "=" + DynamicAnnotationsLayout.ID;
    }

    @UILink(position = 141, label = NlsText.I18n, caption = "Link without href", captionType = CaptionType.STATIC)
    public String getLinkWithoutHref() {
        return null;
    }

    public VaadinIcons getLinkIcon() {
        return Optional.ofNullable(getModelObject().getEnumValue()).map(Direction::getIcon).orElse(null);
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIYesNoComboBox(position = 150, label = NlsText.I18n, modelAttribute = AllUiElementsModelObject.PROPERTY_YESNOCOMBOBOX, required = RequiredType.DYNAMIC)
    public void yesNoComboBox() {
        // model binding
    }

    public boolean isYesNoComboBoxRequired() {
        return getModelObject().isBooleanValue();
    }

    public boolean isYesNoComboBoxReadOnly() {
        return isReadOnly();
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
}
