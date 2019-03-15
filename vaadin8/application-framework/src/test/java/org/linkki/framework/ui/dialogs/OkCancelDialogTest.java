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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.validation.ValidationDisplayState;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.core.message.Severity;
import org.linkki.framework.ui.application.LinkkiUi;
import org.linkki.framework.ui.application.TestApplicationConfig;
import org.linkki.framework.ui.dialogs.OkCancelDialog.ButtonOption;
import org.linkki.util.StreamUtil;
import org.linkki.util.handler.Handler;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class OkCancelDialogTest {

    @SuppressWarnings("null")
    private LinkkiUi linkkiUi;

    @Before
    public void initCurrentUi() {
        linkkiUi = new LinkkiUi(new TestApplicationConfig());
        UI.setCurrent(linkkiUi);
    }

    @After
    public void cleanUpCurrentUi() {
        UI.setCurrent(null);
    }

    @Test
    public void testBuilder_DefaultOk() {
        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();

        assertThat(dialog.getCaption(), is("caption"));
        assertThat(DialogTestUtil.getContents(dialog), hasSize(0));
        assertThat(DialogTestUtil.getButtons(dialog), hasSize(2));

        // should not throw exception
        dialog.ok();
    }

    @Test
    public void testBuilder_DefaultCancel() {
        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();

        assertThat(dialog.getCaption(), is("caption"));
        assertThat(DialogTestUtil.getContents(dialog), hasSize(0));
        assertThat(DialogTestUtil.getButtons(dialog), hasSize(2));

        // should not throw exception
        dialog.cancel();
    }

    @Test
    public void testSetContent() {
        OkCancelDialog dialog = OkCancelDialog.builder("").build();
        assertThat(DialogTestUtil.getContents(dialog).size(), is(0));

        dialog.setContent(new Label());

        assertThat(DialogTestUtil.getContents(dialog), hasSize(1));
        assertThat(dialog, is(showingEnabledOkButton()));
    }

    @Test
    public void testOk() {
        Handler okHandler = mock(Handler.class);
        Handler cancelHandler = mock(Handler.class);

        OkCancelDialog dialog = OkCancelDialog.builder("")
                .okHandler(okHandler)
                .cancelHandler(cancelHandler)
                .build();
        dialog.open();
        assertThat(UI.getCurrent().getWindows(), hasSize(1));

        DialogTestUtil.clickOkButton(dialog);

        verify(okHandler).apply();
        verify(cancelHandler, never()).apply();

        assertThat(UI.getCurrent().getWindows(), hasSize(0));
    }

    @Test
    public void testCancel_ButtonClick() {
        Handler okHandler = mock(Handler.class);
        Handler cancelHandler = mock(Handler.class);

        OkCancelDialog dialog = OkCancelDialog.builder("")
                .okHandler(okHandler)
                .cancelHandler(cancelHandler)
                .build();
        dialog.open();
        assertThat(UI.getCurrent().getWindows(), hasSize(1));

        DialogTestUtil.clickCancelButton(dialog);

        verify(okHandler, never()).apply();
        verify(cancelHandler).apply();
        assertThat(UI.getCurrent().getWindows(), hasSize(0));
    }

    @Test
    public void testCancel_Close() {
        Handler okHandler = mock(Handler.class);
        Handler cancelHandler = mock(Handler.class);

        OkCancelDialog dialog = OkCancelDialog.builder("")
                .okHandler(okHandler)
                .cancelHandler(cancelHandler)
                .build();
        dialog.open();
        assertThat(UI.getCurrent().getWindows(), hasSize(1));

        dialog.close();
        assertThat(UI.getCurrent().getWindows(), hasSize(0));

        verify(cancelHandler).apply();
        verify(okHandler, never()).apply();
    }

    @Test
    public void testContent() {
        Label content1 = new Label("1");
        Label content2 = new Label("2");

        OkCancelDialog dialog = OkCancelDialog.builder("caption")
                .content(content1, content2)
                .build();

        assertThat(DialogTestUtil.getContents(dialog), contains(content1, content2));
    }

    @Test
    public void testButtonOption() {
        OkCancelDialog dialog = OkCancelDialog.builder("").buttonOption(ButtonOption.OK_ONLY).build();
        assertThat(DialogTestUtil.getButtons(dialog), hasSize(1));
    }


    @Test
    public void testValidate_ShowsNoInitialMessages() {
        MessageList messages = new MessageList();
        ValidationService validationService = ValidationService.of(messages);

        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();
        dialog.setValidationService(validationService);

        assertThat(dialog, is(not(displayingMessage())));
        assertThat(dialog, is(showingEnabledOkButton()));
    }

    @Test
    public void testValidate_ShowsErrorsAndWarning() {
        MessageList messages = new MessageList();
        ValidationService validationService = ValidationService.of(messages);

        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();
        dialog.setValidationService(validationService);

        // MessageList with error and warning: error is displayed, button is disabled
        messages.add(Message.newWarning("warning", "warning"));
        messages.add(Message.newError("error", "error"));
        dialog.validate();
        assertThat(dialog, is(displayingMessage("error")));
        assertThat(dialog, is(showingDisabledOkButton()));

        // MessageList without entries: nothing is displayed, button is enabled
        messages.clear();
        dialog.validate();
        assertThat(dialog, is(not(displayingMessage())));
        assertThat(dialog, is(showingEnabledOkButton()));

        // MessageList with warning: warning is displayed, button is enabled
        messages.add(Message.newWarning("warning", "warning"));
        dialog.validate();
        assertThat(dialog, is(displayingMessage("warning")));
        assertThat(dialog, is(showingEnabledOkButton()));
    }

    @Test
    public void testValidate_FiltersMessages() {
        ValidationMarker mandatoryFieldMarker = () -> true;
        Message message = Message.builder("error", Severity.ERROR).markers(mandatoryFieldMarker).create();
        MessageList messages = new MessageList(message);
        ValidationService validationService = ValidationService.of(messages);

        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();
        dialog.setValidationService(validationService);

        // mandatory field validations are hidden initially
        assertThat(dialog.getValidationDisplayState(), is(ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS));
        MessageList dialogMessage = dialog.validate();
        assertTrue(dialogMessage.isEmpty());
        assertThat(dialog, is(not(displayingMessage())));
        assertThat(dialog, is(showingEnabledOkButton()));

        // mandatory field validations are shown after the first click on the OK button
        DialogTestUtil.getOkButton(dialog).click();
        assertThat(dialog.getValidationDisplayState(), is(ValidationDisplayState.SHOW_ALL));
        dialogMessage = dialog.validate();
        assertThat(dialogMessage, contains(message));
        assertThat(dialog, is(displayingMessage("error")));
        assertThat(dialog, is(showingDisabledOkButton()));

    }

    private Matcher<OkCancelDialog> showingDisabledOkButton() {
        return not(showingEnabledOkButton());
    }

    private Matcher<OkCancelDialog> showingEnabledOkButton() {
        return new TypeSafeMatcher<OkCancelDialog>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("an OkCancelDialog whose OK button is enabled");
            }

            @Override
            protected boolean matchesSafely(OkCancelDialog dialog) {
                return DialogTestUtil.getOkButton(dialog).isEnabled();
            }
        };
    }

    private Matcher<OkCancelDialog> displayingMessage() {
        return new TypeSafeMatcher<OkCancelDialog>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("an OkCancelDialog displaying a message");
            }

            @SuppressWarnings("null")
            @Override
            protected boolean matchesSafely(OkCancelDialog dialog) {
                @NonNull
                VerticalLayout layout = (VerticalLayout)dialog.getContent();
                VerticalLayout nestedLayout = (VerticalLayout)layout.getComponent(0);
                return components(nestedLayout).anyMatch(Label.class::isInstance);
            }
        };
    }

    private Matcher<OkCancelDialog> displayingMessage(String text) {
        return new TypeSafeMatcher<OkCancelDialog>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("an OkCancelDialog displaying a message with the text '");
                description.appendText(text);
                description.appendText("'");
            }

            @Override
            protected boolean matchesSafely(OkCancelDialog dialog) {
                @SuppressWarnings("null")
                @NonNull
                VerticalLayout layout = (VerticalLayout)dialog.getContent();
                VerticalLayout nestedLayout = (VerticalLayout)layout.getComponent(0);
                return components(nestedLayout).filter(Label.class::isInstance)
                        .findFirst()
                        .map(c -> ((Label)c).getCaption().equals(text))
                        .orElse(false);
            }
        };
    }

    static Stream<Component> components(VerticalLayout layout) {
        return StreamUtil.stream(layout);
    }

}
