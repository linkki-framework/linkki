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
package org.linkki.framework.ui.application;

import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.navigator.Navigator.EmptyView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * Overall layout frame for the application. Contains the application header, the main work area,
 * and a footer.
 */
public class ApplicationLayout extends VerticalLayout implements ViewDisplay {

    private static final long serialVersionUID = 1L;

    private Component mainArea = new EmptyView();

    /**
     * Creates a new {@link ApplicationLayout} with the given {@link ApplicationHeader header} and
     * (optional) {@link ApplicationFooter footer}.
     */
    public ApplicationLayout(ApplicationHeader header, @Nullable ApplicationFooter footer) {
        setMargin(false);
        setSizeFull();

        // Header
        header.init();
        addComponent(header);

        // Set an empty main area. Will be exchanged by showView(View)
        setMainArea(mainArea);

        // Footer
        if (footer != null) {
            footer.init();
            addComponent(footer);
        }
    }

    @Override
    public void showView(View view) {
        if (view instanceof Component) {
            setMainArea((Component)view);
        } else {
            throw new IllegalArgumentException(
                    "View is not a component: " + view);
        }
    }

    private void setMainArea(Component newMainArea) {
        removeComponent(mainArea);
        mainArea = newMainArea;
        addComponent(newMainArea, 1);
        setExpandRatio(newMainArea, 1);
    }

    /**
     * Returns the view that is currently displayed, if there is any.
     */
    protected <T extends View & Component> T getCurrentView() {
        @SuppressWarnings("unchecked")
        T view = (T)mainArea;
        return view;
    }

}
