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
package org.linkki.framework.ui.application;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Overall layout frame for the application. Contains the application header, the main work area, and a
 * footer.
 */
public class ApplicationLayout extends VerticalLayout implements RouterLayout {

    private static final long serialVersionUID = 1L;

    private Component mainArea = new Div();

    /**
     * Creates a new {@link ApplicationLayout} with the given {@link ApplicationHeader header} and
     * (optional) {@link ApplicationFooter footer}.
     */
    public ApplicationLayout(ApplicationHeader header, @CheckForNull ApplicationFooter footer) {
        setMargin(false);
        setSpacing(false);
        setSizeFull();

        // Header
        header.init();
        add(header);

        // Set an empty main area. Will be exchanged by showView(View)
        showView(mainArea);

        // Footer
        if (footer != null) {
            footer.init();
            add(footer);
        }
    }

    public void showView(Component component) {
        remove(mainArea);
        mainArea = component;
        addComponentAtIndex(1, component);
        setFlexGrow(1, component);
    }

    /**
     * Returns the view that is currently displayed, if there is any.
     */
    protected <T extends Component> T getCurrentView() {
        @SuppressWarnings("unchecked")
        T view = (T)mainArea;
        return view;
    }

}
