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
import org.linkki.framework.state.ApplicationConfig;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.dialogs.DefaultErrorDialog;
import org.linkki.framework.ui.dialogs.DialogErrorHandler;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinSession;

/**
 * Overall router layout for the application. Contains the application header, the main work area, and a
 * footer. To use this {@link ApplicationLayout} create a subclass and implement the
 * {@link #getApplicationConfig()}.
 * <p>
 * The {@link ErrorHandler} can be customized by overriding the method {@link #getErrorHandler()}.
 */
@CssImport(value = "./styles/linkki-application-layout.css", include = "@vaadin/vaadin-lumo-styles/all-imports")
public abstract class ApplicationLayout extends VerticalLayout implements RouterLayout {

    private static final long serialVersionUID = 1L;

    private VerticalLayout mainArea = new VerticalLayout();

    protected ApplicationLayout() {
        setMargin(false);
        setSpacing(false);
        setPadding(false);
        setSizeFull();

        ApplicationConfig config = getApplicationConfig();

        // header
        ApplicationHeader header = config.getHeaderDefinition()
                .apply(new ApplicationMenu(config.getMenuItemDefinitions().list()),
                       config);
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
            ApplicationFooter footer = fd.apply(config);
            footer.init();
            add(footer);
        });

        // init linkki converters and the error handler
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        if (vaadinSession != null) {
            vaadinSession.setAttribute(LinkkiConverterRegistry.class, config.getConverterRegistry());
            vaadinSession.setErrorHandler(getErrorHandler());
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

    /**
     * Returns the {@link ApplicationConfig} to be used to configure this {@link ApplicationLayout}.
     */
    public abstract ApplicationConfig getApplicationConfig();

    /**
     * Configures the error handling. Override to use a different {@link ErrorHandler}.
     * 
     * @implNote linkki uses a {@link DialogErrorHandler} creating a new {@link DefaultErrorDialog} by
     *           default.
     */
    protected ErrorHandler getErrorHandler() {
        return new DialogErrorHandler(DefaultErrorDialog::new);
    }

}
