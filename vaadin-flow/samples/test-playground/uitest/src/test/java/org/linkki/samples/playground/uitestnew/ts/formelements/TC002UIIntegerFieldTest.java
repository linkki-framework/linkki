/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.samples.playground.uitestnew.ts.formelements;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.formelements.IntegerFieldPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class TC002UIIntegerFieldTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC002);
    }

    @Test
    void testIntegerField_PrimitiveInteger() {
        TextFieldElement integerField = getTextField("primitiveInteger");
        integerField.setValue("42");

        integerField.sendKeys("1");
        assertThat(integerField.getValue(), is("421"));

        // tab out to lose focus
        integerField.sendKeys("\t");
        assertThat(integerField.getValue(), is("421"));
    }

    @Test
    void testIntegerField_BoxedInteger() {
        TextFieldElement field = getTextField("boxedInteger");
        field.setValue("123");

        field.setValue("");
        field.sendKeys("\t");

        assertThat(field.getValue(), is(""));
    }

    @Test
    void testIntegerField_PrimitiveIntegerResetsOnInvalidValue() {
        TextFieldElement field = getTextField("primitiveInteger");
        field.setValue("123");

        field.setValue("");
        field.sendKeys("\t");

        // field resets to previous value
        assertThat(field.getValue(), is("123"));
    }

    @Test
    void testIntegerField_PrimitiveIntegerRejectsInvalidCharacters() {
        TextFieldElement integerField = getTextField("primitiveInteger");
        integerField.setValue("1");

        integerField.sendKeys("abc2!.,3xyz4" + Keys.TAB);

        assertThat(integerField.getValue(), is("1.234"));
    }

    @Test
    void testIntegerField_BoxedIntegerRejectsInvalidCharacters() {
        TextFieldElement field = getTextField("boxedInteger");
        field.setValue("");

        field.sendKeys("a-bc123!#.xyz456?,q789" + Keys.TAB);

        assertThat(field.getValue(), is("-123.456.789"));
    }

    @Test
    void testIntegerField_UserDefinedFormat() {
        // This field has a format with no thousands separator
        TextFieldElement field = getTextField("userFormattedInteger");
        field.setValue("");

        field.sendKeys("a-bc987!#.xyz654?,q321" + Keys.TAB);

        assertThat(field.getValue(), is("-987654321"));
    }

    private TextFieldElement getTextField(String id) {
        return getSection(IntegerFieldPmo.class).$(TextFieldElement.class).id(id);
    }

}
