/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Nullable;

import org.junit.Test;
import org.linkki.core.message.Message;
import org.linkki.core.message.Severity;
import org.linkki.util.validation.ValidationMarker;

public class MessageUtilTest {

    @Test
    public void testIsMandatoryFieldMessage() {
        ValidationMarker mandatoryMarker = () -> true;
        ValidationMarker notMandatoryMarker = () -> false;

        assertThat(MessageUtil.isMandatoryFieldMessage(createMessage()), is(false));
        assertThat(MessageUtil.isMandatoryFieldMessage(createMessage(notMandatoryMarker)), is(false));
        assertThat(MessageUtil.isMandatoryFieldMessage(createMessage(mandatoryMarker)), is(true));
        assertThat(MessageUtil.isMandatoryFieldMessage(createMessage(notMandatoryMarker, mandatoryMarker)), is(true));
    }

    private Message createMessage(@Nullable ValidationMarker... markers) {
        return Message.builder("", Severity.ERROR).markers(markers).create();
    }

}
