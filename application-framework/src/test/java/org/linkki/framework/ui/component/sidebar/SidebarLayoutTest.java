/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.framework.ui.component.sidebar;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.linkki.util.handler.Handler;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class SidebarLayoutTest {

    private static final Resource ANY_ICON = FontAwesome.AMBULANCE;
    private static final String ANY_STRING = "anything";

    @Test
    public void testSelect_newSheetIsSelected() {
        SidebarLayout sidebarLayout = new SidebarLayout();

        VerticalLayout content = new VerticalLayout();
        SidebarSheet sidebarSheet = spy(new SidebarSheet(ANY_ICON, ANY_STRING, content));

        sidebarLayout.select(sidebarSheet);

        verify(sidebarSheet).select();
        assertThat(sidebarLayout.getSelected(), is(sidebarSheet));
        assertThat(sidebarLayout.getContentArea(), hasItem(content));
    }

    @Test
    public void testSelect_oldSheetIsUnselected() {
        SidebarLayout sidebarLayout = new SidebarLayout();

        SidebarSheet oldSheet = spy(new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent()));
        SidebarSheet newSheet = spy(new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent()));

        sidebarLayout.select(oldSheet);
        assertThat(sidebarLayout.getSelected(), is(oldSheet));
        verify(oldSheet).select();

        sidebarLayout.select(newSheet);
        assertThat(sidebarLayout.getSelected(), is(newSheet));
        verify(oldSheet).unselect();
        verify(newSheet).select();
    }

    @Test
    public void testAddSheet_firstSheetGetsSelected() {
        SidebarLayout sidebarLayout = new SidebarLayout();
        SidebarSheet sheet1 = new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent());
        SidebarSheet sheet2 = new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent());

        sidebarLayout.addSheets(sheet1, sheet2);

        assertThat(sidebarLayout.getSelected(), is(sheet1));
    }

    @Test
    public void testAddSheet_lazyLoading() {
        Handler somethingCalledInConstructor = mock(Handler.class);

        SidebarLayout sidebarLayout = new SidebarLayout();
        SidebarSheet sheet1 = new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent());
        SidebarSheet sheet2 = new SidebarSheet(ANY_ICON, ANY_STRING,
                () -> new TestLayout(somethingCalledInConstructor));
        sidebarLayout.addSheets(sheet1, sheet2);

        assertThat(sidebarLayout.getSidebar().getComponentCount(), is(2));
        assertThat(sidebarLayout.getContentArea().getComponentCount(), is(1));
        verify(somethingCalledInConstructor, never()).apply();
    }

    @Test
    public void testSelect_createContentIfNotAlready() {
        Handler somethingCalledInConstructor = mock(Handler.class);

        SidebarLayout sidebarLayout = new SidebarLayout();
        SidebarSheet sheet1 = new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent());
        SidebarSheet sheet2 = new SidebarSheet(ANY_ICON, ANY_STRING,
                () -> new TestLayout(somethingCalledInConstructor));

        sidebarLayout.addSheets(sheet1, sheet2);

        // content is added when first selected
        sidebarLayout.select(sheet2);
        verify(somethingCalledInConstructor).apply();
        assertThat(sidebarLayout.getSidebar().getComponentCount(), is(2));
        assertThat(sidebarLayout.getContentArea().getComponentCount(), is(2));
        assertThat(sidebarLayout.getContentArea().getComponent(1), instanceOf(TestLayout.class));

        sidebarLayout.select(sheet1);

        // content is not created twice
        sidebarLayout.select(sheet2);
        verifyNoMoreInteractions(somethingCalledInConstructor);
        assertThat(sidebarLayout.getSidebar().getComponentCount(), is(2));
        assertThat(sidebarLayout.getContentArea().getComponentCount(), is(2));
    }

    private static Component createNewContent() {
        return new VerticalLayout();
    }

    private static class TestLayout extends VerticalLayout {
        private static final long serialVersionUID = -7619999338652450248L;

        public TestLayout(Handler doSomethingComplicated) {
            super();
            doSomethingComplicated.apply();
        }
    }
}
