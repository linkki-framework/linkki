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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.Point;

import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.flow.component.icon.testbench.IconElement;
import com.vaadin.flow.component.tabs.testbench.TabElement;

class TC001VerticalTabLayoutTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;

    @BeforeEach
    void goToTestCase() {
        testCaseSection = goToTestCase(TestScenarioView.TS010, TestScenarioView.TC001);
    }

    @Test
    void testVerticalTabs() {
        TabElement tab1 = testCaseSection.$(TabElement.class).id("vertical-tab1");
        TabElement tab2 = testCaseSection.$(TabElement.class).id("vertical-tab2");

        assertThat(tab1.getLocation().x, is(tab2.getLocation().x));
    }

    @Test
    void testTabHasIcon() {
        TabElement tab = testCaseSection.$(TabElement.class).id("vertical-tab2");

        IconElement icon = tab.$(IconElement.class).first();

        assertThat(icon.getDomAttribute("icon"), is("vaadin:plus"));
    }

    @Test
    void testContentReplacedInSamePosition() {
        TabElement tab1 = testCaseSection.$(TabElement.class).id("vertical-tab1");
        TabElement tab2 = testCaseSection.$(TabElement.class).id("vertical-tab2");

        tab1.click();
        SpanElement content1 = testCaseSection.$(SpanElement.class).id("vertical-content1");
        assertThat(content1.isDisplayed(), is(true));
        Point location1 = content1.getLocation();
        tab2.click();
        SpanElement content2 = testCaseSection.$(SpanElement.class).id("vertical-content2");
        assertThat(content2.isDisplayed(), is(true));
        Point location2 = content2.getLocation();

        assertThat(location1, is(equalTo(location2)));
    }
}
