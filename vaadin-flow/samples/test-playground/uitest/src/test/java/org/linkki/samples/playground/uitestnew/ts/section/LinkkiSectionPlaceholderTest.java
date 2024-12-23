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

package org.linkki.samples.playground.uitestnew.ts.section;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.section.SectionsWithPlaceholder;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiGridElement;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;

class LinkkiSectionPlaceholderTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS002, TestScenarioView.TC006);
    }

    @Test
    void testPlaceholderOnEmptySection() {
        var section = getSection(SectionsWithPlaceholder.EmptySectionWithPlaceholderPmo.class);
        assertPlaceholder(section, "This section has no visible children");
    }

    @Test
    void testPlaceholderOnSectionWithInvisibleChildren() {
        var section = getSection(SectionsWithPlaceholder.SectionWithPlaceholderPmo.class);
        assertPlaceholder(section, "This section contains no visible child elements.");

        section.getHeaderComponents(CheckboxElement.class).first().click();

        assertPlaceholderIsEmpty(section);
    }

    @Test
    void testPlaceholderOnSectionWithEmptyTable() {
        var section = getSection(SectionsWithPlaceholder.SectionWithEmptyTablePmo.class);
        assertPlaceholderIsEmpty(section);
        var grid = section.$(LinkkiGridElement.class).id("rowPmos");
        assertPlaceholder(grid, "This table is empty.");
    }

    private void assertPlaceholder(LinkkiSectionElement section, String placeholder) {
        assertThat(section.getPlaceholderText()).hasValue(placeholder);
    }

    private void assertPlaceholder(LinkkiGridElement grid, String placeholder) {
        assertThat(grid.getPlaceholderText()).hasValue(placeholder);
    }

    private void assertPlaceholderIsEmpty(LinkkiSectionElement section) {
        assertThat(section.getPlaceholderText()).isEmpty();
    }

}
