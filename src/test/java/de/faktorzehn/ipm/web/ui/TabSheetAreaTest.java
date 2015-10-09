/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

import de.faktorzehn.ipm.web.ui.page.AbstractPage;

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
    public void testAddTab_CreatesTabSheetAreaContent() {
        TabSheetArea tabSheetArea = new TestArea();
        TabSheetArea childArea = mock(TabSheetArea.class);

        tabSheetArea.addTab(childArea, "1");

        verify(childArea).createContent();
    }

    @Test
    public void testAddTab_RefreshesPage() {
        TabSheetArea tabSheetArea = new TestArea();
        AbstractPage childPage = mock(AbstractPage.class);

        tabSheetArea.addTab(childPage, "1");

        verify(childPage).refresh();
    }

    @Test
    public void testGetTabContents() {
        TabSheetArea childArea = mock(TabSheetArea.class);
        AbstractPage childPage = mock(AbstractPage.class);

        TabSheetArea tabSheetArea = new TestArea();
        tabSheetArea.addTab(childArea, "area");
        tabSheetArea.addTab(childPage, "page");

        assertThat(tabSheetArea.getTabContents(Button.class), is(empty()));
        assertThat(tabSheetArea.getTabContents(TabSheetArea.class), contains(childArea));
        assertThat(tabSheetArea.getTabContents(AbstractPage.class), contains(childPage));
        assertThat(tabSheetArea.getTabContents(Component.class), contains(childArea, childPage));
    }

}
