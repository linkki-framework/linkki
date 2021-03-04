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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.allelements.AllUiElementsModelObject;
import org.linkki.samples.playground.uitest.extensions.DriverExtension;

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

@DriverExtension.Configuration(locale = "en")
public class AllUiElementsL10nUsTest extends AbstractUiTest {

    @Test
    public void testDoubleField() {
        TextFieldElement doubleField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_DOUBLEVALUE);
        assertThat(doubleField.getValue(), is("47.11"));

        doubleField.sendKeys("1");
        assertThat(doubleField.getValue(), is("47.111"));

        doubleField.sendKeys("x");
        assertThat(doubleField.getValue(), is("47.111x"));
        // tab out to lose focus
        doubleField.sendKeys("\t");
        assertThat(doubleField.getValue(), is("47.111"));

        doubleField.setValue("");
        doubleField.sendKeys("1,2345");
        assertThat(doubleField.getValue(), is("1,2345"));
        // tab out to lose focus
        doubleField.sendKeys("\t");
        assertThat(doubleField.getValue(), is("12,345.00"));
    }

    @Test
    public void testLabelWithConverter() {
        DivElement label = $(DivElement.class).id("bigDecimalLabel");
        // because Vaadin's StringToBigDecimalConverter uses NumberFormat,
        // which uses BigDecimal#doubleValue(), we get a rounded value
        assertThat(label.getText(), is("12,345.679"));
    }

    @Test
    public void testDecimalField() {
        TextFieldElement decimalField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_DECIMALVALUE);
        assertThat(decimalField.getValue(), is("12,345.6789"));

        decimalField.sendKeys("6");
        assertThat(decimalField.getValue(), is("12,345.67896"));

        decimalField.sendKeys("x");
        assertThat(decimalField.getValue(), is("12,345.67896x"));
        // tab out to lose focus
        decimalField.sendKeys("\t");
        // Rounding, because NumberFormat#parse returns Double and we use it in the
        // FormattedNumberToStringConverter...
        assertThat(decimalField.getValue(), is("12,345.679"));

        decimalField.setValue("");
        decimalField.sendKeys("1,2345");
        assertThat(decimalField.getValue(), is("1,2345"));
        // tab out to lose focus
        decimalField.sendKeys("\t");
        assertThat(decimalField.getValue(), is("12,345.00"));
    }

}
