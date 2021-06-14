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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.samples.playground.uitest.AbstractUiTest;

import com.vaadin.flow.component.menubar.testbench.MenuBarElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.TestBenchElement;

public class ApplicationLayoutTest extends AbstractUiTest {

    @Test
    public void testApplicationLayout_HeaderExists() {
        ElementQuery<HorizontalLayoutElement> appHeader = $(HorizontalLayoutElement.class)
                .attribute("class", LinkkiApplicationTheme.APPLICATION_HEADER);
        assertThat(appHeader.first().isDisplayed(), is(true));
    }

    @Test
    public void testApplicationLayout_FooterExists() {
        ElementQuery<HorizontalLayoutElement> appHeader = $(HorizontalLayoutElement.class)
                .attribute("class", LinkkiApplicationTheme.APPLICATION_FOOTER);
        assertThat(appHeader.first().isDisplayed(), is(true));
    }

    @Test
    public void testApplicationLayout_MenuExists() {
        MenuBarElement menu = $(MenuBarElement.class)
                .id(LinkkiApplicationTheme.APPLICATION_MENU);
        assertThat(menu.isDisplayed(), is(true));
    }


    public static final String OVERLAY_TAG = "vaadin-context-menu-overlay";
    public static final String MENU_ITEM_TAG = "vaadin-context-menu-item";

    protected List<TestBenchElement> getAllOverlays() {
        return $(OVERLAY_TAG).all();
    }

    protected List<TestBenchElement> getMenuItems(TestBenchElement overlay) {
        return overlay.$(MENU_ITEM_TAG).all();
    }

    protected TestBenchElement getMenuItem(List<TestBenchElement> menuItems, String caption) {
        for (TestBenchElement menuItem : menuItems) {
            if (menuItem.getText().trim().equals(caption)) {
                return menuItem;
            }
        }
        return null;
    }

}
