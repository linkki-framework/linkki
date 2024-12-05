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

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.entry;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.util.handler.Handler;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.mock.MockVaadinSession;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.ErrorEvent;

class DialogErrorHandlerTest {

    private static final String DEFAULT_VIEW_ROUTE = "";
    private static final String TEST_VIEW_ROUTE = "route";

    @BeforeAll
    static void setupLanguage() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterEach
    void vaadinTearDown() {
        MockVaadin.tearDown();
    }

    @BeforeEach
    void setupVaadin() {
        MockVaadin.setup(new Routes(Stream.of(DefaultView.class, TestView.class)
                .collect(Collectors.toSet()),
                Collections.emptySet(), true));
    }

    @Test
    void testError() {
        var dialogConfig = ErrorDialogConfiguration.createWithHandlerNavigatingTo(TEST_VIEW_ROUTE)
                .withCaption("custom caption")
                .withErrorMessage("custom error message")
                .showExceptionMessage()
                .showExceptionStacktrace();
        var handler = new DialogErrorHandler(dialogConfig);
        MockVaadinSession.getCurrent().setErrorHandler(handler);
        var exception = new RuntimeException("Exception message", new RuntimeException("Cause"));
        var event = new ErrorEvent(exception);
        var testLoggingHandler = new TestLoggingHandler();
        var logger = Logger.getLogger(DialogErrorHandler.class.getName());
        logger.addHandler(testLoggingHandler);

        handler.error(event);

        _assertOne(OkCancelDialog.class);
        var errorDialog = _get(OkCancelDialog.class);
        assertThat(errorDialog.getCaption())
                .as("Check dialog caption is as configured").isEqualTo(dialogConfig.getCaption());
        var dialogContent = KaribuUtils.printComponentTree(errorDialog.getContentArea());
        assertThat(dialogContent).contains("custom error message")
                .contains("Timestamp")
                .contains("Cause");
        assertThat(dialogContent).as("Stacktrace should not be shown")
                .contains("Exception message");
        var uuidMatcher = Pattern.compile("Timestamp:.+\\[([^\\]]+)\\]", Pattern.MULTILINE).matcher(dialogContent);
        var hasMatch = uuidMatcher.find();
        assertThat(hasMatch).as("UUID is displayed after the timestamp").isTrue();
        var uuid = uuidMatcher.group(1);
        assertThatNoException().as("The string displayed after timestamp is a valid UUID")
                .isThrownBy((() -> UUID.fromString(uuid)));
        assertThat(testLoggingHandler.getMessage())
                .contains(uuid);

        errorDialog.ok();

        assertUrlAfterOkIsCorrect(TEST_VIEW_ROUTE);
        logger.removeHandler(testLoggingHandler);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testError_WithDialogCreator() {
        var caption = "caption";
        var exceptionMessage = "message";
        BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator = (e, h) -> new ConfirmationDialog(caption, h,
                new Text(e.getThrowable().getMessage()));
        var handler = new DialogErrorHandler(dialogCreator);

        handler.error(new ErrorEvent(new RuntimeException(exceptionMessage)));

        _assertOne(ConfirmationDialog.class);
        var errorDialog = _get(ConfirmationDialog.class);
        assertThat(errorDialog.getContentArea().getChildren())
                .map(KaribuUtils::getTextContent)
                .containsExactly(exceptionMessage);

        errorDialog.ok();

        assertUrlAfterOkIsCorrect(DEFAULT_VIEW_ROUTE);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testError_WithDialogCreatorAndStartView() {
        var caption = "caption";
        var exceptionMessage = "message";
        BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator = (e, h) -> new ConfirmationDialog(caption, h,
                new Text(e.getThrowable().getMessage()));
        var handler = new DialogErrorHandler(dialogCreator, TEST_VIEW_ROUTE);

        handler.error(new ErrorEvent(new RuntimeException(exceptionMessage)));

        _assertOne(ConfirmationDialog.class);
        var errorDialog = _get(ConfirmationDialog.class);
        assertThat(errorDialog.getContentArea().getChildren())
                .map(KaribuUtils::getTextContent)
                .containsExactly(exceptionMessage);

        errorDialog.ok();

        assertUrlAfterOkIsCorrect(TEST_VIEW_ROUTE);
    }

    private void assertUrlAfterOkIsCorrect(String expectedRouteAfterOk) {
        var locationAfterOk = UI.getCurrent().getInternals().getActiveViewLocation();
        assertThat(locationAfterOk.getSegments()).last()
                .as("Check route after click")
                .isEqualTo(expectedRouteAfterOk);
        assertThat(locationAfterOk.getQueryParameters().getParameters())
                .as("Check add error parameter after click")
                .containsExactly(entry(DialogErrorHandler.ERROR_PARAM, List.of(StringUtils.EMPTY)));
    }

    @Route(value = DEFAULT_VIEW_ROUTE)
    public static class DefaultView extends Div {
        private static final long serialVersionUID = 1L;
    }

    @Route(value = TEST_VIEW_ROUTE)
    public static class TestView extends Div {
        private static final long serialVersionUID = 1L;
    }
}
