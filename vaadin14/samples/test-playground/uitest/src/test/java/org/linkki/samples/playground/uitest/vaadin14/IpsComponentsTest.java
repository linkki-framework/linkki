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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ips.DecimalFieldPmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class IpsComponentsTest extends AbstractUiTest {

    @BeforeEach
    public void setTab() {
        openTab(PlaygroundApplicationView.IPS_TAB_ID);
    }

    @Test
    public void testDecimalField_Format() {
        TextFieldElement field = getTextField("decimal");
        field.setValue("");

        field.sendKeys("12345,6789\t");

        assertThat(field.getValue(), is("12.345,6789"));
        // decimalWithNoDecimalPlaces rounds up
        assertThat(getTextField("decimalWithNoDecimalPlaces").getValue(), is("12.346"));
        assertThat(getTextField("decimalWithThousandsSeparator").getValue(), is("1.23.45,6789"));
    }

    @Test
    public void testDecimalField_RestrictedInput() {
        TextFieldElement field = getTextField("decimal");
        field.setValue("");

        field.sendKeys("a-bc123!#.xyz456?,q789\t");

        assertThat(field.getValue(), is("-123.456,789"));
    }

    @Test
    public void testDecimalField() {
        TextFieldElement decimalField = getTextField("decimal");
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
    public void testDecimalField_RejectsInvalidCharacters() {
        TextFieldElement decimalField = getTextField("decimal");
        decimalField.setValue("1,00");

        waitUntil(ExpectedConditions.elementToBeClickable(decimalField));

        decimalField.sendKeys("abc2!.,3xyz4");

        assertThat(decimalField.getValue(), is("1,002.,34"));
    }

    private TextFieldElement getTextField(String id) {
        return $(VerticalLayoutElement.class).id(DecimalFieldPmo.class.getSimpleName()).$(TextFieldElement.class)
                .id(id);
    }


}
