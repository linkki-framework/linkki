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

package org.linkki.samples.playground.uitest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.samples.playground.allelements.AllUiElementsModelObject;
import org.linkki.samples.playground.allelements.DynamicFieldPmo;
import org.linkki.samples.playground.allelements.Suit;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.DateFieldElement;
import com.vaadin.testbench.elements.FormLayoutElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.RadioButtonGroupElement;
import com.vaadin.testbench.elements.TextAreaElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

public class AllUiElementsTest extends AbstractUiTest {

    @Test
    public void testTextField() {
        TextFieldElement textField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_TEXT);
        assertThat(textField.getValue(), is("foo"));

        textField.sendKeys("bar");
        assertThat(textField.getValue(), is("foobar"));
    }

    @Test
    public void testTextArea() {
        TextAreaElement textArea = $(TextAreaElement.class).id(AllUiElementsModelObject.PROPERTY_LONGTEXT);
        assertThat(textArea.getValue(), startsWith("Lorem ipsum"));

        textArea.clear();
        textArea.sendKeys("bla bla");
        assertThat(textArea.getValue(), is("bla bla"));
    }

    @Test
    public void testIntegerField() {
        TextFieldElement integerField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_INTVALUE);
        assertThat(integerField.getValue(), is("42"));

        integerField.sendKeys("1");
        assertThat(integerField.getValue(), is("421"));

        integerField.sendKeys("x");
        assertThat(integerField.getValue(), is("421x"));
        // tab out to lose focus
        integerField.sendKeys("\t");
        assertThat(integerField.getValue(), is("421"));
    }

    @Test
    public void testDoubleField() {
        TextFieldElement doubleField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_DOUBLEVALUE);
        assertThat(doubleField.getValue(), is("47,11"));

        doubleField.sendKeys("1");
        assertThat(doubleField.getValue(), is("47,111"));

        doubleField.sendKeys("x");
        assertThat(doubleField.getValue(), is("47,111x"));
        // tab out to lose focus
        doubleField.sendKeys("\t");
        assertThat(doubleField.getValue(), is("47,111"));

        doubleField.clear();
        doubleField.sendKeys("1.2345");
        assertThat(doubleField.getValue(), is("1.2345"));
        // tab out to lose focus
        doubleField.sendKeys("\t");
        assertThat(doubleField.getValue(), is("12.345,00"));
    }

    @Test
    public void testDateField() {
        DateFieldElement dateField = $(DateFieldElement.class).id(AllUiElementsModelObject.PROPERTY_DATE);
        assertThat(dateField.getValue(),
                   is(LocalDate.now()
                           .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMANY))));

        dateField.setDate(LocalDate.of(2019, 1, 1));
        assertThat(dateField.getValue(), is("01.01.2019"));

        dateField.setValue("2.3.4");
        assertThat(dateField.getValue(), is("02.03.2004"));

        dateField.setValue("01042020");
        assertThat(dateField.getValue(), is("01.04.2020"));

        dateField.setValue("111111");
        assertThat(dateField.getValue(), is("11.11.2011"));
    }

    @Test
    public void testComboBox() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("enumValueComboBox");

        assertThat(comboBox.getValue(), is(""));

        comboBox.selectByText(Suit.HEARTS.getName());
        assertThat(comboBox.getValue(), is(Suit.HEARTS.getName()));

        comboBox.sendKeys("x");
        assertThat(comboBox.getValue(), is(Suit.HEARTS.getName() + "x"));
        comboBox.sendKeys("\t");
        assertThat(comboBox.getValue(), is(Suit.HEARTS.getName()));
    }

    @Test
    public void testCheckBox() {
        CheckBoxElement checkBox = $(CheckBoxElement.class).id(AllUiElementsModelObject.PROPERTY_BOOLEANVALUE);
        assertThat(checkBox.isChecked(), is(true));

        checkBox.click();
        assertThat(checkBox.isChecked(), is(false));
    }

    @Test
    public void testCustomField() {
        PasswordFieldElement customField = $(PasswordFieldElement.class).id(AllUiElementsModelObject.PROPERTY_SECRET);
        assertThat(customField.getValue(), is("secret"));

        customField.sendKeys("!");
        assertThat(customField.getValue(), is("secret!"));
    }

    @Test
    public void testLabel() {
        LabelElement label = $(LabelElement.class).id("textLabel");
        assertThat(label.getText(), is("secret"));

        PasswordFieldElement customField = $(PasswordFieldElement.class).id(AllUiElementsModelObject.PROPERTY_SECRET);
        customField.sendKeys("!\t");
        assertThat(label.getText(), is("secret!"));
    }

    @Test
    public void testLabelWithConverter() {
        LabelElement label = $(LabelElement.class).id("bigDecimalLabel");
        // because Vaadin's StringToBigDecimalConverter uses NumberFormat,
        // which uses BigDecimal#doubleValue(), we get a rounded value
        assertThat(label.getText(), is("12.345,679"));
    }

    @Test
    public void testButton() {
        ButtonElement button = $(ButtonElement.class).id("action");
        TextFieldElement integerField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_INTVALUE);
        assertThat(integerField.getValue(), is("42"));

        button.click();

        assertThat(integerField.getValue(), is("43"));
    }

    @Test
    public void testRadioButton() {
        RadioButtonGroupElement radioButtons = $(RadioButtonGroupElement.class).id("enumValueRadioButton");

        assertThat(radioButtons.getValue(), is(nullValue()));

        radioButtons.selectByText(Suit.HEARTS.getName());

        assertThat(radioButtons.getValue(), is(Suit.HEARTS.getName()));
    }

    @Test
    public void testHorizontalLayout_Id() {
        HorizontalLayoutElement horizontalLayout = $(HorizontalLayoutElement.class).id("HorizontalLayoutPmo");
        assertThat("Caption of UIHorizontalLayout is bindable", horizontalLayout.getCaption(),
                   is("UIHorizontalLayout"));
        assertThat(horizontalLayout.$(TextFieldElement.class).all().size(), is(1));
        assertThat(horizontalLayout.$(LabelElement.class).all().size(), is(1));
        assertThat(horizontalLayout.$(ButtonElement.class).all().size(), is(1));
        assertThat(horizontalLayout.$(CheckBoxElement.class).all().size(), is(1));
    }

    @Test
    public void testVerticalLayout_Id() {
        VerticalLayoutElement verticalLayout = $(VerticalLayoutElement.class).id("VerticalLayoutPmo");
        assertThat("Caption of UIVerticalLayout is bindable", verticalLayout.getCaption(),
                   is("UIVerticalLayout"));
        assertThat(verticalLayout.$(TextFieldElement.class).all().size(), is(1));
        assertThat(verticalLayout.$(LabelElement.class).all().size(), is(1));
        assertThat(verticalLayout.$(ButtonElement.class).all().size(), is(1));
        assertThat(verticalLayout.$(CheckBoxElement.class).all().size(), is(1));
    }

    @Test
    public void testFormLayout() {
        FormLayoutElement formLayout = $(FormLayoutElement.class).id("FormLayoutPmo");
        assertThat("Caption of UIFormLayout is bindable", formLayout.getCaption(),
                   is("UIFormLayout"));
        assertThat(formLayout.$(TextFieldElement.class).all().size(), is(2));
        assertThat(formLayout.$(LabelElement.class).all().size(), is(1));
        assertThat(formLayout.$(LabelElement.class).all().size(), is(1));
    }

    @Test
    public void testDecimalField() {
        TextFieldElement decimalField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_DECIMALVALUE);
        assertThat(decimalField.getValue(), is("12.345,6789"));

        decimalField.sendKeys("6");
        assertThat(decimalField.getValue(), is("12.345,67896"));

        decimalField.sendKeys("x");
        assertThat(decimalField.getValue(), is("12.345,67896x"));
        // tab out to lose focus
        decimalField.sendKeys("\t");
        // Rounding, because NumberFormat#parse returns Double and we use it in the
        // FormattedNumberToStringConverter...
        assertThat(decimalField.getValue(), is("12.345,679"));

        decimalField.clear();
        decimalField.sendKeys("1.2345");
        assertThat(decimalField.getValue(), is("1.2345"));
        // tab out to lose focus
        decimalField.sendKeys("\t");
        assertThat(decimalField.getValue(), is("12.345,00"));
    }

    @Test
    public void testDynamicField() {
        DynamicFieldPmo.FieldTypeCaptionProvider fieldTypeCaptionProvider = new DynamicFieldPmo.FieldTypeCaptionProvider();
        ComboBoxElement typeComboBox = $(ComboBoxElement.class).id(DynamicFieldPmo.PROPERTY_TYPE);
        assertThat(typeComboBox.getValue(), is(fieldTypeCaptionProvider.getCaption(UIComboBox.class)));

        ComboBoxElement valueComboBox = $(ComboBoxElement.class).id(DynamicFieldPmo.PROPERTY_VALUE);
        assertThat(valueComboBox.getValue(), is("foo"));


        typeComboBox.selectByText(fieldTypeCaptionProvider.getCaption(UITextField.class));

        TextFieldElement valueTextField = $(TextFieldElement.class).id(DynamicFieldPmo.PROPERTY_VALUE);
        assertThat(valueTextField.getValue(), is("foo"));

        valueTextField.sendKeys("bar");
        assertThat(valueTextField.getValue(), is("foobar"));

        typeComboBox = $(ComboBoxElement.class).id(DynamicFieldPmo.PROPERTY_TYPE);
        typeComboBox.selectByText(fieldTypeCaptionProvider.getCaption(UIComboBox.class));

        valueComboBox = $(ComboBoxElement.class).id(DynamicFieldPmo.PROPERTY_VALUE);
        assertThat(valueComboBox.getValue(), is("foobar"));
    }

}
