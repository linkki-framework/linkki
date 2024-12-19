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
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.linkki.core.ui.test.KaribuUIExtension.KaribuConfiguration.withDefaults;
import static org.linkki.core.ui.test.KaribuUtils.getTextContent;

import java.io.Serial;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.Route;

class LinkkiErrorPageTest {

    @AfterEach
    void vaadinTearDown() {
        MockVaadin.tearDown();
    }

    @Nested
    class ProductionMode {

        @RegisterExtension
        private static KaribuUIExtension extension = createKaribuUIExtension(LinkkiErrorPage.class, true);

        @Test
        void testNoExceptionDetails() {
            UI.getCurrent().navigate(ViewWithException.class);

            var testWrapper = new LinkkiErrorPageTestHelper();
            assertTitle(testWrapper.getTitle());
            assertThat(getTextContent(testWrapper.getMessage())).isEqualTo("An unexpected error occurred.");
            assertErrorDetails(testWrapper.getErrorDetails());
            assertThat(KaribuUtils.printComponentTree())
                    .doesNotContain(ViewWithException.EXCEPTION_MESSAGE)
                    .doesNotContain(ViewWithException.CAUSE_MESSAGE);
            assertNavigationButton(testWrapper.getNavigationButton());
        }

        @Test
        void testMessageException() {
            UI.getCurrent().navigate(ViewWithMessageException.class);

            var testWrapper = new LinkkiErrorPageTestHelper();
            assertTitle(testWrapper.getTitle());
            assertThat(testWrapper.size()).isEqualTo(4);
            assertThat(getTextContent(testWrapper.getMessage()))
                    .as("Text of MessageException should always be shown")
                    .isEqualTo(ViewWithMessageException.EXCEPTION_MESSAGE);
        }
    }

    @Nested
    class DevelopmentMode {

        @RegisterExtension
        private static KaribuUIExtension extension = createKaribuUIExtension(LinkkiErrorPage.class, false);

        @Test
        void testCustomMessageShown() {
            UI.getCurrent().navigate(ViewWithCustomMessageException.class);

            var testWrapper = new LinkkiErrorPageTestHelper();
            assertTitle(testWrapper.getTitle());
            assertThat(getTextContent(testWrapper.getMessage()))
                    .isEqualTo(ViewWithCustomMessageException.CUSTOM_MESSAGE);
            assertErrorDetails(testWrapper.getErrorDetails());
            assertThat(testWrapper.getStacktrace().getElement().getText())
                    .startsWith("java.lang.IllegalArgumentException");
            assertNavigationButton(testWrapper.getNavigationButton());
        }

        @Test
        void testExceptionDetailsShown() {
            UI.getCurrent().navigate(ViewWithException.class);

            var testWrapper = new LinkkiErrorPageTestHelper();
            assertTitle(testWrapper.getTitle());
            assertThat(getTextContent(testWrapper.getMessage())).isEqualTo(ViewWithException.EXCEPTION_MESSAGE);
            assertErrorDetails(testWrapper.getErrorDetails());
            assertThat(testWrapper.getStacktrace().getElement().getText())
                    .contains(ViewWithException.EXCEPTION_MESSAGE)
                    .contains(ViewWithException.CAUSE_MESSAGE)
                    .contains("java.lang.IllegalArgumentException");
            assertNavigationButton(testWrapper.getNavigationButton());
        }
    }

    @Nested
    class CustomSubclass {

        @RegisterExtension
        private static KaribuUIExtension extension = createKaribuUIExtension(CustomErrorPage.class, true);

        @Test
        void testCustomSubClass() {
            UI.getCurrent().navigate(ViewWithException.class);

            var testWrapper = new LinkkiErrorPageTestHelper();
            assertTitle(testWrapper.getTitle());
            assertThat(getTextContent(testWrapper.getMessage()))
                    .contains(CustomErrorPage.CUSTOM_MESSAGE)
                    .contains(ViewWithException.EXCEPTION_MESSAGE);
            assertThat(getTextContent(testWrapper.getErrorDetails()))
                    .contains(CustomErrorPage.CUSTOM_DETAILS)
                    .contains(ViewWithException.EXCEPTION_MESSAGE);
            assertThat(testWrapper.getStacktrace().getElement().getText())
                    .contains(ViewWithException.EXCEPTION_MESSAGE)
                    .contains(ViewWithException.CAUSE_MESSAGE)
                    .contains("java.lang.IllegalArgumentException");
            assertNavigationButton(testWrapper.getNavigationButton());
        }
    }

