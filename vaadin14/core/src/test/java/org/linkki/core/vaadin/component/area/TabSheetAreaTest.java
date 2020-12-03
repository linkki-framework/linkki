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
package org.linkki.core.vaadin.component.area;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.tabs.Tab;

public class TabSheetAreaTest {

    static class TestArea extends TabSheetArea {

        private static final long serialVersionUID = 1L;

        @Override
        public void createContent() {
            // nothing to do
        }

        @Override
        public void updateContent() {
            // nothing to do
        }

    }

    @Test
    public void testAddTab_RefreshesFirstTabPage() {
        TabSheetArea tabSheetArea = new TestArea();
        Tab tabPage1 = new Tab("1");
        Tab tabPage2 = new Tab("2");

        // TODO LIN-2065
        // verify(tabPage1, times(0)).reloadBindings();
        // verify(tabPage2, times(0)).reloadBindings();
        tabSheetArea.add(tabPage1);
        tabSheetArea.add(tabPage2);
        // first tab is refreshed because it is automatically set as the selected tab. Second tab is
        // not refreshed as it is currently not visible
        // verify(tabPage1, times(1)).reloadBindings();
        // verify(tabPage2, times(0)).reloadBindings();
    }

    @Test
    public void testTabSelectionRefreshesPage() {
        TabSheetArea tabSheetArea = new TestArea();
        Tab tabPage1 = new Tab("1");
        Tab tabPage2 = new Tab("2");

        tabSheetArea.add(tabPage1);
        tabSheetArea.add(tabPage2);

        // TODO LIN-2065
        // verify(tabPage2, times(0)).reloadBindings();

        tabSheetArea.setSelectedIndex(1);
        // verify(tabPage2, times(1)).reloadBindings();
    }

    @Test
    public void testGetTabs() {
        Tab tabPage1 = new Tab("1");
        Tab tabPage2 = new Tab("2");

        TabSheetArea tabSheetArea = new TestArea();
        assertThat(tabSheetArea.getTabs(), is(empty()));

        tabSheetArea.add(tabPage1);
        tabSheetArea.add(tabPage2);

        assertThat(tabSheetArea.getTabs(), contains(tabPage1, tabPage2));
    }

}
