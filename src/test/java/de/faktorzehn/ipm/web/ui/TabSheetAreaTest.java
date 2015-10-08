/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class TabSheetAreaTest {

    @Test
    public void testCreateContentChildTabSheet() {
        TabSheetArea tabSheetArea = new TestArea();
        TabSheetArea childArea = mock(TabSheetArea.class);

        tabSheetArea.addTab(childArea, "1");

        verify(childArea).createContent();
    }

    public class TestArea extends TabSheetArea {
        private static final long serialVersionUID = 1L;

        @Override
        public void createContent() {
            // nothing to do
        }
    }
}
