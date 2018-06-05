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

import org.linkki.core.ui.converters.LinkkiConverterFactory;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.util.Sequence;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.navigator.Navigator.EmptyView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * Overall layout frame for the application. Contains the application header, the main work area,
 * and a footer.
 */
public class ApplicationLayout extends VerticalLayout implements ViewDisplay {

    static final EmptyView EMPTY_VIEW = new EmptyView();

    private static final long serialVersionUID = 1L;

    /** The header that is displayed at the top. */
    private final ApplicationHeader header;

    /** The footer that is displayed at the bottom. */
    @CheckForNull
    private final ApplicationFooter footer;

    private final Sequence<Converter<?, ?>> converters;

    private Component mainArea = EMPTY_VIEW;

    private ApplicationLayout(ApplicationHeader header, ApplicationFooter footer) {
        this(header, footer, LinkkiConverterFactory.DEFAULT_JAVA_8_DATE_CONVERTERS);
    }

    /**
     * Creates a new {@link ApplicationLayout} with the given {@link ApplicationHeader header} and
     * {@link ApplicationFooter footer} and registers the supplied {@link Converter Converters} with the
     * {@link VaadinSession}.
     * <p>
     * You can either supply converters to this constructor or overwrite {@link #getConverters()}.
     */
    private ApplicationLayout(ApplicationHeader header, ApplicationFooter footer,
            Sequence<Converter<?, ?>> converters) {
        this.header = header;
        this.footer = footer;
        this.converters = converters;
        init();
    }

    private void init() {
        // init converters
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        if (vaadinSession != null) {
            vaadinSession.setConverterFactory(new LinkkiConverterFactory(this::getConverters));
        }

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
     * Returns the {@link Converter converters} to be
     * {@link VaadinSession#setConverterFactory(com.vaadin.data.util.converter.ConverterFactory)
     * registered in the Vaadin session}.
     * <p>
     * By default, the {@link LinkkiConverterFactory#DEFAULT_JAVA_8_DATE_CONVERTERS} are returned.
     */
    protected Sequence<Converter<?, ?>> getConverters() {
        return converters;
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
        private Sequence<Converter<?, ?>> converters;

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

        public T withConverters(@SuppressWarnings("hiding") Sequence<Converter<?, ?>> converters) {
            this.converters = converters;
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
            if (converters == null) {
                return new ApplicationLayout(header, footer);
            } else {
                return new ApplicationLayout(header, footer, converters);
            }

        }
    }

}
