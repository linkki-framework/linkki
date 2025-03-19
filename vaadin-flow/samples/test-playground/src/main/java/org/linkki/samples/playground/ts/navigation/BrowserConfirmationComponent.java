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
package org.linkki.samples.playground.ts.navigation;

import java.io.Serial;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;

public class BrowserConfirmationComponent extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = 3917640539748989669L;

    public BrowserConfirmationComponent() {
        var button = new Button("Go to view", e -> UI.getCurrent().navigate(SampleBrowserConfirmationView.class,
                                                                            new RouteParameters(
                                                                                    new RouteParam("id", "1"))));
        button.setId("goToView");
        add(button);
    }

}
