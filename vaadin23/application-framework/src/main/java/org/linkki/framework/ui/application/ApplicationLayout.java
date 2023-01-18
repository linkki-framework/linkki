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

import org.linkki.core.ui.converters.LinkkiConverterRegistry;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinSession;

/**
 * Overall router layout for the application. Contains the application header, the main work area, and a
 * footer.
 * 
 * @implNote To use this {@link ApplicationLayout} create a subclass to pass the
 *           {@link ApplicationConfig} via constructor.
 *           <p>
 *           The {@link ErrorHandler} can be customized by overriding the method
 *           {@link ApplicationConfig#getErrorHandler()}.
 */
@CssImport(value = "./styles/linkki-application-layout.css")
@CssImport(value = "./styles/linkki-vaadin-field.css", themeFor = "vaadin-*")
public abstract class ApplicationLayout extends VerticalLayout implements RouterLayout {

    private static final long serialVersionUID = 1L;

    private final VerticalLayout mainArea = new VerticalLayout();

    /**
     * Constructor for passing {@link ApplicationConfig}
     * 
     * @param config the custom {@link ApplicationConfig} to use
     */
    protected ApplicationLayout(ApplicationConfig config) {
        setMargin(false);
        setSpacing(false);
        setPadding(false);
        setSizeFull();

        UI.getCurrent().getElement().getThemeList().addAll(config.getDefaultVariants());

        // header
        ApplicationHeader header = config.getHeaderDefinition().create(config.getApplicationInfo(),
                                                                       config.getMenuItemDefinitions());
        header.init();
        add(header);

        mainArea.setPadding(false);
        mainArea.setMargin(false);
        mainArea.setSpacing(false);
        mainArea.setSizeFull();
        mainArea.addClassName("linkki-main-area");
        add(mainArea);
        setFlexGrow(1, mainArea);

        // optional footer
        config.getFooterDefinition().ifPresent(fd -> {
            ApplicationFooter footer = fd.create(config.getApplicationInfo());
            footer.init();
            add(footer);
        });

        // init linkki converters and the error handler
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        if (vaadinSession != null) {
            vaadinSession.setAttribute(LinkkiConverterRegistry.class, config.getConverterRegistry());
            vaadinSession.setErrorHandler(config.getErrorHandler());
        }
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        if (content != null) {
            mainArea.getElement().appendChild(content.getElement());
        } else {
            mainArea.getElement().removeAllChildren();
        }
    }

    @Override
    public void removeRouterLayoutContent(HasElement oldContent) {
        mainArea.getElement().removeAllChildren();
    }

}
