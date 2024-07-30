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

package org.linkki.framework.ui.error;

import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

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

    private static final String START_VIEW_SPAN_ID = "test-span";
    private static final String TEST_MESSAGE_WITH_HTML = "Test Message<br>with HTML";
    private static final String CUSTOM_MESSAGE = "CustomMessage";

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

        var testWrapper = new LinkkiErrorPageTestHelper();
        assertTitle(testWrapper.getTitle());
        assertErrorMessage(testWrapper.getMessage(), "An unknown error occurred.");
        assertErrorDetails(testWrapper.getErrorDetails());
        assertThat(_find(Div.class, div -> div.withClasses("error-page-stacktrace"))).isEmpty();
        assertNavigationButton(testWrapper.getNavigationButton());
    }

    @Test
    void testLinkkiErrorPage_ProductionMode_MessageException() {
        setupVaadin(true);

        // Manually load the error page since navigating to an unknown route causes Karibu to fail.
        UI.getCurrent().navigate(MessageErrorPageView.class);

        var testWrapper = new LinkkiErrorPageTestHelper();
        assertTitle(testWrapper.getTitle());
        assertThat(testWrapper.size()).isEqualTo(4);
        // Text of MessageException should always be shown
        assertErrorMessage(testWrapper.getMessage(), TEST_MESSAGE_WITH_HTML);
    }

    @Test
    void testLinkkiErrorPage_DevelopmentMode_CustomMessageShown() {
        setupVaadin(false);

        // Manually load the error page since navigating to an unknown route causes Karibu to fail.
        UI.getCurrent().navigate(CustomMessageErrorPageView.class);

        var testWrapper = new LinkkiErrorPageTestHelper();
        assertTitle(testWrapper.getTitle());
        assertErrorMessage(testWrapper.getMessage(), CUSTOM_MESSAGE);
        assertErrorDetails(testWrapper.getErrorDetails());
        assertThat(testWrapper.getStacktrace().getElement().getText()).startsWith("java.lang.IllegalArgumentException");
        assertNavigationButton(testWrapper.getNavigationButton());
    }

    @Test
    void testLinkkiErrorPage_DevelopmentMode_ExceptionDetailsShown() {
        setupVaadin(false);

        // Manually load the error page since navigating to an unknown route causes Karibu to fail.
        UI.getCurrent().navigate(ErrorPageView.class);

        var testWrapper = new LinkkiErrorPageTestHelper();
        assertTitle(testWrapper.getTitle());
        assertErrorMessage(testWrapper.getMessage(), TEST_MESSAGE_WITH_HTML);
        assertErrorDetails(testWrapper.getErrorDetails());
        assertThat(testWrapper.getStacktrace().getElement().getText()).startsWith("java.lang.IllegalArgumentException");
        assertNavigationButton(testWrapper.getNavigationButton());
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
        assertThat(errorDetailsText).contains("Timestamp:").doesNotContain("IllegalArgumentException");
    }

    /**
     * This method executes and tests the navigation event triggered by the passed button. After
     * calling this method, the error page is not opened anymore.
     */
    private void assertNavigationButton(Component navigationButton) {
        assertThat(navigationButton).isInstanceOf(Button.class);
        assertThat(navigationButton.getElement().getText()).isEqualTo("Go to Start View");
        _click((Button)navigationButton);
        assertThat(_find(Span.class, span -> span.withId(START_VIEW_SPAN_ID))).isNotEmpty();
    }

    /**
     * Mocks Vaadin for the current test instance with the given production mode and the given test
     * view.
     *
     * @param productionMode {@code true} whether production mode should be enabled
     */
    private void setupVaadin(boolean productionMode) {
        System.setProperty("vaadin.productionMode", String.valueOf(productionMode));
        MockVaadin.setup(new Routes(
                Set.of(StartView.class, ErrorPageView.class, MessageErrorPageView.class,
                       CustomMessageErrorPageView.class),
                Collections.emptySet(), true));
    }

    @Route("")
    public static class StartView extends Div {

        @Serial
        private static final long serialVersionUID = 1L;

        public StartView() {
            var span = new Span();
            span.setId(START_VIEW_SPAN_ID);
            add(span);
        }
    }

    abstract static class TestLinkkiErrorPageView extends Div {

        @Serial
        private static final long serialVersionUID = 1L;

        TestLinkkiErrorPageView(Exception exception, String customMessage) {
            var errorPage = new LinkkiErrorPage();
            var errorParameter = Optional.ofNullable(customMessage)
                    .map(m -> new ErrorParameter<>(Exception.class, exception, m))
                    .orElse(new ErrorParameter<>(Exception.class, exception));
            errorPage.setErrorParameter(null, errorParameter);
            add(errorPage);
        }

        TestLinkkiErrorPageView(Exception exception) {
            this(exception, null);
        }
    }

    @Route("error-page")
    public static class ErrorPageView extends TestLinkkiErrorPageView {
        @Serial
        private static final long serialVersionUID = 1L;

        public ErrorPageView() {
            super(new IllegalArgumentException(TEST_MESSAGE_WITH_HTML));
        }
    }

    @Route("message-error-page")
    public static class MessageErrorPageView extends TestLinkkiErrorPageView {
        @Serial
        private static final long serialVersionUID = 1L;

        public MessageErrorPageView() {
            super(new MessageException(TEST_MESSAGE_WITH_HTML));
        }
    }

    @Route("custom-message-error-page")
    public static class CustomMessageErrorPageView extends TestLinkkiErrorPageView {

        @Serial
        private static final long serialVersionUID = 1L;

        public CustomMessageErrorPageView() {
            super(new IllegalArgumentException(TEST_MESSAGE_WITH_HTML), CUSTOM_MESSAGE);
        }
    }

    static class LinkkiErrorPageTestHelper {

        private final List<Component> children;

        public LinkkiErrorPageTestHelper() {
            var errorPageContent = _get(Div.class, div -> div.withClasses("error-page-message"));
            children = errorPageContent.getChildren().toList();
        }

        int size() {
            return children.size();
        }

        Component getTitle() {
            return children.get(0);
        }

        Component getMessage() {
            return children.get(1);
        }

        Component getNavigationButton() {
            return children.get(2);
        }

        Component getErrorDetails() {
            return children.get(3);
        }

        Component getStacktrace() {
            assertThat(children).as("Page must have at least 5 children to show the error stacktrace").hasSize(5);
            return children.get(4);
        }
    }
}
