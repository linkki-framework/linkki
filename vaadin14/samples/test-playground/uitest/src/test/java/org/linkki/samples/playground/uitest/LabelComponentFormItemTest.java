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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.AllUiElementsUiSectionPmo;
import org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.RequiredCaptionProvider;
import org.linkki.samples.playground.allelements.AllUiElementsModelObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class LabelComponentFormItemTest extends AbstractUiTest {

    @Test
    public void testTextField_DynamicRequired() {
        ComboBoxElement requiredField = $(ComboBoxElement.class)
                .id(AllUiElementsUiSectionPmo.PROPERTY_ALL_ELEMENTS_REQUIRED);
        TextFieldElement textField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_TEXT);
        WebElement formItem = getFormItem(AllUiElementsModelObject.PROPERTY_TEXT);

        requiredField.selectByText(RequiredCaptionProvider.REQUIRED);

        assertThat(formItem.getAttribute("required"), is(notNullValue()));
        assertThat(textField.hasAttribute("required"), is(true));

        requiredField.selectByText(RequiredCaptionProvider.NOT_REQUIRED);

        assertThat(formItem.getAttribute("required"), is(nullValue()));
        assertThat(textField.hasAttribute("required"), is(false));
    }

    @Test
    public void testTextField_Required_Empty() {
        ComboBoxElement requiredField = $(ComboBoxElement.class)
                .id(AllUiElementsUiSectionPmo.PROPERTY_ALL_ELEMENTS_REQUIRED);
        TextFieldElement textField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_TEXT);
        WebElement formItem = getFormItem(AllUiElementsModelObject.PROPERTY_TEXT);

        requiredField.selectByText(RequiredCaptionProvider.REQUIRED);

        assertThat(formItem.getAttribute("invalid"), is(nullValue()));
        assertThat(textField.hasAttribute("invalid"), is(false));

        textField.setValue("");

        assertThat(formItem.getAttribute("invalid"), is(notNullValue()));
        assertThat(textField.hasAttribute("invalid"), is(true));

        textField.setValue("something");

        assertThat(formItem.getAttribute("invalid"), is(nullValue()));
        assertThat(textField.hasAttribute("invalid"), is(false));
    }

    private WebElement getFormItem(String componentId) {
        return findElements(By.cssSelector("vaadin-form-item")).stream()
                .filter(e -> !e.findElements(By.id(componentId)).isEmpty()).findFirst().get();
    }
}
