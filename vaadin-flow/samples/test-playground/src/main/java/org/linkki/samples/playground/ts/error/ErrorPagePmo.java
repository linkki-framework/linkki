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

package org.linkki.samples.playground.ts.error;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.QueryParameters;

@UISection
public class ErrorPagePmo {

    @UIButton(position = 10, label = "MessageException", caption = "Trigger")
    public void messageException() {
        redirectToError(ErrorPageRedirector.ACTION_MESSAGE_EXCEPTION);
    }

    @UIButton(position = 20, label = "MessageException with cause", caption = "Trigger")
    public void messageExceptionWithCause() {
        redirectToError(ErrorPageRedirector.ACTION_MESSAGE_EXCEPTION_WITH_CAUSE);
    }

    @UIButton(position = 30, label = "RuntimeException", caption = "Trigger")
    public void runtimeException() {
        redirectToError(ErrorPageRedirector.ACTION_RUNTIME_EXCEPTION);
    }

    @UIButton(position = 40, label = "RuntimeException with message", caption = "Trigger")
    public void runtimeExceptionWithMessage() {
        redirectToError(ErrorPageRedirector.ACTION_RUNTIME_EXCEPTION_WITH_MESSAGE);
    }

    private void redirectToError(String action) {
        var params = QueryParameters.of(ErrorPageRedirector.PARAMETER_ACTION, action);
        UI.getCurrent().navigate(ErrorPageRedirector.class, params);
    }

}
