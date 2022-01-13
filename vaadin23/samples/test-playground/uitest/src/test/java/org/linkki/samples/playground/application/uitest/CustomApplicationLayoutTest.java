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

package org.linkki.samples.playground.application.uitest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.linkki.samples.playground.uitest.DriverProperties;

import com.vaadin.flow.component.menubar.testbench.MenuBarElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.testbench.ElementQuery;

class CustomApplicationLayoutTest extends AbstractUiTest {

    @BeforeEach
    void setup() {
        getDriver().get(DriverProperties.getTestUrl(""));
        clickMenuItem("Custom Layout");
    }

    @Test
    void testApplicationLayout_HeaderExists() {
        ElementQuery<HorizontalLayoutElement> appHeader = $(HorizontalLayoutElement.class)
                .attribute("class", LinkkiApplicationTheme.APPLICATION_HEADER);
        assertThat(appHeader.first().isDisplayed(), is(true));
    }

    @Test
    void testApplicationLayout_FooterExists() {
        ElementQuery<HorizontalLayoutElement> appHeader = $(HorizontalLayoutElement.class)
                .attribute("class", LinkkiApplicationTheme.APPLICATION_FOOTER);
        assertThat(appHeader.first().isDisplayed(), is(true));
    }

    @Test
    void testApplicationLayout_MenuExists() {
        MenuBarElement menu = $(MenuBarElement.class)
                .id(LinkkiApplicationTheme.APPLICATION_MENU);
        assertThat(menu.isDisplayed(), is(true));
    }
}