    private void assertTitle(Component title) {
        assertThat(title.getElement().getText()).isEqualTo("Ooops, something went wrong!");
    }

    private void assertErrorDetails(Component errorDetails) {
        var textContent = getTextContent(errorDetails);
        var uuidMatcher = Pattern.compile("Timestamp:.+ \\[([^]]+)]").matcher(textContent);
        var hasMatch = uuidMatcher.find();
        assertThat(hasMatch).as("UUID is displayed after the timestamp").isTrue();
        var uuid = uuidMatcher.group(1);
        assertThatNoException().as("The string displayed after timestamp is a valid UUID")
                .isThrownBy((() -> UUID.fromString(uuid)));
    }

    /**
     * This method executes and tests the navigation event triggered by the passed button. After
     * calling this method, the error page is not opened anymore.
     */
    private void assertNavigationButton(Component navigationButton) {
        assertThat(navigationButton).isInstanceOf(Button.class);
        assertThat(navigationButton.getElement().getText()).isEqualTo("Go to Start View");
        _click((Button)navigationButton);
        assertThat(UI.getCurrent().getCurrentView()).isInstanceOf(StartView.class);
    }

    private static KaribuUIExtension createKaribuUIExtension(Class<? extends HasErrorParameter<?>> errorView,
            boolean productionMode) {
        return KaribuUIExtension
                .withConfiguration(withDefaults()
                        .addRoutes(StartView.class, ViewWithException.class,
                                   ViewWithMessageException.class,
                                   ViewWithCustomMessageException.class)
                        .addErrorRoutes(errorView)
                        .setProductionMode(productionMode));
    }

    @Route("")
    public static class StartView extends Div {

        @Serial
        private static final long serialVersionUID = -8257983255778209425L;
    }

    @Route("exception")
    public static class ViewWithException extends Div implements BeforeEnterObserver {

        @Serial
        private static final long serialVersionUID = 1L;
        public static final String EXCEPTION_MESSAGE = "Test Message<br>with HTML";
        public static final String CAUSE_MESSAGE = "cause";

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
            throw new RuntimeException(EXCEPTION_MESSAGE, new IllegalArgumentException(CAUSE_MESSAGE));
        }
    }

    @Route("message")
    public static class ViewWithMessageException extends Div implements BeforeEnterObserver {

        @Serial
        private static final long serialVersionUID = 1L;
        public static final String EXCEPTION_MESSAGE = "message";
        public static final String CAUSE_MESSAGE = "cause";

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
            throw new MessageException(EXCEPTION_MESSAGE, new IllegalArgumentException(CAUSE_MESSAGE));
        }
    }

    @Route("custom-message")
    public static class ViewWithCustomMessageException extends Div implements BeforeEnterObserver {

        @Serial
        private static final long serialVersionUID = 1L;
        public static final String CUSTOM_MESSAGE = "custom-message";

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
            event.rerouteToError(IllegalArgumentException.class, CUSTOM_MESSAGE);
        }
    }

    public static class CustomErrorPage extends LinkkiErrorPage {
        @Serial
        private static final long serialVersionUID = 1L;
        public static final String CUSTOM_DETAILS = "custom details";
        public static final String CUSTOM_MESSAGE = "custom message";

        @Override
        protected Component createErrorDetails(ErrorParameter<Exception> parameter) {
            return new Span(CUSTOM_DETAILS + parameter.getCaughtException().getMessage());
        }

        @Override
        protected Component createErrorMessage(ErrorParameter<? extends Exception> parameter) {
            return new Span(CUSTOM_MESSAGE + parameter.getCaughtException().getMessage());
        }

        @Override
        protected boolean isDevelopmentMode() {
            return true;
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

        Component getErrorDetails() {
            return children.get(2);
        }

        Component getStacktrace() {
            assertThat(children).as("Page must have at least 5 children to show the error stacktrace").hasSize(5);
            return children.get(3);
        }

        Component getNavigationButton() {
            if (children.size() <= 4) {
                return children.get(3);
            }
            return children.get(4);
        }
    }
}
