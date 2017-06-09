/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.message;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Nullable;

import org.junit.Test;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.server.ErrorMessage.ErrorLevel;

public class MessageTest {

    @Test
    public void testIsMandatoryFieldMessage() {
        ValidationMarker mandatoryMarker = () -> true;
        ValidationMarker notMandatoryMarker = () -> false;

        assertThat(createMessage().isMandatoryFieldMessage(), is(false));
        assertThat(createMessage(notMandatoryMarker).isMandatoryFieldMessage(), is(false));
        assertThat(createMessage(mandatoryMarker).isMandatoryFieldMessage(), is(true));
        assertThat(createMessage(notMandatoryMarker, mandatoryMarker).isMandatoryFieldMessage(), is(true));
    }

    private Message createMessage(@Nullable ValidationMarker... markers) {
        return Message.builder("", ErrorLevel.ERROR).markers(markers).create();
    }

}