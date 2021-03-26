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

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.allelements.AllUiElementsModelObject;
import org.linkki.samples.playground.allelements.Direction;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.html.testbench.AnchorElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.radiobutton.testbench.RadioButtonGroupElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextAreaElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

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

        textArea.setValue("");
        textArea.sendKeys("bla bla");
        assertThat(textArea.getValue(), is("bla bla"));
    }

    @Test
    public void testCheckBox() {
        CheckboxElement checkBox = $(CheckboxElement.class).id(AllUiElementsModelObject.PROPERTY_BOOLEANVALUE);
        assertThat(checkBox.isChecked(), is(true));

        checkBox.click();
        assertThat(checkBox.isChecked(), is(false));
    }

    @Test
    public void testCustomField() {
        PasswordFieldElement customField = $(PasswordFieldElement.class).id(AllUiElementsModelObject.PROPERTY_SECRET);
        assertThat(customField.getValue(), is("secret"));

        customField.sendKeys("!\t");
        assertThat(customField.getValue(), is("secret!"));
    }

    @Test
    public void testLabel() {
        DivElement label = $(DivElement.class).id("textLabel");
        assertThat(label.getText(), is("secret"));
    }

    @Test
    public void testLabelWithConverter() {
        DivElement label = $(DivElement.class).id("bigDecimalLabel");
        // because Vaadin's StringToBigDecimalConverter uses NumberFormat,
        // which uses BigDecimal#doubleValue(), we get a rounded value
        assertThat(label.getText(), is("12.345,679"));
    }

    @Test
    public void testRadioButton() {
        RadioButtonGroupElement radioButtons = $(RadioButtonGroupElement.class).id("enumValueRadioButton");

        assertThat(radioButtons.getSelectedText(), is(nullValue()));

        radioButtons.selectByText(Direction.LEFT.getName());

        assertThat(radioButtons.getSelectedText(), is(Direction.LEFT.getName()));
    }

    @Test
    public void testLink() {
        AnchorElement link = $(AnchorElement.class).id("link");

        assertThat(link.getText(), is("Link to Dynamic Annotations"));
        assertThat(link.getAttribute("href"), endsWith("sheet=Dynamic%20Annotations"));
    }

}
