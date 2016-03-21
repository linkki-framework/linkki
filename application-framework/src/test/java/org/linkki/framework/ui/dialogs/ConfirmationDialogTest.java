/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Label;

/**
 * A dialog to present an information to the user, who has to confirm it with OK.
 */
public class ConfirmationDialogTest {

    @Test
    public void testCancelCallsOk() {
        Handler handler = mock(Handler.class);
        ConfirmationDialog dialog = new ConfirmationDialog("", new Label(), handler);
        dialog.cancel();
        verify(handler).apply();
    }

}
