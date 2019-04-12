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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.linkki.core.vaadin.component.page.Page;

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
        Page tabPage1 = mock(Page.class);
        Page tabPage2 = mock(Page.class);

        verify(tabPage1, times(0)).reloadBindings();
        verify(tabPage2, times(0)).reloadBindings();
        tabSheetArea.addTab(tabPage1, "1");
        tabSheetArea.addTab(tabPage2, "2");
        // first tab is refreshed because it is automatically set as the selected tab. Second tab is
        // not refreshed as it is currently not visible
        verify(tabPage1, times(1)).reloadBindings();
        verify(tabPage2, times(0)).reloadBindings();
    }

    @Test
    public void testTabSelectionRefreshesPage() {
        TabSheetArea tabSheetArea = new TestArea();
        Page tabPage1 = mock(Page.class);
        Page tabPage2 = mock(Page.class);

        tabSheetArea.addTab(tabPage1, "1");
        tabSheetArea.addTab(tabPage2, "2");

        verify(tabPage2, times(0)).reloadBindings();

        tabSheetArea.setSelectedTab(1);
        verify(tabPage2, times(1)).reloadBindings();
    }

    @Test
    public void testGetTabs() {
        Page tabPage1 = mock(Page.class);
        Page tabPage2 = mock(Page.class);

        TabSheetArea tabSheetArea = new TestArea();
        assertThat(tabSheetArea.getTabs(), is(empty()));

        tabSheetArea.addTab(tabPage1, "1");
        tabSheetArea.addTab(tabPage2, "2");

        assertThat(tabSheetArea.getTabs(), contains(tabPage1, tabPage2));
    }

}
