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
package org.linkki.samples.playground.uitestnew.ts.components;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.components.LongFieldPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class TC018UILongFieldTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC018);
    }

    @Test
    void testLongField_PrimitiveLong() {
        TextFieldElement longField = getTextField("primitiveLong");
        longField.setValue("42");

        longField.sendKeys("1");
        assertThat(longField.getValue()).isEqualTo("421");

        longField.sendKeys("\t");
        assertThat(longField.getValue()).isEqualTo("421");
    }

    @Test
    void testLongField_BoxedLong() {
        TextFieldElement field = getTextField("boxedLong");
        field.setValue("123");

        field.setValue("");
        field.sendKeys("\t");

        assertThat(field.getValue()).isBlank();
    }

    @Test
    void testLongField_PrimitiveLongResetsOnInvalidValue() {
        TextFieldElement field = getTextField("primitiveLong");
        field.setValue("123");

        field.setValue("");
        field.sendKeys("\t");

        assertThat(field.getValue()).isEqualTo("123");
    }

    @Test
    void testLongField_PrimitiveLongRejectsInvalidCharacters() {
        TextFieldElement longField = getTextField("primitiveLong");
        longField.setValue("1");

        longField.sendKeys("abc2!.,3xyz4" + Keys.TAB);

        assertThat(longField.getValue()).isEqualTo("1.234");
    }

    @Test
    void testLongField_BoxedLongRejectsInvalidCharacters() {
        TextFieldElement field = getTextField("boxedLong");
        field.setValue("");

        field.sendKeys("a-bc123!#.xyz456?,q7891s01s1" + Keys.TAB);

        assertThat(field.getValue()).isEqualTo("-1.234.567.891.011");
    }

    @Test
    void testLongField_UserDefinedFormat() {
        // This field has a format with no thousands separator
        TextFieldElement field = getTextField("userFormattedLong");
        field.setValue("");

        field.sendKeys("a-bc1110987!#.xyz654?,q321" + Keys.TAB);

        assertThat(field.getValue()).isEqualTo("-1110987654321");
    }

    private TextFieldElement getTextField(String id) {
        return getSection(LongFieldPmo.class).$(TextFieldElement.class).id(id);
    }

}
