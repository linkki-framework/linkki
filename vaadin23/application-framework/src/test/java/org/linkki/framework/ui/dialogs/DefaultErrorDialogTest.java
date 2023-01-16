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

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.linkki.util.handler.Handler;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.ErrorEvent;

class DefaultErrorDialogTest {
    private static final String EXCEPTION_MESSAGE = "message";

    @BeforeAll
    static void setupLanguage() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterEach
    void vaadinTearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testErrorDialogContent_notProductionMode() {
        setUpVaadin(false);
        RuntimeException exception = new RuntimeException(EXCEPTION_MESSAGE);

        new DefaultErrorDialog(new ErrorEvent(exception), Handler.NOP_HANDLER).open();

        List<Component> children = getErrorDialogContentComponents();
        assertThat(children, hasSize(3));
        assertComponentShowsTimestamp(children.get(0));
        assertComponentShowsExceptionMessage(children.get(1));
        assertComponentShowsStacktrace(children.get(2), exception);
    }

    @Test
    void testErrorDialogContent_productionMode() {
        setUpVaadin(true);
        RuntimeException exception = new RuntimeException(EXCEPTION_MESSAGE);

        new DefaultErrorDialog(new ErrorEvent(exception), Handler.NOP_HANDLER).open();

        List<Component> children = getErrorDialogContentComponents();
        assertThat(children, hasSize(2));
        assertComponentShowsTimestamp(children.get(0));
        assertComponentShowsExceptionMessage(children.get(1));
        // implies that there is no further field which contains the stacktrace
    }

    private void setUpVaadin(boolean productionMode) {
        System.setProperty("vaadin.productionMode", String.valueOf(productionMode));
        MockVaadin.setup(new Routes(Stream.of(TestView.class)
                .collect(Collectors.toSet()),
                Collections.emptySet(), true));
    }

    private List<Component> getErrorDialogContentComponents() {
        List<Component> contentComponents = DialogTestUtil.getContents(_get(DefaultErrorDialog.class));
        return contentComponents.get(0).getChildren().collect(Collectors.toList());
    }

    private void assertComponentShowsTimestamp(Component component) {
        assertThat(component, instanceOf(HasText.class));
        assertThat(((HasText)component).getText(), startsWith("An error occurred. Timestamp "));
    }

    private void assertComponentShowsExceptionMessage(Component component) {
        assertThat(component, instanceOf(HasValue.class));
        assertThat(((HasValue<?, ?>)component).getValue(), is(EXCEPTION_MESSAGE));
    }

    private void assertComponentShowsStacktrace(Component component, Exception exception) {
        assertThat(component, instanceOf(TextArea.class));
        assertThat(((TextArea)component).getValue(),
                is(ExceptionUtils.getStackTrace(exception)));
    }

    @Route(value = "")
    public static class TestView extends Div {
        private static final long serialVersionUID = 1L;
    }
}