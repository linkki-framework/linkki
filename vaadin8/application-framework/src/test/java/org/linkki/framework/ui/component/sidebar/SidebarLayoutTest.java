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

package org.linkki.framework.ui.component.sidebar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.linkki.test.matcher.Matchers.hasValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.component.sidebar.SidebarLayout.SelectionListener;
import org.linkki.framework.ui.component.sidebar.SidebarLayout.SidebarSelectionEvent;
import org.linkki.util.handler.Handler;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import edu.umd.cs.findbugs.annotations.Nullable;

public class SidebarLayoutTest {

    private static final Resource ANY_ICON = VaadinIcons.AMBULANCE;
    private static final String ANY_STRING = "anything";

    @Test
    public void testSelect_newSheetIsSelected() {
        SidebarLayout sidebarLayout = new SidebarLayout();
        VerticalLayout content = new VerticalLayout();
        SidebarSheet sidebarSheet = spy(new SidebarSheet(ANY_ICON, ANY_STRING, content));
        sidebarLayout.addSheet(sidebarSheet);

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
        sidebarLayout.addSheets(oldSheet, newSheet);

        verify(oldSheet).select();
        reset(oldSheet);

        sidebarLayout.select(oldSheet);

        assertThat(sidebarLayout.getSelected(), is(oldSheet));
        verifyNoMoreInteractions(oldSheet);

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

    @Test
    public void testSelect_NotRegistered() {
        SidebarLayout sidebarLayout = new SidebarLayout();
        SidebarSheet sheet1 = new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sidebarLayout.select(sheet1);
        });

    }

    @Test
    public void testAddSelectionListener_NewSheetSelectionTriggered() {
        SidebarLayout sidebarLayout = new SidebarLayout();
        TestSelectionListener listener = new TestSelectionListener();
        SidebarSheet sheet1 = new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent());

        sidebarLayout.addSelectionListener(listener);
        sidebarLayout.addSheet(sheet1);

        assertThat(listener.event.getSelectedSheet(), is(sheet1));
    }

    @Test
    public void testAddSelectionListener_NotTriggeredOnSelectAlreadySelectedSheet() {
        SidebarLayout sidebarLayout = new SidebarLayout();
        TestSelectionListener listener = new TestSelectionListener();
        SidebarSheet sheet1 = new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent());
        SidebarSheet sheet2 = new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent());
        sidebarLayout.addSheet(sheet1);
        sidebarLayout.addSheet(sheet2);

        sidebarLayout.addSelectionListener(listener);
        sidebarLayout.select(sheet1);

        assertThat(listener.event, is(nullValue()));
    }

    @Test
    public void testAddSelectionListener_CheckNormalSelectEvent() {
        SidebarLayout sidebarLayout = new SidebarLayout();
        TestSelectionListener listener = new TestSelectionListener();
        SidebarSheet sheet1 = new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent());
        SidebarSheet sheet2 = new SidebarSheet(ANY_ICON, ANY_STRING, createNewContent());
        sidebarLayout.addSheet(sheet1);
        sidebarLayout.addSheet(sheet2);

        sidebarLayout.addSelectionListener(listener);
        sidebarLayout.select(sheet2);

        assertThat(listener.event.getSelectedSheet(), is(sheet2));
        assertThat(listener.event.getFirstSelectedItem(), hasValue(sheet2));
        assertThat(listener.event.getAllSelectedItems(), hasItem(sheet2));
        assertThat(listener.event.getSelectedSheet(), is(sheet2));
        assertThat(listener.event.getOldValue(), is(sheet1));
        assertThat(listener.event.getSource(), is(sidebarLayout));
        assertThat(listener.event.isUserOriginated(), is(false));
    }

    static class TestSelectionListener implements SelectionListener {

        private static final long serialVersionUID = 1L;

        @Nullable
        SidebarSelectionEvent event;

        @Override
        public void selectionChange(SidebarSelectionEvent e) {
            this.event = e;
        }

    }

    @Test
    public void testSelectString() {
        SidebarLayout sidebarLayout = new SidebarLayout();
        SidebarSheet sheet1 = spy(new SidebarSheet("id1", ANY_ICON, ANY_STRING, createNewContent()));
        SidebarSheet sheet2 = spy(new SidebarSheet("id2", ANY_ICON, ANY_STRING, createNewContent()));
        sidebarLayout.addSheets(sheet1, sheet2);

        sidebarLayout.select("id2");

        assertThat(sidebarLayout.getSelected(), is(sheet2));
    }

    @Test
    public void testSelectString_DoNothingOnInvalidId() {
        SidebarLayout sidebarLayout = new SidebarLayout();
        SidebarSheet sheet1 = spy(new SidebarSheet("id1", ANY_ICON, ANY_STRING, createNewContent()));
        SidebarSheet sheet2 = spy(new SidebarSheet("id2", ANY_ICON, ANY_STRING, createNewContent()));
        sidebarLayout.addSheets(sheet1, sheet2);

        sidebarLayout.select("xyz");

        assertThat(sidebarLayout.getSelected(), is(sheet1));
    }

}
