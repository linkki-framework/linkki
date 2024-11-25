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
import org.linkki.testbench.UITestConfiguration;
import org.linkki.testbench.pageobjects.LinkkiGridElement;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.openqa.selenium.JavascriptExecutor;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;

@UITestConfiguration(locale = "en")
class LinkkiSectionPlaceholderTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS002, TestScenarioView.TC006);
    }

    @Test
    void testPlaceholderOnEmptySection() {
        var section = getSection(SectionsWithPlaceholder.EmptySectionWithDynamicPlaceholderPmo.class);
        assertPlaceholder(section, "This section contains no child elements.");
    }

    @Test
    void testPlaceholderOnSectionWithInvisibleChildren() {
        var section = getSection(SectionsWithPlaceholder.SectionWithInvisibleChildrenPlaceholderPmo.class);
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
        assertThat(getPlaceholderText(section)).isEqualTo("\"" + placeholder + "\"");
    }

    private void assertPlaceholder(LinkkiGridElement grid, String placeholder) {
        assertThat(getPlaceholderText(grid)).isEqualTo("\"" + placeholder + "\"");
    }

    private void assertPlaceholderIsEmpty(LinkkiSectionElement section) {
        assertThat(getPlaceholderText(section)).isEqualTo("none");
    }

    private String getPlaceholderText(LinkkiSectionElement section) {
        String script = "return window.getComputedStyle(arguments[0], '::after').getPropertyValue('content');";
        JavascriptExecutor js = (JavascriptExecutor)getDriver();
        return (String)js.executeScript(script, section.getContent());
    }

    private String getPlaceholderText(LinkkiGridElement grid) {
        String script = "return window.getComputedStyle(arguments[0], '::after').getPropertyValue('content');";
        JavascriptExecutor js = (JavascriptExecutor)getDriver();
        return (String)js.executeScript(script, grid);
    }

    private String isPlaceholderDisplayed(LinkkiSectionElement section) {
        String script = "return window.getComputedStyle(arguments[0], '::after').content";
        JavascriptExecutor js = (JavascriptExecutor)getDriver();
        return (String)js.executeScript(script, section.getContent());
    }

}
