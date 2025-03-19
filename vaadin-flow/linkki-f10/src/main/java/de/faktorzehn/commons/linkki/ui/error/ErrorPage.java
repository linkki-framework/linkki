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

package de.faktorzehn.commons.linkki.ui.error;

import java.io.Serial;
import java.util.List;

import org.linkki.core.nls.NlsService;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.framework.ui.application.ApplicationInfo;
import org.linkki.framework.ui.application.ApplicationLayout;
import org.linkki.framework.ui.error.LinkkiErrorPage;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.server.HttpStatusCode;

/**
 * Customizable abstract error page whose implementations handle specific types of exceptions. An
 * implementation will be automatically registered with Vaadin.
 * <p>
 * If the {@link BeforeEnterEvent} is available, for clarity reasons, we recommend using one of the
 * overloaded {@link BeforeEnterEvent#rerouteToError(Exception, String)} methods, instead of
 * throwing an exception of Type T, to reroute to an implementation this page.
 * <p>
 * Displays the {@link ErrorParameter}'s {@link ErrorParameter#getCustomMessage() custom message} if
 * {@link ErrorParameter#hasCustomMessage()} or the {@link ErrorParameter#getException()
 * Exception}'s message otherwise.
 * <p>
 * To add an {@link ApplicationLayout}, annotate the implementation with
 * {@code @ParentLayout(MyApplicationLayout.class)}.
 * <p>
 * For customization, see
 * <ul>
 * <li>{@link #createContent(BeforeEnterEvent, ErrorParameter)}
 * <li>{@link #createMessage(BeforeEnterEvent, ErrorParameter)}
 * <li>{@link #getStatusCode()}
 * </ul>
 *
 * @param <T> Exception type handled by this ErrorPage.
 *
 * @deprecated This class has been deprecated in favor of the {@link LinkkiErrorPage} class in
 *             'linkki'.
 */
@Deprecated(since = "24.1")
@CssImport("./error-page.css")
public abstract class ErrorPage<T extends Exception> extends VerticalLayout
        implements HasErrorParameter<T>, HasDynamicTitle {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String BUNDLE_NAME = "de/faktorzehn/commons/linkki/messages";
    private static final String MSG_KEY_TITLE = "ErrorPage.Title";
    private static final String MSG_KEY_GO_TO_START_VIEW = "ErrorPage.GoToStartView";

    private final ApplicationInfo applicationInfo;
    private Div contentWrapper;

    /**
     * Creates a ErrorPage for the given {@link ApplicationInfo}. By default, its
     * {@link ApplicationInfo#getApplicationName() application name} will be used as
     * {@link #getPageTitle() page title}.
     */
    protected ErrorPage(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
        setId(getClass().getSimpleName());

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        addClassName("error-page");

        Div wrapper = new Div();
        add(wrapper);

        Div iconWrapper = new Div();
        iconWrapper.addClassName("error-page-icon");
        iconWrapper.addClassName("linkki-message-error");
        wrapper.add(iconWrapper);

        Icon icon = VaadinIcon.EXCLAMATION_CIRCLE.create();
        iconWrapper.add(icon);

        contentWrapper = new Div();
        contentWrapper.addClassName("error-page-message");
        wrapper.add(contentWrapper);
    }

    @Override
    public String getPageTitle() {
        return applicationInfo.getApplicationName();
    }

    /**
     * Override this method if you want to change the page's whole content.
     *
     * @see #createContent(BeforeEnterEvent, ErrorParameter)
     * @see #createMessage(BeforeEnterEvent, ErrorParameter)
     * @see #getStatusCode()
     */
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<T> parameter) {

        contentWrapper.removeAll();

        H4 title = new H4(localize(MSG_KEY_TITLE));
        contentWrapper.add(title);

        contentWrapper.add(createContent(event, parameter));

        return getStatusCode();
    }

    /**
     * Override this method to customize the page's content.
     *
     * @param event the event passed to {@link #setErrorParameter(BeforeEnterEvent, ErrorParameter)}
     * @param parameter the parameter passed to
     *            {@link #setErrorParameter(BeforeEnterEvent, ErrorParameter)}
     *
     * @return list of components to add to the page's content.
     *
     * @see #createMessage(BeforeEnterEvent, ErrorParameter)
     */
    protected List<Component> createContent(BeforeEnterEvent event, ErrorParameter<T> parameter) {
        var message = createMessage(event, parameter);

        Button goToStartViewButton = new Button(localize(MSG_KEY_GO_TO_START_VIEW));
        goToStartViewButton.addClickListener(e -> UI.getCurrent().navigate(""));

        return List.of(message, goToStartViewButton);
    }

    /**
     * Creates the message of this page's content with the ErrorParameter's
     * {@link ErrorParameter#getCustomMessage() custom massage} if
     * {@link ErrorParameter#hasCustomMessage() available}, or to the {@link Exception#getMessage()
     * message} of the ErrorParameter's {@link ErrorParameter#getException()} otherwise.
     * <p>
     * Override this method to customize the text.
     *
     * @param event The {@link BeforeEnterEvent} for subclasses to get event information like the
     *            location
     */
    protected Component createMessage(BeforeEnterEvent event, ErrorParameter<T> parameter) {
        var message = new LinkkiText();
        if (parameter.hasCustomMessage()) {
            message.setText(parameter.getCustomMessage());
        } else {
            message.setText(parameter.getException().getMessage());
        }
        return message;
    }

    /**
     * Override this method to let {@link #setErrorParameter(BeforeEnterEvent, ErrorParameter)}
     * return an HTTP status code other than the default 400 BAD REQUEST.
     */
    protected int getStatusCode() {
        return HttpStatusCode.BAD_REQUEST.getCode();
    }

    private String localize(String key) {
        return NlsService.get().getString(BUNDLE_NAME, key).orElseGet(() -> '!' + key + '!');
    }
}
