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

package org.linkki.samples.playground.uitest.vaadin14;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.allelements.AllUiElementsModelObject;
import org.linkki.samples.playground.allelements.NumberFieldsPmo;
import org.linkki.samples.playground.uitest.AbstractUiTest;

import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class NumberFieldTest extends AbstractUiTest {

    @Test
    public void testIntegerField_PrimitiveInteger() {
        TextFieldElement field = getTextField("primitiveInteger");
        field.setValue("123");

        field.setValue("");
        field.sendKeys("\t");

        // field resets to previous value
        assertThat(field.getValue(), is("123"));
    }

    @Test
    public void testIntegerField_BoxedInteger() {
        TextFieldElement field = getTextField("boxedInteger");
        field.setValue("123");

        field.setValue("");
        field.sendKeys("\t");

        assertThat(field.getValue(), is(""));
    }

    @Test
    public void testIntegerField_RestrictedInput() {
        TextFieldElement field = getTextField("boxedInteger");
        field.setValue("");

        field.sendKeys("a-bc123!#.xyz456?,q789\t");

        assertThat(field.getValue(), is("-123.456.789"));
    }

    @Test
    public void testIntegerField() {
        TextFieldElement integerField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_INTVALUE);
        integerField.setValue("42");

        integerField.sendKeys("1");
        assertThat(integerField.getValue(), is("421"));

        // tab out to lose focus
        integerField.sendKeys("\t");
        assertThat(integerField.getValue(), is("421"));
    }

    @Test
    public void testIntegerField_RejectsInvalidCharacters() {
        TextFieldElement integerField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_INTVALUE);
        integerField.setValue("1");

        integerField.sendKeys("abc2!.,3xyz4");

        assertThat(integerField.getValue(), is("1234"));
    }

    @Test
    public void testDoubleField_PrimitiveDouble() {
        TextFieldElement field = getTextField("primitiveDouble");
        field.setValue("123,45");

        field.setValue("");
        field.sendKeys("\t");

        // field resets to previous value
        assertThat(field.getValue(), is("123,45"));
    }

    @Test
    public void testDoubleField_BoxedDouble() {
        TextFieldElement field = getTextField("boxedDouble");
        field.setValue("123,45");

        field.setValue("");
        field.sendKeys("\t");

        assertThat(field.getValue(), is(""));
    }

    @Test
    public void testDoubleField_RestrictedInput() {
        TextFieldElement field = getTextField("boxedDouble");
        field.setValue("");

        field.sendKeys("a-bc123!#.xyz456?,q789\t");

        assertThat(field.getValue(), is("-123.456,789"));
    }

    @Test
    public void testDoubleField() {
        TextFieldElement doubleField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_DOUBLEVALUE);
        doubleField.setValue("47,11");

        doubleField.sendKeys("1");
        assertThat(doubleField.getValue(), is("47,111"));

        // tab out to lose focus
        doubleField.sendKeys("\t");
        assertThat(doubleField.getValue(), is("47,111"));

        doubleField.setValue("1.2345");
        // tab out to lose focus
        doubleField.sendKeys("\t");
        assertThat(doubleField.getValue(), is("12.345,00"));
    }

    @Test
    public void testDoubleField_RejectsInvalidCharacters() {
        TextFieldElement doubleField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_DOUBLEVALUE);
        doubleField.setValue("1,00");

        doubleField.sendKeys("abc2!.,3xyz4");

        assertThat(doubleField.getValue(), is("1,002.,34"));
    }

    private TextFieldElement getTextField(String id) {
        return $(VerticalLayoutElement.class).id(NumberFieldsPmo.class.getSimpleName()).$(TextFieldElement.class)
                .id(id);
    }


}
