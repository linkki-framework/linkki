/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.util;

import org.linkki.core.message.Message;
import org.linkki.util.validation.ValidationMarker;

public class MessageUtil {

    // Utility class that should not be instantiated
    private MessageUtil() {
        super();
    }

    /**
     * Returns {@code true} if the given message has at least one marker of type
     * {@code ValidationMarker} marking it as an mandatory field validation message, i.e.
     * {@link ValidationMarker#isRequiredInformationMissing()} is {@code true}. Returns {@code false}
     * if the message has no {@code ValidationMarker} or is not marked as a mandatory field
     * validation message.
     */
    public static boolean isMandatoryFieldMessage(Message msg) {
        return msg.getMarkers().stream()
                .anyMatch(ValidationMarker::isRequiredInformationMissing);
    }

}
