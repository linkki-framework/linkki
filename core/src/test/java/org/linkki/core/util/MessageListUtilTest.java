/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.linkki.test.matcher.Matchers.emptyMessageList;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.Severity;
import org.junit.Test;

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

    @Test
    public void testGetErrorLevel_null() {
        Severity severity = null;
        assertNull(MessageListUtil.getErrorLevel(severity));
    }

    @Test
    public void testNewMessageList_null() {
        assertThat(MessageListUtil.newMessageList((Message[])null), is(emptyMessageList()));
    }

    @Test
    public void testNewMessageList_empty() {
        assertThat(MessageListUtil.newMessageList(new Message[0]), is(emptyMessageList()));
    }

    @Test
    public void testNewMessageList() {
        Message m1 = Message.newError("error", "error");
        Message m2 = Message.newWarning("warning", "warning");
        assertThat(MessageListUtil.newMessageList(m1, m2), hasItems(m1, m2));
    }

    @Test
    public void testSortBySeverity() {
        Message e1 = Message.newError("e1", "E1");
        Message e2 = Message.newError("e2", "E2");
        Message e3 = Message.newError("e3", "E3");
        Message w1 = Message.newWarning("w1", "W1");
        Message w2 = Message.newWarning("w2", "W2");
        Message i1 = Message.newInfo("i1", "I1");
        Message i2 = Message.newInfo("i2", "I2");
        MessageList unsortedMessageList = MessageListUtil.newMessageList(i2, e1, w1, e3, i1, e2, w2);
        MessageList sortedMessageList = MessageListUtil.newMessageList(e1, e3, e2, w1, w2, i2, i1);

        MessageList actualMessageList = MessageListUtil.sortBySeverity(unsortedMessageList);

        assertThat(actualMessageList, is(equalTo(sortedMessageList)));
    }

    @Test
    public void testSortBySeverity_Null() {
        assertThat(MessageListUtil.sortBySeverity(null), is(nullValue()));
    }
}
