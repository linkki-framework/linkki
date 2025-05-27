/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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
