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

package org.linkki.framework.ui.error;

import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serial;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.linkki.core.vaadin.component.base.LinkkiText;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.Route;

class LinkkiErrorPageTest {

    private static final String ROUTE_START_VIEW = StringUtils.EMPTY;
    private static final String ROUTE_ERROR_PAGE_TEST_VIEW = "error-page";
    private static final String ROUTE_ERROR_PAGE_TEST_MSG_EX = "error-page-message-exception";
    private static final String ROUTE_ERROR_PAGE_TEST_MSG_EX_CAUSE = "error-page-message-exception-cause";

    private static final String START_VIEW_SPAN_ID = "test-span";
    private static final String TEST_MESSAGE_WITH_HTML = "Test Message<br>with HTML";
    public static final String MESSAGE_EXCEPTION_FOR_DISPLAY = "Message exception for display";

    @BeforeAll
    static void setupLanguage() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterEach
    void vaadinTearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testLinkkiErrorPage_ProductionMode_NoExceptionDetails() {
        setupVaadin(true);

        // Manually load the error page since navigating to an unknown route causes Karibu to fail.
        UI.getCurrent().navigate(ErrorPageView.class);

        var errorPageContent = _get(Div.class, div -> div.withClasses("error-page-message"));
        var children = errorPageContent.getChildren().toList();
        assertThat(children).hasSize(4);
        var title = children.get(0);
        var message = children.get(1);
        var navigationButton = children.get(2);
        var errorDetails = children.get(3);
        assertTitle(title);
        assertErrorMessage(message, "An unknown error occurred.");
        assertErrorDetailsProduction(errorDetails);
        assertThat(_find(Div.class, div -> div.withClasses("error-page-stacktrace"))).isEmpty();
        assertNavigationButton(navigationButton);
    }

    @Test
    void testLinkkiErrorPage_DevelopmentMode_ExceptionDetailsShown() {
        setupVaadin(false);

        // Manually load the error page since navigating to an unknown route causes Karibu to fail.
        UI.getCurrent().navigate(ErrorPageView.class);

        var errorPageContent = _get(Div.class, div -> div.withClasses("error-page-message"));
        var children = errorPageContent.getChildren().toList();
        assertThat(children).hasSize(5);
        var title = children.get(0);
        var message = children.get(1);
        var navigationButton = children.get(2);
        var errorDetails = children.get(3);
        var stacktrace = children.get(4);
        assertTitle(title);
        assertErrorMessage(message, TEST_MESSAGE_WITH_HTML);
        assertErrorDetails(errorDetails);
        assertThat(stacktrace.getElement().getText()).startsWith("java.lang.IllegalArgumentException");
        assertNavigationButton(navigationButton);
    }

    @Test
    void testLinkkiErrorPage_DevelopmentMode_MessageException() {
        setupVaadin(false);
        UI.getCurrent().navigate(ErrorPageViewMessageException.class);

        var errorPageContent = _get(Div.class, div -> div.withClasses("error-page-message"));
        var children = errorPageContent.getChildren().toList();

        var title = children.get(0);
        var message = children.get(1);
        var navigationButton = children.get(2);
        var errorDetails = children.get(3);
        var stacktrace = children.get(4);

        assertTitle(title);
        assertErrorMessageForMessageException(message);
        assertErrorDetails(errorDetails);
        assertNavigationButton(navigationButton);
        assertThat(stacktrace.getElement().getText()).startsWith("org.linkki.framework.ui.error.MessageException");
    }

    @Test
    void testLinkkiErrorPage_DevelopmentMode_MessageExceptionAndCause() {
        setupVaadin(false);
        UI.getCurrent().navigate(ErrorPageViewMessageExceptionWithCause.class);

        var errorPageContent = _get(Div.class, div -> div.withClasses("error-page-message"));
        var children = errorPageContent.getChildren().toList();

        var title = children.get(0);
        var message = children.get(1);
        var navigationButton = children.get(2);
        var errorDetails = children.get(3);
        var stacktrace = children.get(4);

        assertTitle(title);
        assertThat(message).isInstanceOf(LinkkiText.class);
        LinkkiText messageTextComponent = (LinkkiText)message;
        String actualMessage = messageTextComponent.getText();
        assertThat(actualMessage).isEqualTo("Message exception for display<br>Additional Info: UnknownException<br>");
        assertErrorDetails(errorDetails);
        assertNavigationButton(navigationButton);
        assertThat(stacktrace.getElement().getText()).startsWith("org.linkki.framework.ui.error.MessageException");
    }

    @Test
    void testLinkkiErrorPage_ProductionMode_MessageExceptionAndCause() {
        setupVaadin(true);
        UI.getCurrent().navigate(ErrorPageViewMessageExceptionWithCause.class);

        var errorPageContent = _get(Div.class, div -> div.withClasses("error-page-message"));
        var children = errorPageContent.getChildren().toList();

        var title = children.get(0);
        var message = children.get(1);
        var navigationButton = children.get(2);
        var errorDetails = children.get(3);

        assertTitle(title);
        assertErrorMessage(message, "Message exception for display<br>Additional Info: UnknownException<br>");
        assertErrorDetailsProduction(errorDetails);
        assertThat(_find(Div.class, div -> div.withClasses("error-page-stacktrace"))).isEmpty();
        assertNavigationButton(navigationButton);
    }

