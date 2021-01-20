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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.PROPERTY_BOOLEAN_VALUE;
import static org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.PROPERTY_DATE;
import static org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.PROPERTY_DECIMAL_VALUE;
import static org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.PROPERTY_DOUBLE_VALUE;
import static org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.PROPERTY_ENUMVALUE_RADIO_BUTTON;
import static org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.PROPERTY_ENUM_VALUE_COMBO_BOX;
import static org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.PROPERTY_INT_VALUE;
import static org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.PROPERTY_LONG_TEXT;
import static org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.PROPERTY_READ_ONLY;
import static org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.PROPERTY_SECRET;
import static org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.PROPERTY_TEXT;

import org.junit.jupiter.api.Test;

import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.DateFieldElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.RadioButtonGroupElement;
import com.vaadin.testbench.elements.TextAreaElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class BindReadOnlyTest extends AbstractUiTest {

    @Test
    public void testBindReadOnlyBehavior() {
        $(CheckBoxElement.class).id(PROPERTY_READ_ONLY).click();
        assertThat($(TextFieldElement.class).id(PROPERTY_TEXT).isReadOnly(), is(true));
        assertThat($(TextAreaElement.class).id(PROPERTY_LONG_TEXT).isReadOnly(), is(true));
        assertThat($(TextFieldElement.class).id(PROPERTY_INT_VALUE).isReadOnly(), is(true));
        assertThat($(TextFieldElement.class).id(PROPERTY_DOUBLE_VALUE).isReadOnly(), is(true));
        assertThat($(ComboBoxElement.class).id(PROPERTY_ENUM_VALUE_COMBO_BOX).isReadOnly(), is(true));
        assertThat($(CheckBoxElement.class).id(PROPERTY_BOOLEAN_VALUE).isReadOnly(), is(true));
        assertThat($(PasswordFieldElement.class).id(PROPERTY_SECRET).isReadOnly(), is(true));
        assertThat($(TextFieldElement.class).id(PROPERTY_DECIMAL_VALUE).isReadOnly(), is(true));
        assertThat($(DateFieldElement.class).id(PROPERTY_DATE).isReadOnly(), is(true));
        assertThat($(RadioButtonGroupElement.class).id(PROPERTY_ENUMVALUE_RADIO_BUTTON).isReadOnly(), is(true));

        $(CheckBoxElement.class).id(PROPERTY_READ_ONLY).click();
        assertThat($(TextFieldElement.class).id(PROPERTY_TEXT).isReadOnly(), is(false));
        assertThat($(TextAreaElement.class).id(PROPERTY_LONG_TEXT).isReadOnly(), is(false));
        assertThat($(TextFieldElement.class).id(PROPERTY_INT_VALUE).isReadOnly(), is(false));
        assertThat($(TextFieldElement.class).id(PROPERTY_DOUBLE_VALUE).isReadOnly(), is(false));
        assertThat($(ComboBoxElement.class).id(PROPERTY_ENUM_VALUE_COMBO_BOX).isReadOnly(), is(false));
        assertThat($(CheckBoxElement.class).id(PROPERTY_BOOLEAN_VALUE).isReadOnly(), is(false));
        assertThat($(PasswordFieldElement.class).id(PROPERTY_SECRET).isReadOnly(), is(false));
        assertThat($(TextFieldElement.class).id(PROPERTY_DECIMAL_VALUE).isReadOnly(), is(false));
        assertThat($(DateFieldElement.class).id(PROPERTY_DATE).isReadOnly(), is(false));
        assertThat($(RadioButtonGroupElement.class).id(PROPERTY_ENUMVALUE_RADIO_BUTTON).isReadOnly(), is(false));
    }
}
