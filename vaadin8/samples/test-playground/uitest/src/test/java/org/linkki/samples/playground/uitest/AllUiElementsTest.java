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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.linkki.test.matcher.Matchers.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import org.junit.Test;
import org.linkki.samples.playground.allelements.AllUiElementsModelObject;
import org.linkki.samples.playground.allelements.Suit;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.DateFieldElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
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
    }

    @Test
    public void testComboBox() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id(AllUiElementsModelObject.PROPERTY_ENUMVALUE);
        assertThat(comboBox.getValue(), is(Suit.SPADES.getName()));

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
        assertThat(checkBox.isChecked());

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
    public void testHorizontalLayout_Id() {
        boolean layoutExists = $(HorizontalLayoutElement.class).state("id", "HorizontalLayoutPmo").exists();
        assertThat(layoutExists, is(true));
    }

    @Test
    public void testVerticalLayout_Id() {
        boolean layoutExists = $(VerticalLayoutElement.class).state("id", "VerticalLayoutPmo").exists();
        assertThat(layoutExists, is(true));
    }
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

}
