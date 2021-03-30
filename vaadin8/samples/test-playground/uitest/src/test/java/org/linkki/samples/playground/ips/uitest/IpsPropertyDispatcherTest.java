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

package org.linkki.samples.playground.ips.uitest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.samples.playground.ips.EnabledSectionPmo;
import org.linkki.samples.playground.ips.IpsComponent;
import org.linkki.samples.playground.ips.RequiredSectionPmo;
import org.linkki.samples.playground.ips.VisibleSectionPmo;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.openqa.selenium.NoSuchElementException;

import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

public class IpsPropertyDispatcherTest extends AbstractUiTest {

    @BeforeEach
    public void setup() {
        clickButton(IpsComponent.ID);
    }

    @Test
    public void testVisible_NotEmptyValueSet_ShouldBeVisible() {
        VerticalLayoutElement section = getVisibleSection();
        TextFieldElement textField = section.$(TextFieldElement.class).id("notEmptyValueSet");

        textField.scrollIntoView();
        assertThat(textField.isDisplayed(), is(true));
    }

    @Test
    public void testVisible_EmptyValueSet_ShouldNotBeVisible() {
        VerticalLayoutElement section = getVisibleSection();
        assertThrows(NoSuchElementException.class, () -> section.$(ComboBoxElement.class).id("emptyValueSet"));
    }

    @Test
    public void testVisible_DynamicVisibleEmptyValueSet_ShouldBeVisible() {
        VerticalLayoutElement section = getVisibleSection();
        ComboBoxElement textField = section.$(ComboBoxElement.class).id("dynamicVisibleEmptyValueSet");

        textField.scrollIntoView();
        assertThat(textField.isDisplayed(), is(true));
    }

    @Test
    public void testEnabled_NotEmptyValueSet_ShouldBeEnabled() {
        VerticalLayoutElement section = getEnabledSection();
        TextFieldElement textField = section.$(TextFieldElement.class).id("notEmptyValueSet");

        assertThat(textField.isEnabled(), is(true));
    }

    @Test
    public void testEnabled_EmptyValueSet_ShouldBeEnabled() {
        VerticalLayoutElement section = getEnabledSection();
        ComboBoxElement comboBox = section.$(ComboBoxElement.class).id("emptyValueSet");

        assertThat(comboBox.isEnabled(), is(false));
    }

    @Test
    public void testEnabled_DynamicEnabledEmptyValueSet_ShouldBeVisibleButDisabled() {
        VerticalLayoutElement section = getEnabledSection();
        ComboBoxElement combobox = section.$(ComboBoxElement.class).id("dynamicEnabledEmptyValueSet");

        assertThat(combobox.isEnabled(), is(true));
    }

    @Test
    public void testRequired_UnrestrictedValueSetExclNull_ShouldBeRequired() {
        VerticalLayoutElement section = getRequiredSection();
        TextFieldElement textField = section.$(TextFieldElement.class).id("unrestrictedValueSetExclNull");

        assertThat(textField.getClassNames(), hasItem(LinkkiTheme.REQUIRED_LABEL_COMPONENT_WRAPPER));
    }

    @Test
    public void testRequired_UnrestrictedValueSetInclNull_ShouldNotBeRequired() {
        VerticalLayoutElement section = getRequiredSection();
        TextFieldElement textField = section.$(TextFieldElement.class).id("unrestrictedValueSetInclNull");

        assertThat(textField.getClassNames(), not(hasItem(LinkkiTheme.REQUIRED_LABEL_COMPONENT_WRAPPER)));
    }

    private VerticalLayoutElement getSection(Class<?> sectionPmoClass) {
        return $(VerticalLayoutElement.class).id(sectionPmoClass.getSimpleName());
    }

    private VerticalLayoutElement getRequiredSection() {
        return getSection(RequiredSectionPmo.class);
    }

    private VerticalLayoutElement getEnabledSection() {
        return getSection(EnabledSectionPmo.class);
    }

    private VerticalLayoutElement getVisibleSection() {
        return getSection(VisibleSectionPmo.class);
    }
}
