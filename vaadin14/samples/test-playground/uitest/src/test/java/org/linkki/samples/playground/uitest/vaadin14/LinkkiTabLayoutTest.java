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

package org.linkki.samples.playground.uitest.vaadin14;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.tablayout.TabLayoutPage;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.flow.component.tabs.testbench.TabElement;

public class LinkkiTabLayoutTest extends AbstractUiTest {

    @BeforeEach
    public void setTab() {
        openTab("Tab Layout");
    }

    @Test
    public void testTabHasIcon() {
        TabElement tab = $(TabElement.class).id(TabLayoutPage.HORIZONTAL_TAB_2);

        WebElement icon = tab.findElement(By.tagName("iron-icon"));

        assertThat(icon.getAttribute("icon"), is("vaadin:plus"));
    }

    @Test
    public void testContentReplacedInSamePosition() {
        TabElement tab1 = $(TabElement.class).id(TabLayoutPage.HORIZONTAL_TAB_1);
        TabElement tab2 = $(TabElement.class).id(TabLayoutPage.HORIZONTAL_TAB_2);

        tab1.click();
        SpanElement content1 = $(SpanElement.class).id(TabLayoutPage.HORIZONTAL_CONTENT_1);
        assertThat(content1.isDisplayed(), is(true));
        Point location1 = content1.getLocation();
        tab2.click();
        SpanElement content2 = $(SpanElement.class).id(TabLayoutPage.HORIZONTAL_CONTENT_2);
        assertThat(content2.isDisplayed(), is(true));
        Point location2 = content2.getLocation();

        assertThat(location1, is(equalTo(location2)));
    }

    @Test
    public void testHorizontalTabs() {
        TabElement tab1 = $(TabElement.class).id(TabLayoutPage.HORIZONTAL_TAB_1);
        TabElement tab2 = $(TabElement.class).id(TabLayoutPage.HORIZONTAL_TAB_2);

        assertThat(tab1.getLocation().y, is(tab2.getLocation().y));
    }

    @Test
    public void testVerticalTabs() {
        TabElement tab1 = $(TabElement.class).id(TabLayoutPage.VERTICAL_TAB_1);
        TabElement tab2 = $(TabElement.class).id(TabLayoutPage.VERTICAL_TAB_2);

        assertThat(tab1.getLocation().x, is(tab2.getLocation().x));
    }

}
