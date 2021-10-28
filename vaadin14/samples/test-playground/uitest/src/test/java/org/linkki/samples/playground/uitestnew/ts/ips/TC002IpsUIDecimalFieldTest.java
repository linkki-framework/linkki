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

package org.linkki.samples.playground.uitestnew.ts.ips;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.LinkkiSectionElement;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class TC002IpsUIDecimalFieldTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;
    private LinkkiSectionElement section;

    @BeforeEach
    void setup() {
        super.setUp();
        testCaseSection = goToTestCase(PlaygroundApplicationView.TS004, PlaygroundApplicationView.TC002);
        section = testCaseSection.getContentWrapper().$(LinkkiSectionElement.class).first();
    }

    @Test
    void testDecimalField_Format() {
        TextFieldElement field = getDecimalField("decimal");
        field.setValue("");

        field.sendKeys("12345,6789\t");

        assertThat(field.getValue(), is("12.345,6789"));
        // decimalWithNoDecimalPlaces rounds up
        assertThat(getDecimalField("decimalWithNoDecimalPlaces").getValue(), is("12.346"));
        assertThat(getDecimalField("decimalWithThousandsSeparator").getValue(), is("1.23.45,6789"));
    }

    @Test
    void testDecimalField_RestrictedInput() {
        TextFieldElement field = getDecimalField("decimal");
        field.setValue("");

        field.sendKeys("a-bc123!#.xyz456?,q789\t");

        assertThat(field.getValue(), is("-123.456,789"));
    }

    @Test
    void testDecimalField() {
        TextFieldElement decimalField = getDecimalField("decimal");
        decimalField.setValue("12.345,6789");

        decimalField.sendKeys("6");
        assertThat(decimalField.getValue(), is("12.345,67896"));

        // tab out to lose focus
        decimalField.sendKeys("\t");
        // Rounding, because NumberFormat#parse returns Double and we use it in the
        // FormattedNumberToStringConverter...
        assertThat(decimalField.getValue(), is("12.345,679"));

        decimalField.setValue("");
        decimalField.sendKeys("1.2345");
        assertThat(decimalField.getValue(), is("1.2345"));
        // tab out to lose focus
        decimalField.sendKeys("\t");
        assertThat(decimalField.getValue(), is("12.345,00"));
    }

    @Test
    void testDecimalField_RejectsInvalidCharacters() {
        TextFieldElement decimalField = getDecimalField("decimal");
        decimalField.setValue("1,00");

        waitUntil(ExpectedConditions.elementToBeClickable(decimalField));

        decimalField.sendKeys("abc2!.,3xyz4");

        assertThat(decimalField.getValue(), is("1,002.,34"));
    }

    @Test
    void testDecimalField_0_ShouldBeValidRange() {
        TextFieldElement field = getDecimalField("decimal");

        field.setValue("0");
        field.sendKeys("\t");

        assertThat(field.getValue(), is("0,00"));
    }

    @Test
    void testDecimalField_100_ShouldBeValidRange() {
        TextFieldElement field = getDecimalField("decimal");

        field.setValue("100");
        field.sendKeys("\t");

        assertThat(field.getValue(), is("100,00"));
    }

    @Test
    void testDecimalField_150_ShouldBeInvalidRange() {
        TextFieldElement field = getDecimalField("decimal");

        field.setValue("150");
        field.sendKeys("\t");

        assertThat(field.getValue(), is("150,00"));
        assertThat(containsMessageError(field), is(true));
    }

    @Test
    void testDecimalField_NegativeValue_ShouldBeInvalidRange() {
        TextFieldElement field = getDecimalField("decimal");

        field.setValue("-1");
        field.sendKeys("\t");

        assertThat(field.getValue(), is("-1,00"));
        assertThat(containsMessageError(field), is(true));
    }

    private TextFieldElement getDecimalField(String id) {
        return section.$(TextFieldElement.class).id(id);
    }

    private boolean containsMessageError(TextFieldElement field) {
        return field.$(DivElement.class).all().stream().map(e -> e.getAttribute("part")).filter(Objects::nonNull)
                .anyMatch(p -> p.equals("error-message"));
    }
}
