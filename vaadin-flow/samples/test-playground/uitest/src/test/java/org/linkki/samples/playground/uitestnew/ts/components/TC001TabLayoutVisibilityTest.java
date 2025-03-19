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

package org.linkki.samples.playground.uitestnew.ts.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.flow.component.tabs.testbench.TabElement;

class TC001TabLayoutVisibilityTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;
    private DivElement section;

    @BeforeEach
    void goToTestCase() {
        testCaseSection = goToTestCase(TestScenarioView.TS010, TestScenarioView.TC001);
    }

    @Test
    void testHideFirstTab() {
        TabElement tab = testCaseSection.$(TabElement.class).id("visibility-tab1");
        SpanElement content = testCaseSection.$(SpanElement.class).id("visibility-content1");
        testCaseSection.$(CheckboxElement.class).id("visibility-checkbox1").setChecked(false);

        testCaseSection.$(ButtonElement.class).id("visibility-update-button").click();

        waitUntil(ExpectedConditions.invisibilityOf(tab));
        waitUntil(ExpectedConditions.invisibilityOf(content));
    }
}
