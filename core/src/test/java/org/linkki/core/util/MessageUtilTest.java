/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.faktorips.runtime.IMarker;
import org.faktorips.runtime.Message;
import org.faktorips.runtime.Severity;
import org.junit.Test;
import org.linkki.core.binding.validation.ValidationMarker;

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

    private Message createMessage(ValidationMarker... markers) {
        return new Message.Builder("", Severity.ERROR).markers(markers != null ? markers : new IMarker[] {}).create();
    }

}
