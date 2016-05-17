/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.validation;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.linkki.core.binding.validation.ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS;
import static org.linkki.core.binding.validation.ValidationDisplayState.SHOW_ALL;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.junit.Test;
import org.linkki.core.util.MessageListUtil;
import org.linkki.test.matcher.Matchers;
import org.linkki.util.validation.ValidationMarker;

public class ValidationDisplayStateTest {

    @Test
    public void testFilter() {
        ValidationMarker mandatoryFieldMarker = () -> true;
        ValidationMarker nonMandatoryFieldMarker = () -> false;
        Message m1 = Message.error("m1").markers(mandatoryFieldMarker).create();
        Message m2 = Message.error("m2").markers(nonMandatoryFieldMarker).create();
        Message m3 = Message.error("m3").create();
        MessageList messages = MessageListUtil.newMessageList(m1, m2, m3);

        assertThat(SHOW_ALL.filter(messages), contains(m1, m2, m3));
        assertThat(HIDE_MANDATORY_FIELD_VALIDATIONS.filter(messages), contains(m2, m3));
    }

    @Test
    public void testFilter_EmptyMessageList() {
        MessageList emptyMessageList = new MessageList();

        assertThat(SHOW_ALL.filter(emptyMessageList), is(Matchers.emptyMessageList()));
        assertThat(HIDE_MANDATORY_FIELD_VALIDATIONS.filter(emptyMessageList), is(emptyMessageList));
    }

}
