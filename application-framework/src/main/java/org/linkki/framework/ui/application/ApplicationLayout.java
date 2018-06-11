/*
 * Copyright Faktor Zehn AG.
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

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;

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

    static final EmptyView EMPTY_VIEW = new EmptyView();

    private static final long serialVersionUID = 1L;

    private Component mainArea = EMPTY_VIEW;

    /**
     * Creates a new {@link ApplicationLayout} with the given {@link ApplicationHeader header} and
     * {@link ApplicationFooter footer}.
     */
    private ApplicationLayout(ApplicationHeader header, @Nullable ApplicationFooter footer) {
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
        newMainArea.setSizeFull();
        addComponent(newMainArea, 1);
        setExpandRatio(newMainArea, 1);
    }

    /**
     * Returns the view that is currently displayed if there is any.
     */
    protected <T extends View & Component> T getCurrentView() {
        @SuppressWarnings("unchecked")
        T view = (T)mainArea;
        return view;
    }

    public static Builder<?> create() {
        return new Builder<>();
    }

    public static class Builder<T extends Builder<T>> {

        @CheckForNull
        private ApplicationHeader header;

        @CheckForNull
        private ApplicationFooter footer;

        @CheckForNull
        private ApplicationMenu applicationMenu;

        @SuppressWarnings("unchecked")
        private T self() {
            return (T)this;
        }

        public T withHeader(@SuppressWarnings("hiding") ApplicationHeader header) {
            this.header = header;
            return self();
        }

        public T withFooter(@SuppressWarnings("hiding") ApplicationFooter footer) {
            this.footer = footer;
            return self();
        }

        public T withMenuItems(ApplicationMenuItemDefinition... itemDefs) {
            withMenu(new ApplicationMenu(itemDefs));
            return self();
        }

        public T withMenu(@SuppressWarnings("hiding") ApplicationMenu applicationMenu) {
            this.applicationMenu = applicationMenu;
            return self();
        }


        @SuppressWarnings({ "null", "synthetic-access" })
        public ApplicationLayout build() {
            if (header == null) {
                if (applicationMenu == null) {
                    applicationMenu = new ApplicationMenu();
                }
                header = new ApplicationHeader(applicationMenu);
            }
            return new ApplicationLayout(header, footer);
        }
    }

}
