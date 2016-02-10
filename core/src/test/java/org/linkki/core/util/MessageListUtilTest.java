/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.Severity;
import org.junit.Test;
import org.linkki.core.util.MessageListUtil;

import com.vaadin.server.ErrorMessage.ErrorLevel;

public class MessageListUtilTest {

    @Test
    public void testGetErrorLevel() {
        MessageList list = new MessageList();
        list.add(Message.newError("code", "text"));

        assertEquals(ErrorLevel.ERROR, MessageListUtil.getErrorLevel(list));
    }

    @Test
    public void testGetErrorLevel_error() {
        assertEquals(ErrorLevel.ERROR, MessageListUtil.getErrorLevel(Severity.ERROR));
    }

    @Test
    public void testGetErrorLevel_warning() {
        assertEquals(ErrorLevel.WARNING, MessageListUtil.getErrorLevel(Severity.WARNING));
    }

    @Test
    public void testGetErrorLevel_info() {
        assertEquals(ErrorLevel.INFORMATION, MessageListUtil.getErrorLevel(Severity.INFO));
    }

    @Test
    public void testGetErrorLevel_none() {
        assertNull(MessageListUtil.getErrorLevel(Severity.NONE));
    }

    public void testGetErrorLevel_null() {
        Severity severity = null;
        assertNull(MessageListUtil.getErrorLevel(severity));
    }
}