    private void assertTitle(Component title) {
        assertThat(title.getElement().getText()).isEqualTo("Ooops, something went wrong!");
    }

    private void assertErrorMessage(Component message, String expectedMessage) {
        assertThat(message).isInstanceOf(LinkkiText.class);
        var errorMessage = ((LinkkiText)message).getText();
        assertThat(errorMessage).isEqualTo(expectedMessage);
    }

    private void assertErrorDetails(Component errorDetails) {
        assertThat(errorDetails).isInstanceOf(LinkkiText.class);
        var errorDetailsText = ((LinkkiText)errorDetails).getText();
        assertThat(errorDetailsText).contains("Timestamp:", "Cause:");
    }

    private void assertErrorDetailsProduction(Component errorDetails) {
        assertThat(errorDetails).isInstanceOf(LinkkiText.class);
        var errorDetailsText = ((LinkkiText)errorDetails).getText();
        assertThat(errorDetailsText).contains("Timestamp:");
        assertThat(errorDetailsText).doesNotContain("Cause:");
    }


    /**
     * This method executes and tests the navigation event triggered by the passed button. After calling
     * this method, the error page is not opened anymore.
     */
    private void assertNavigationButton(Component navigationButton) {
        assertThat(navigationButton).isInstanceOf(Button.class);
        assertThat(navigationButton.getElement().getText()).isEqualTo("Go to Start View");
        _click((Button)navigationButton);
        assertThat(_find(Span.class, span -> span.withId(START_VIEW_SPAN_ID))).isNotEmpty();
    }

    private void assertErrorMessageForMessageException(Component messageComponent) {
        assertThat(messageComponent).isInstanceOf(LinkkiText.class);
        LinkkiText messageTextComponent = (LinkkiText)messageComponent;
        String actualMessage = messageTextComponent.getText();
        assertThat(actualMessage).contains(MESSAGE_EXCEPTION_FOR_DISPLAY);
    }

    /**
     * Mocks Vaadin for the current test instance with the given production mode and the given test
     * view.
     *
     * @param productionMode {@code true} whether production mode should be enabled
     */
    private void setupVaadin(boolean productionMode) {
        System.setProperty("vaadin.productionMode", String.valueOf(productionMode));
        MockVaadin.setup(new Routes(Stream.of(StartView.class, ErrorPageView.class,
                        ErrorPageViewMessageException.class, ErrorPageViewMessageExceptionWithCause.class)
                        .collect(Collectors.toSet()),
                Collections.emptySet(), true));
    }

    @Route(value = ROUTE_START_VIEW)
    public static class StartView extends Div {

        @Serial
        private static final long serialVersionUID = 1L;

        public StartView() {
            var span = new Span();
            span.setId(START_VIEW_SPAN_ID);
            add(span);
        }
    }

    @Route(value = ROUTE_ERROR_PAGE_TEST_VIEW)
    public static class ErrorPageView extends Div {

        @Serial
        private static final long serialVersionUID = 1L;

        public ErrorPageView() {
            var errorPage = new LinkkiErrorPage();
            var errorParameter = new ErrorParameter<>(Exception.class,
                    new IllegalArgumentException(TEST_MESSAGE_WITH_HTML));
            errorPage.setErrorParameter(null, errorParameter);
            add(errorPage);
        }
    }

    @Route(value = ROUTE_ERROR_PAGE_TEST_MSG_EX)
    public static class ErrorPageViewMessageException extends Div {

        @Serial
        private static final long serialVersionUID = 1L;

        public ErrorPageViewMessageException() {
            var errorPage = new LinkkiErrorPage();
            var errorParameter = new ErrorParameter<>(Exception.class,
                    new MessageException(MESSAGE_EXCEPTION_FOR_DISPLAY));
            errorPage.setErrorParameter(null, errorParameter);
            add(errorPage);
        }
    }

    @Route(value = ROUTE_ERROR_PAGE_TEST_MSG_EX_CAUSE)
    public static class ErrorPageViewMessageExceptionWithCause extends Div {

        @Serial
        private static final long serialVersionUID = 1L;
        private static final String TEST_CAUSE_MESSAGE = "UnknownException";

        public ErrorPageViewMessageExceptionWithCause() {
            var errorPage = new LinkkiErrorPage();
            Throwable cause = new Exception(TEST_CAUSE_MESSAGE);
            var errorParameter = new ErrorParameter<>(Exception.class,
                    new MessageException(MESSAGE_EXCEPTION_FOR_DISPLAY, cause));
            errorPage.setErrorParameter(null, errorParameter);
            add(errorPage);
        }

    }
}
