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

package org.linkki.framework.ui.dialogs;

import java.io.Serial;
import java.util.UUID;

import com.vaadin.flow.server.ErrorEvent;

/**
 * A wrapper for a {@link com.vaadin.flow.server.ErrorEvent} that has a unique ID for the caught
 * exception.
 */
public class IdErrorEvent extends ErrorEvent {

    @Serial
    private static final long serialVersionUID = -7807881142804245128L;

    private final String exceptionId;

    /**
     * Creates a new instance using a new random UUID.
     */
    public IdErrorEvent(ErrorEvent errorEvent) {
        this(errorEvent, UUID.randomUUID().toString());
    }

    /**
     * Creates a new instance using the given ID.
     */
    public IdErrorEvent(ErrorEvent errorEvent, String exceptionId) {
        super(errorEvent.getThrowable());
        this.exceptionId = exceptionId;
    }

    public String getExceptionId() {
        return exceptionId;
    }
}
