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

package org.linkki.samples.playground.uitestnew.ts.components;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.components.DoubleFieldPmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class TC003UIDoubleFieldTest extends PlaygroundUiTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        goToTestCase(PlaygroundApplicationView.TS005, PlaygroundApplicationView.TC003);
    }

    @Test
    void testDoubleField_PrimitiveDouble() {
        TextFieldElement field = getTextField("primitiveDouble");
        field.setValue("123,45");

        field.setValue("");
        field.sendKeys("\t");

        // field resets to previous value
        assertThat(field.getValue(), is("123,45"));
    }

    @Test
    void testDoubleField_BoxedDouble() {
        TextFieldElement field = getTextField("boxedDouble");
        field.setValue("123,45");

        field.setValue("");
        field.sendKeys("\t");

        assertThat(field.getValue(), is(""));
    }

    @Test
    void testDoubleField_RestrictedInput() {
        TextFieldElement field = getTextField("boxedDouble");
        field.setValue("");

        field.sendKeys("a-bc123!#.xyz456?,q789\t");

        assertThat(field.getValue(), is("-123.456,789"));
    }

    @Test
    void testDoubleField() {
        TextFieldElement doubleField = getTextField("primitiveDouble");
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
    void testDoubleField_RejectsInvalidCharacters() {
        TextFieldElement doubleField = getTextField("primitiveDouble");
        doubleField.setValue("1,00");

        doubleField.sendKeys("abc2!.,3xyz4");

        assertThat(doubleField.getValue(), is("1,002.,34"));
    }

    private TextFieldElement getTextField(String id) {
        return getSection(DoubleFieldPmo.class)
                .$(TextFieldElement.class).id(id);
    }

}
