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

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.validation.ValidationDisplayState;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.framework.ui.dialogs.OkCancelDialog.ButtonOption;
import org.linkki.util.handler.Handler;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

@ExtendWith(KaribuUIExtension.class)
class OkCancelDialogTest {

    @Test
    void testBuilder_DefaultOk() {
        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();

        assertThat(dialog.getCaption(), is("caption"));
        assertThat(KaribuUtils.Dialogs.getContents(dialog), hasSize(0));
        assertThat(_find(dialog.getContent(), Button.class), hasSize(2));

        // should not throw exception
        dialog.ok();
    }

    @Test
    void testBuilder_DefaultCancel() {
        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();

        assertThat(dialog.getCaption(), is("caption"));
        assertThat(KaribuUtils.Dialogs.getContents(dialog), hasSize(0));
        assertThat(_find(dialog.getContent(), Button.class), hasSize(2));

        // should not throw exception
        dialog.cancel();
    }

    @Test
    void testBuilder_OkCaption() {
        OkCancelDialog dialog = OkCancelDialog.builder("caption").okCaption("confirm it").build();

        assertThat(dialog.getOkCaption(), is("confirm it"));
        assertThat(KaribuUtils.Dialogs.getOkButton(dialog).getText(), is("confirm it"));
    }

    @Test
    void testBuilder_CancelCaption() {
        OkCancelDialog dialog = OkCancelDialog.builder("").cancelCaption("cancel it").build();

        assertThat(dialog.getCancelCaption(), is("cancel it"));
        assertThat(KaribuUtils.Dialogs.getCancelButton(dialog).getText(), is("cancel it"));
    }

    @Test
    void testBuilder_CancelCaption_NoCancelButton() {
        // it shouldn't make a difference whether cancelCaption is set before or after buttonOption
        OkCancelDialog dialog = OkCancelDialog.builder("")
                .buttonOption(ButtonOption.OK_ONLY)
                .cancelCaption("cancel it")
                .build();
        OkCancelDialog dialog2 = OkCancelDialog.builder("")
                .cancelCaption("cancel it")
                .buttonOption(ButtonOption.OK_ONLY)
                .build();

        assertThrows(IllegalStateException.class, dialog::getCancelCaption);
        assertThrows(IllegalStateException.class, dialog2::getCancelCaption);
    }

    @Test
    void testSetContent() {
        OkCancelDialog dialog = OkCancelDialog.builder("").build();
        assertThat(KaribuUtils.Dialogs.getContents(dialog).size(), is(0));

        dialog.addContent(new Div());

        assertThat(KaribuUtils.Dialogs.getContents(dialog), hasSize(1));
        assertThat(dialog, is(showingEnabledOkButton()));
    }

    @Test
    void testOk() {
        Handler okHandler = mock(Handler.class);
        Handler cancelHandler = mock(Handler.class);

        OkCancelDialog dialog = OkCancelDialog.builder("")
                .okHandler(okHandler)
                .cancelHandler(cancelHandler)
                .build();
        dialog.open();
        assertThat(dialog.getContent().isOpened(), is(true));

        KaribuUtils.Dialogs.clickOkButton();

        verify(okHandler).apply();
        verify(cancelHandler, never()).apply();
        assertThat(dialog.getContent().isOpened(), is(false));
        assertThat(dialog.isOkPressed(), is(true));
    }

    @Test
    void testCancel_ButtonClick() {
        Handler okHandler = mock(Handler.class);
        Handler cancelHandler = mock(Handler.class);

        OkCancelDialog dialog = OkCancelDialog.builder("")
                .okHandler(okHandler)
                .cancelHandler(cancelHandler)
                .build();
        dialog.open();
        assertThat(dialog.getContent().isOpened(), is(true));

        KaribuUtils.Dialogs.clickCancelButton();

        verify(okHandler, never()).apply();
        verify(cancelHandler).apply();
        assertThat(dialog.getContent().isOpened(), is(false));
        assertThat(dialog.isCancelPressed(), is(true));
    }

    @Test
    void testCancel_Close() {
        Handler okHandler = mock(Handler.class);
        Handler cancelHandler = mock(Handler.class);

        OkCancelDialog dialog = OkCancelDialog.builder("")
                .okHandler(okHandler)
                .cancelHandler(cancelHandler)
                .build();
        dialog.open();
        assertThat(dialog.getContent().isOpened(), is(true));

        dialog.close();
        assertThat(dialog.getContent().isOpened(), is(false));

        verify(cancelHandler).apply();
        verify(okHandler, never()).apply();
    }

    @Test
    void testSetOkCaption() {
        OkCancelDialog dialog = OkCancelDialog.builder("").build();

        dialog.setOkCaption("confirm it");

        assertThat(dialog.getOkCaption(), is("confirm it"));
        assertThat(KaribuUtils.Dialogs.getOkButton(dialog).getText(), is("confirm it"));
    }

    @Test
    void testSetCancelCaption() {
        OkCancelDialog dialog = OkCancelDialog.builder("").build();

        dialog.setCancelCaption("cancel it");

        assertThat(dialog.getCancelCaption(), is("cancel it"));
        assertThat(KaribuUtils.Dialogs.getCancelButton(dialog).getText(), is("cancel it"));
    }

