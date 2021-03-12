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

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;

public class AllUiElementsTest extends AbstractUiTest {

    @Test
    public void testCustomField() {
        PasswordFieldElement customField = $(PasswordFieldElement.class).id(AllUiElementsModelObject.PROPERTY_SECRET);
        assertThat(customField.getValue(), is("secret"));

        customField.sendKeys("!\t");
        assertThat(customField.getValue(), is("secret!"));
    }

    @Test
    public void testLabelWithConverter() {
        DivElement label = $(DivElement.class).id("bigDecimalLabel");
        // because Vaadin's StringToBigDecimalConverter uses NumberFormat,
        // which uses BigDecimal#doubleValue(), we get a rounded value
        assertThat(label.getText(), is("12.345,679"));
    }

}
