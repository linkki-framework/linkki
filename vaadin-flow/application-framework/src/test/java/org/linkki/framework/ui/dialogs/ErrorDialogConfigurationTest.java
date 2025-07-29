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

package org.linkki.framework.ui.dialogs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.ui.test.KaribuUIExtension.KaribuConfiguration.withDefaults;

import java.io.Serial;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.ErrorEvent;

class ErrorDialogConfigurationTest {

    private static final String ROUTE_DEFAULT_TEST_VIEW = StringUtils.EMPTY;
    private static final String ROUTE_CUSTOM_TEST_VIEW = "route";

    private static final String TIMESTAMP_STRING = "Timestamp:";
    private static final String DEFAULT_ERROR_MESSAGE = "An unexpected error occurred.";
    private static final String EXCEPTION_MESSAGE = "message";
    private static final String EXCEPTION_ID = "id";

    ErrorEvent errorEvent;
    Throwable exception;

    /**
     * Creates a new error event as in dialog error handler
     */
    @BeforeEach
    void initErrorEvent() {
        exception = new RuntimeException(EXCEPTION_MESSAGE);
        errorEvent = new IdErrorEvent(new ErrorEvent(exception), EXCEPTION_ID);
    }

    @Nested
    class DevelopmentMode {

        @RegisterExtension
        private KaribuUIExtension extension = KaribuUIExtension
                .withConfiguration(withDefaults().addRoutes(DefaultTestView.class, CustomTestView.class)
                        .setProductionMode(false));

        @Test
        void testGetDialogContent_Default() {
            var content = ErrorDialogConfiguration.createWithHandlerNavigatingTo(ROUTE_DEFAULT_TEST_VIEW)
                    .getDialogContent(errorEvent);

            assertThat(content).element(0).extracting(KaribuUtils::getTextContent).isEqualTo(DEFAULT_ERROR_MESSAGE);
            assertThat(content).element(1).extracting(KaribuUtils::getTextContent).asString()
                    .contains(TIMESTAMP_STRING)
                    .contains(EXCEPTION_ID);
            assertThat(content).element(2).extracting(KaribuUtils::getTextContent).asString()
                    .contains(exception.getClass().getSimpleName())
                    .contains(exception.getMessage());
            assertThat(content).element(3).extracting(KaribuUtils::getTextContent)
                    .isEqualTo(ExceptionUtils.getStackTrace(exception));
        }

        @Test
        void testCaption() {
            testGetCaption();
        }
    }

    @Nested
    class ProductionMode {

        @RegisterExtension
        private KaribuUIExtension extension = KaribuUIExtension
                .withConfiguration(withDefaults().addRoutes(DefaultTestView.class, CustomTestView.class)
                        .setProductionMode(true));

        @Test
        void testGetDialogContent_Default() {
            var content = ErrorDialogConfiguration.createWithHandlerNavigatingTo(ROUTE_DEFAULT_TEST_VIEW)
                    .getDialogContent(errorEvent);

            assertThat(content).element(0).extracting(KaribuUtils::getTextContent).isEqualTo(DEFAULT_ERROR_MESSAGE);
            assertThat(content).element(1).extracting(KaribuUtils::getTextContent).asString()
                    .contains(TIMESTAMP_STRING)
                    .contains(EXCEPTION_ID);
            assertThat(content)
                    .map(KaribuUtils::printComponentTree)
                    .noneMatch(m -> m.contains(EXCEPTION_MESSAGE));
        }

        @Test
        void testGetDialogContent_AllCustomized() {
            var customCaption = "caption";
            var customErrorMessage = "message";
            // use a config which is the opposite of the default
            var config = ErrorDialogConfiguration.createWithHandlerNavigatingTo(ROUTE_CUSTOM_TEST_VIEW)
                    .withCaption(customCaption)
                    .withErrorMessage(customErrorMessage)
                    .showExceptionMessage()
                    .showExceptionStacktrace();

            var content = config.getDialogContent(errorEvent);

            assertThat(content).element(0).extracting(KaribuUtils::getTextContent).isEqualTo(customErrorMessage);
            assertThat(content).element(1).extracting(KaribuUtils::getTextContent).asString()
                    .contains(TIMESTAMP_STRING)
                    .contains(EXCEPTION_ID);
            assertThat(content).element(2).extracting(KaribuUtils::getTextContent).asString()
                    .contains(exception.getClass().getSimpleName())
                    .contains(exception.getMessage());
            assertThat(content).element(3).extracting(KaribuUtils::getTextContent)
                    .isEqualTo(ExceptionUtils.getStackTrace(exception));
        }

        @Test
        void testCaption() {
            testGetCaption();
        }
    }

    void testGetCaption() {
        var customCaption = "title";

        var config = ErrorDialogConfiguration.createWithHandlerNavigatingTo(ROUTE_DEFAULT_TEST_VIEW)
                .withCaption(customCaption);
        var dialogCaption = config.getCaption();

        assertThat(dialogCaption).isEqualTo(customCaption);
    }

    @Route(value = ROUTE_DEFAULT_TEST_VIEW)
    public static class DefaultTestView extends Div {
        @Serial
        private static final long serialVersionUID = 1L;
    }

    @Route(value = ROUTE_CUSTOM_TEST_VIEW)
    public static class CustomTestView extends Div {
        @Serial
        private static final long serialVersionUID = 1L;
    }
}