    @Test
    void testSetCancelCaption_NoCancelButton() {
        OkCancelDialog dialog = OkCancelDialog.builder("").buttonOption(ButtonOption.OK_ONLY).build();

        assertThrows(IllegalStateException.class, () -> dialog.setCancelCaption("cancel it"));
    }

    @Test
    void testContent() {
        Span content1 = new Span("1");
        Span content2 = new Span("2");

        OkCancelDialog dialog = OkCancelDialog.builder("caption")
                .content(content1, content2)
                .build();

        assertThat(KaribuUtils.Dialogs.getContents(dialog), contains(content1, content2));
    }

    @Test
    void testButtonOption() {
        OkCancelDialog dialog = OkCancelDialog.builder("").buttonOption(ButtonOption.OK_ONLY).build();
        assertThat(_find(dialog.getContent(), Button.class), hasSize(1));
    }

    @Test
    void testValidate_ShowsNoInitialMessages() {
        MessageList messages = new MessageList();
        ValidationService validationService = ValidationService.of(messages);

        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();
        dialog.setValidationService(validationService);

        assertThat(dialog, is(not(displayingMessage())));
        assertThat(dialog, is(showingEnabledOkButton()));
    }

    @Test
    void testValidate_ShowsErrorsAndWarning() {
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
        assertThat(dialog.getMessageArea().isVisible(), is(false));
        assertThat(dialog, is(showingEnabledOkButton()));

        // MessageList with warning: warning is displayed, button is enabled
        messages.add(Message.newWarning("warning", "warning"));
        dialog.validate();
        assertThat(dialog, is(displayingMessage("warning")));
        assertThat(dialog, is(showingEnabledOkButton()));
    }

    @Test
    void testValidate_FiltersMessages() {
        ValidationMarker mandatoryFieldMarker = () -> true;
        Message message = Message.builder("error", Severity.ERROR).markers(mandatoryFieldMarker).create();
        MessageList messages = new MessageList(message);
        ValidationService validationService = ValidationService.of(messages);

        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();
        dialog.setValidationService(validationService);
        dialog.open();

        // mandatory field validations are hidden initially
        assertThat(dialog.getValidationDisplayState(), is(ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS));
        MessageList dialogMessage = dialog.validate();
        assertThat(dialogMessage.isEmpty(), is(true));
        assertThat(dialog, is(not(displayingMessage())));
        assertThat(dialog, is(showingEnabledOkButton()));

        // mandatory field validations are shown after the first click on the OK button
        KaribuUtils.Dialogs.getOkButton(dialog).click();
        assertThat(dialog.getValidationDisplayState(), is(ValidationDisplayState.SHOW_ALL));
        dialogMessage = dialog.validate();
        assertThat(dialogMessage, contains(message));
        assertThat(dialog, is(displayingMessage("error")));
        assertThat(dialog, is(showingDisabledOkButton()));

    }

    @Test
    void testSetClassName() {
        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();

        dialog.setClassName("classy");
        var classes = dialog.getContent().getChildren().findFirst().get().getElement().getAttribute("class");

        assertThat(classes, is("classy"));
    }

    @Test
    void testGetClassName() {
        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();

        dialog.getContent().getChildren().findFirst().get().getElement().setAttribute("class", "classy");

        assertThat(dialog.getClassName(), is("classy"));
    }

    @Test
    void testGetClassNames() {
        OkCancelDialog dialog = OkCancelDialog.builder("caption").build();

        dialog.getContent().getChildren().findFirst().get().getElement().setAttribute("class", "classy");
        var classList = dialog.getClassNames();

        assertThat(classList.contains("classy"), is(true));
    }

    private Matcher<OkCancelDialog> showingDisabledOkButton() {
        return not(showingEnabledOkButton());
    }

    private Matcher<OkCancelDialog> showingEnabledOkButton() {
        return new TypeSafeMatcher<>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("an OkCancelDialog whose OK button is enabled");
            }

            @Override
            protected boolean matchesSafely(OkCancelDialog dialog) {
                return KaribuUtils.Dialogs.getOkButton(dialog).isEnabled();
            }
        };
    }

    private Matcher<OkCancelDialog> displayingMessage() {
        return new TypeSafeMatcher<>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("an OkCancelDialog displaying a message");
            }

            @Override
            protected boolean matchesSafely(OkCancelDialog dialog) {
                return dialog.getMessageArea().getComponentCount() > 0;
            }
        };
    }

    private Matcher<OkCancelDialog> displayingMessage(String text) {
        return new TypeSafeMatcher<>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("an OkCancelDialog displaying a message with the text '");
                description.appendText(text);
                description.appendText("'");
            }

            @Override
            protected boolean matchesSafely(OkCancelDialog dialog) {
                Div layout = dialog.getMessageArea();
                LinkkiText message = (LinkkiText)layout.getComponentAt(0);
                return text.contentEquals(message.getText());
            }
        };
    }

}
