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

import java.io.Serial;

import org.linkki.framework.ui.error.MessageException;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route(value = ErrorPageRedirector.ROUTE)
public class ErrorPageRedirector extends Div implements BeforeEnterObserver {

    public static final String ROUTE = "error-page-redirect";
    public static final String PARAMETER_ACTION = "action";

    public static final String ACTION_RUNTIME_EXCEPTION = "runtime-exception";
    public static final String ACTION_RUNTIME_EXCEPTION_WITH_MESSAGE = "runtime-exception-with-message";
    public static final String ACTION_MESSAGE_EXCEPTION = "message-exception";
    public static final String ACTION_MESSAGE_EXCEPTION_WITH_CAUSE = "message-exception-with-cause";

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var action = event.getLocation().getQueryParameters()
                .getSingleParameter(PARAMETER_ACTION)
                .orElse("");

        switch (action) {
            case ACTION_MESSAGE_EXCEPTION:
                throw new MessageException("User-friendly message");
            case ACTION_MESSAGE_EXCEPTION_WITH_CAUSE:
                var cause = new RuntimeException("Technical cause");
                throw new MessageException("User-friendly message", cause);
            case ACTION_RUNTIME_EXCEPTION_WITH_MESSAGE:
                var exception = new RuntimeException("Internal error");
                event.rerouteToError(exception, "Custom message");
                return;
            case ACTION_RUNTIME_EXCEPTION:
                // fall-through
            default:
                throw new RuntimeException("Internal error");
        }
    }
}
