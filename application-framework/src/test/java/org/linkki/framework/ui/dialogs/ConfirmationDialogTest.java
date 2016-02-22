/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.vaadin.ui.Label;

/**
 * A dialog to present an information to the user, who has to confirm it with OK.
 */
public class ConfirmationDialogTest {

    @Test
    public void testCancelCallsOk() {
        OkHandler handler = mock(OkHandler.class);
        ConfirmationDialog dialog = new ConfirmationDialog("", new Label(), handler);
        dialog.cancel();
        verify(handler).onOk();
    }

}
