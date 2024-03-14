package org.linkki.samples.appsample.model;

import org.faktorips.runtime.IMarker;
import org.faktorips.runtime.Message;
import org.faktorips.runtime.Message.Builder;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.Severity;

public class Validation {

    public static final IMarker MARKER_REQUIRED = new RequiredInformationMissingMarker();

    private Validation() {
        // prevent instantiation
    }

    public static MessageList validateRequiredProperty(String value,
            String errorCode,
            String errorMessage,
            Object object,
            String property) {
        if (value == null || value.isEmpty()) {
            Message message = new Builder(errorMessage, Severity.ERROR)
                    .code(errorCode)
                    .invalidObjectWithProperties(object, property)
                    .markers(Validation.MARKER_REQUIRED).create();
            return new MessageList(message);
        } else {
            return new MessageList();
        }
    }

    public static class RequiredInformationMissingMarker implements IMarker {

        @Override
        public boolean isRequiredInformationMissing() {
            return true;
        }

        @Override
        public boolean isTechnicalConstraintViolated() {
            return false;
        }

    }
}
