/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.linkki.core.util.MessageListUtil.newMessageList;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.junit.Test;
import org.linkki.framework.ui.component.MessageRow;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class OkCancelDialogTest {

    @Test
    public void testSetMessageList() {
        OkCancelDialog dialog = new OkCancelDialog("caption");
        VerticalLayout layout = (VerticalLayout)dialog.getContent();
        Component mainArea = layout.getComponent(0);
        Button okButton = (Button)((HorizontalLayout)layout.getComponent(1)).getComponent(0);

        // Initial state: no message is displayed, buttons are enabled
        assertThat(mainArea, is(not(instanceOf(MessageRow.class))));
        assertThat(dialog.isOkEnabled(), is(true));
        assertThat(okButton.isEnabled(), is(true));

        // MessageList with error and warning: error is displayed, button is disabled
        dialog.setMessageList(newMessageList(Message.newWarning("warning", "warning"),
                                             Message.newError("error", "error")));
        assertThat(layout.getComponent(0), is(instanceOf(MessageRow.class)));
        assertThat(((MessageRow)layout.getComponent(0)).getText(), is("error"));
        assertThat(dialog.isOkEnabled(), is(false));
        assertThat(okButton.isEnabled(), is(false));

        // MessageList without entries: nothing is displayed, button is enabled
        dialog.setMessageList(new MessageList());
        assertThat(layout.getComponent(0), is(mainArea));
        assertThat(dialog.isOkEnabled(), is(true));
        assertThat(okButton.isEnabled(), is(true));

        // MessageList with warning: warning is displayed, button is enabled
        dialog.setMessageList(new MessageList(Message.newWarning("warning", "warning")));
        assertThat(layout.getComponent(0), is(instanceOf(MessageRow.class)));
        assertThat(((MessageRow)layout.getComponent(0)).getText(), is("warning"));
        assertThat(dialog.isOkEnabled(), is(true));
        assertThat(okButton.isEnabled(), is(true));
    }

}
