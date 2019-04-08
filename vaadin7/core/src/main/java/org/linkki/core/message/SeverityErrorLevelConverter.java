/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.message;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.validation.message.Severity;

import com.vaadin.server.ErrorMessage.ErrorLevel;

public class SeverityErrorLevelConverter {

    private SeverityErrorLevelConverter() {
        // prevent instatiation of utiliy class
    }

    public static ErrorLevel convertToErrorLevel(Severity severity) {
        requireNonNull(severity, "severity must not be null");
        switch (severity) {
            case ERROR:
                return ErrorLevel.ERROR;
            case INFO:
                return ErrorLevel.INFORMATION;
            case WARNING:
                return ErrorLevel.WARNING;
            default:
                return ErrorLevel.INFORMATION;
        }
    }

    public static Severity convertToSeverity(ErrorLevel errorLevel) {
        requireNonNull(errorLevel, "errorLevel must not be null");
        switch (errorLevel) {
            case CRITICAL:
                return Severity.ERROR;
            case ERROR:
                return Severity.ERROR;
            case INFORMATION:
                return Severity.INFO;
            case SYSTEMERROR:
                return Severity.ERROR;
            case WARNING:
                return Severity.WARNING;
            default:
                return Severity.INFO;
        }
    }
}
