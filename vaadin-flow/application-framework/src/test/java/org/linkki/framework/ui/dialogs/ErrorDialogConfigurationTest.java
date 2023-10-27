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

package org.linkki.framework.ui.dialogs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.ErrorEvent;

class ErrorDialogConfigurationTest {

    private static final String ROUTE_DEFAULT_TEST_VIEW = StringUtils.EMPTY;
    private static final String ROUTE_CUSTOM_TEST_VIEW = "route";

    private static final String DEFAULT_ERROR_MESSAGE = "An unexpected error occurred.";
    private static final String EXCEPTION_MESSAGE = "message";
    private static final String EXPECTED_EXCEPTION_MESSAGE = "RuntimeException: " + EXCEPTION_MESSAGE;

    @BeforeAll
    static void setupLanguage() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterEach
    void vaadinTearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testGetDialogContent_Default_DevMode() {
        setupVaadin(false, DefaultTestView.class);
        var exception = new RuntimeException(EXCEPTION_MESSAGE);

        var content = ErrorDialogConfiguration.createWithHandlerNavigatingTo(ROUTE_DEFAULT_TEST_VIEW)
                .getDialogContent(new ErrorEvent(exception));

        assertThat(content).hasSize(4);
        assertTimestamp(content.get(0));
        assertErrorMessage(content.get(1), DEFAULT_ERROR_MESSAGE);
        assertExceptionMessage(content.get(2), EXPECTED_EXCEPTION_MESSAGE);
        assertExceptionStacktrace(content.get(3), ExceptionUtils.getStackTrace(exception));
    }

    @Test
    void testGetDialogContent_Default_ProductionMode() {
        setupVaadin(true, DefaultTestView.class);
        var exception = new RuntimeException(EXCEPTION_MESSAGE);

        var content = ErrorDialogConfiguration.createWithHandlerNavigatingTo(ROUTE_DEFAULT_TEST_VIEW)
                .getDialogContent(new ErrorEvent(exception));

        assertThat(content).hasSize(2);
        assertTimestamp(content.get(0));
        assertErrorMessage(content.get(1), DEFAULT_ERROR_MESSAGE);
        // implies there are no exception message and stacktrace fields
    }

    @Test
    void testGetDialogContent_AllCustomized() {
        setupVaadin(true, CustomTestView.class);
        var exception = new RuntimeException(EXCEPTION_MESSAGE);
        var customCaption = "caption";
        var customErrorMessage = "message";

        // use a config which is the opposite of the default
        var config = ErrorDialogConfiguration.createWithHandlerNavigatingTo(ROUTE_CUSTOM_TEST_VIEW)
                .withCaption(customCaption)
                .withErrorMessage(customErrorMessage)
                .showExceptionMessage()
                .showExceptionStacktrace();
        var content = config.getDialogContent(new ErrorEvent(exception));

        assertThat(content).hasSize(4);
        assertTimestamp(content.get(0));
        assertErrorMessage(content.get(1), customErrorMessage);
        assertExceptionMessage(content.get(2), EXPECTED_EXCEPTION_MESSAGE);
        assertExceptionStacktrace(content.get(3), ExceptionUtils.getStackTrace(exception));
    }

    @Test
    void testGetCaption() {
        setupVaadin(false, DefaultTestView.class);
        var customCaption = "title";

        var config = ErrorDialogConfiguration.createWithHandlerNavigatingTo(ROUTE_DEFAULT_TEST_VIEW)
                .withCaption(customCaption);
        var dialogCaption = config.getCaption();

        assertThat(dialogCaption).isEqualTo(customCaption);
    }

    /**
     * Mocks Vaadin for the current test instance with the given production mode and the given test
     * view.
     *
     * @param productionMode {@code true} whether production mode should be enabled
     * @param testView the test view to be used
     */
    private void setupVaadin(boolean productionMode, Class<? extends Div> testView) {
        System.setProperty("vaadin.productionMode", String.valueOf(productionMode));
        MockVaadin.setup(new Routes(Stream.of(testView).collect(Collectors.toSet()),
                Collections.emptySet(), true));
    }

    private void assertTimestamp(Component timestamp) {
        assertThat(timestamp).isInstanceOf(HasText.class);
        assertThat(((HasText)timestamp).getText()).startsWith("Timestamp ");
    }

    private void assertErrorMessage(Component errorMessage, String expectedMessage) {
        assertThat(errorMessage).isInstanceOf(HasText.class);
        assertThat(((HasText)errorMessage).getText()).isEqualTo(expectedMessage);
    }

    private void assertExceptionMessage(Component exceptionMessage, String expectedMessage) {
        assertThat(exceptionMessage).isInstanceOf(HasValue.class);
        assertThat(((HasValue<?, ?>)exceptionMessage).getValue()).isEqualTo(expectedMessage);
    }

    private void assertExceptionStacktrace(Component exceptionStacktrace, String expectedStacktrace) {
        assertThat(exceptionStacktrace).isInstanceOf(HasValue.class);
        assertThat(((HasValue<?, ?>)exceptionStacktrace).getValue()).isEqualTo(expectedStacktrace);
    }

    @Route(value = ROUTE_DEFAULT_TEST_VIEW)
    public static class DefaultTestView extends Div {
        private static final long serialVersionUID = 1L;
    }

    @Route(value = ROUTE_CUSTOM_TEST_VIEW)
    public static class CustomTestView extends Div {
        private static final long serialVersionUID = 1L;
    }
}
