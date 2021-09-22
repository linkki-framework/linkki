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

package org.linkki.samples.playground.uitestnew.ts.tablayout;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.flow.component.tabs.testbench.TabElement;

public class TC001VerticalTabLayoutTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;
    private DivElement section;

    @BeforeEach
    void setup() {
        super.setUp();
        testCaseSection = goToTestCase("TS010", "TC001");
        section = testCaseSection.getContentWrapper().$(DivElement.class).first();
    }

    @Test
    public void testVerticalTabs() {
        TabElement tab1 = section.$(TabElement.class).id("tab1");
        TabElement tab2 = section.$(TabElement.class).id("tab2");

        assertThat(tab1.getLocation().y, is(tab2.getLocation().y));
    }

    @Test
    public void testTabHasIcon() {
        TabElement tab = section.$(TabElement.class).id("tab2");

        WebElement icon = tab.findElement(By.tagName("iron-icon"));

        assertThat(icon.getAttribute("icon"), is("vaadin:plus"));
    }

    @Test
    public void testContentReplacedInSamePosition() {
        TabElement tab1 = section.$(TabElement.class).id("tab1");
        TabElement tab2 = section.$(TabElement.class).id("tab2");

        tab1.click();
        SpanElement content1 = section.$(SpanElement.class).id("content1");
        assertThat(content1.isDisplayed(), is(true));
        Point location1 = content1.getLocation();
        tab2.click();
        SpanElement content2 = section.$(SpanElement.class).id("content2");
        assertThat(content2.isDisplayed(), is(true));
        Point location2 = content2.getLocation();

        assertThat(location1, is(equalTo(location2)));
    }

}
