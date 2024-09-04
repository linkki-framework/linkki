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
package org.linkki.samples.playground.bugs.lin3846;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;

public class ReactRouterTestView extends VerticalLayout implements BeforeEnterObserver, BeforeLeaveObserver {

    public static final String CAPTION = "LIN-3846";

    private static final long serialVersionUID = 1L;

    public ReactRouterTestView() {
        add(new H4(CAPTION));
        add(new Div(
                "When using React router, History#replaceState and History#pushState should not trigger navigation events. " +
                "No \"beforeEnter\" or \"beforeLeave\" should appear when clicking any of the buttons."));

        add(new Button("replace state - no query params",
                event -> UI.getCurrent().getPage().getHistory().replaceState(null, "bugs/lin-3846")));
        // no navigation only if the current URL is the same including query params
        add(new Button("replace state - push query param",
                event -> UI.getCurrent().getPage().getHistory().replaceState(null, "bugs/LIN-3846?new")));
        // no navigation only if the current URL is the same including query params
        add(new Button("replace state - change path param",
                event -> UI.getCurrent().getPage().getHistory().replaceState(null, "bugs/LIN-3846/new")));
        // no navigation only if the current URL is the same including query params
        add(new Button("push state - no query params",
                event -> UI.getCurrent().getPage().getHistory().pushState(null, "bugs/LIN-3846")));
        // no navigation
        add(new Button("push state - query param",
                event -> UI.getCurrent().getPage().getHistory().pushState(null, "bugs/LIN-3846?new")));
        // no navigation
        add(new Button("push state - change path param",
                event -> UI.getCurrent().getPage().getHistory().pushState(null, "bugs/LIN-3846/new")));
        // no navigation
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        add(new Div("beforeEnter"));
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
        add(new Div("beforeLeave"));
    }
}