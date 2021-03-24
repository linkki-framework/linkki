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
    public void testVisible_ValueSetInclNull_ShouldBeVisible() {
        VerticalLayoutElement section = getVisibleSection();
        ComboBoxElement comboBox = section.$(ComboBoxElement.class).id("valueSetInclNull");

        assertThat(comboBox.isDisplayed(), is(true));
    }

    @Test
    public void testVisible_ValueSetExclNull_ShouldBeVisible() {
        VerticalLayoutElement section = getVisibleSection();
        ComboBoxElement comboBox = section.$(ComboBoxElement.class).id("valueSetExclNull");

        assertThat(comboBox.isDisplayed(), is(true));
    }

    @Test
    public void testVisible_EmptyValueSetExclNull_WithDynamicVisibleProperty_ShouldBeVisible() {
        VerticalLayoutElement section = getVisibleSection();
        ComboBoxElement combobox = section.$(ComboBoxElement.class).id("emptyValueSet");
        // Make sure, combobox is in visible area of the browser window
        combobox.scrollIntoView();

        // Invisibility is overruled by VisibleType.DYNAMIC
        assertThat(combobox.isDisplayed(), is(true));
    }

    @Test
    public void testVisible_EmptyValueSetExclNull_ShouldNotBeVisible() {
        VerticalLayoutElement section = getVisibleSection();

        assertThrows(NoSuchElementException.class, () -> section.$(ComboBoxElement.class).id("invisibleEmptyValueSet"));
    }

    @Test
    public void testEnabled_ValueSetInclNull_ShouldBeEnabled() {
        VerticalLayoutElement section = getEnabledSection();
        ComboBoxElement comboBox = section.$(ComboBoxElement.class).id("valueSetInclNull");

        assertThat(comboBox.isEnabled(), is(true));
    }

    @Test
    public void testEnabled_ValueSetExclNull_ShouldBeEnabled() {
        VerticalLayoutElement section = getEnabledSection();
        ComboBoxElement comboBox = section.$(ComboBoxElement.class).id("valueSetExclNull");

        assertThat(comboBox.isEnabled(), is(true));
    }

    @Test
    public void testEnabled_EmptyValueSetExclNull_WithDynamicVisibleProperty_ShouldBeVisibleButDisabled() {
        VerticalLayoutElement section = getEnabledSection();
        ComboBoxElement combobox = section.$(ComboBoxElement.class).id("emptyValueSet");

        // Invisibility is overruled by VisibleType.DYNAMIC, but ComboBox should still be disabled
        assertThat(combobox.isEnabled(), is(false));
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

    private VerticalLayoutElement getRequiredSection() {
        return $(VerticalLayoutElement.class).id(RequiredSectionPmo.class.getSimpleName());
    }

    private VerticalLayoutElement getEnabledSection() {
        return $(VerticalLayoutElement.class).id(EnabledSectionPmo.class.getSimpleName());
    }

    private VerticalLayoutElement getVisibleSection() {
        return $(VerticalLayoutElement.class).id(VisibleSectionPmo.class.getSimpleName());
    }
}
