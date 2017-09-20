/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.linkki.core.binding.validation.ValidationDisplayState;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.framework.ui.component.MessageRow;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class OkCancelDialogTest {

    @Test
    public void testValidate_ShowsNoInitialMessages() {
        MessageList messages = new MessageList();
        ValidationService validationService = ValidationService.of(messages);

        OkCancelDialog dialog = new OkCancelDialog("caption");
        dialog.setValidationService(validationService);

        assertThat(dialog, is(not(displayingMessage())));
        assertThat(dialog, is(showingEnabledOkButton()));
        assertThat(dialog.isOkEnabled(), is(true));
    }

    @Test
    public void testValidate_ShowsErrorsAndWarning() {
        MessageList messages = new MessageList();
        ValidationService validationService = ValidationService.of(messages);

        OkCancelDialog dialog = new OkCancelDialog("caption");
        dialog.setValidationService(validationService);

        // MessageList with error and warning: error is displayed, button is disabled
        messages.add(Message.newWarning("warning", "warning"));
        messages.add(Message.newError("error", "error"));
        dialog.validate();
        assertThat(dialog, is(displayingMessage("error")));
        assertThat(dialog, is(showingDisabledOkButton()));
        assertThat(dialog.isOkEnabled(), is(false));

        // MessageList without entries: nothing is displayed, button is enabled
        messages.clear();
        dialog.validate();
        assertThat(dialog, is(not(displayingMessage())));
        assertThat(dialog, is(showingEnabledOkButton()));
        assertThat(dialog.isOkEnabled(), is(true));

        // MessageList with warning: warning is displayed, button is enabled
        messages.add(Message.newWarning("warning", "warning"));
        dialog.validate();
        assertThat(dialog, is(displayingMessage("warning")));
        assertThat(dialog, is(showingEnabledOkButton()));
        assertThat(dialog.isOkEnabled(), is(true));
    }

    @Test
    public void testValidate_FiltersMessages() {
        ValidationMarker mandatoryFieldMarker = () -> true;
        Message message = Message.builder("error", ErrorLevel.ERROR).markers(mandatoryFieldMarker).create();
        MessageList messages = new MessageList(message);
        ValidationService validationService = ValidationService.of(messages);

        OkCancelDialog dialog = new OkCancelDialog("caption");
        dialog.setValidationService(validationService);

        // mandatory field validations are hidden initially
        assertThat(dialog.getValidationDisplayState(), is(ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS));
        MessageList dialogMessage = dialog.validate();
        assertTrue(dialogMessage.isEmpty());
        assertThat(dialog, is(not(displayingMessage())));
        assertThat(dialog, is(showingEnabledOkButton()));

        // mandatory field validations are shown after the first click on the OK button
        getOkButton(dialog).click();
        assertThat(dialog.getValidationDisplayState(), is(ValidationDisplayState.SHOW_ALL));
        dialogMessage = dialog.validate();
        assertThat(dialogMessage, contains(message));
        assertThat(dialog, is(displayingMessage("error")));
        assertThat(dialog, is(showingDisabledOkButton()));

    }

    @Test
    public void testSetContent() {
        OkCancelDialog dialog = new OkCancelDialog("");
        dialog.setContent(new Label());

        assertThat(dialog, is(showingEnabledOkButton()));
    }

    private Matcher<OkCancelDialog> displayingMessage() {
        return new TypeSafeMatcher<OkCancelDialog>() {

            @SuppressWarnings("null")
            @Override
            public void describeTo(Description description) {
                description.appendText("an OkCancelDialog displaying a message");
            }

            @SuppressWarnings("null")
            @Override
            protected boolean matchesSafely(OkCancelDialog dialog) {
                VerticalLayout layout = (VerticalLayout)dialog.getContent();
                return components(layout).findFirst().map(MessageRow.class::isInstance).orElse(false);
            }
        };
    }

    private Matcher<OkCancelDialog> displayingMessage(String text) {
        return new TypeSafeMatcher<OkCancelDialog>() {

            @SuppressWarnings("null")
            @Override
            public void describeTo(Description description) {
                description.appendText("an OkCancelDialog displaying a message with the text '");
                description.appendText(text);
                description.appendText("'");
            }

            @SuppressWarnings("null")
            @Override
            protected boolean matchesSafely(OkCancelDialog dialog) {
                VerticalLayout layout = (VerticalLayout)dialog.getContent();
                VerticalLayout nestedLayout = (VerticalLayout)layout.getComponent(0);

                return components(nestedLayout).filter(MessageRow.class::isInstance)
                        .findFirst()
                        .map(c -> ((MessageRow)c).getText().equals(text))
                        .orElse(false);
            }
        };
    }

    private Matcher<OkCancelDialog> showingDisabledOkButton() {
        return not(showingEnabledOkButton());
    }

    private Matcher<OkCancelDialog> showingEnabledOkButton() {
        return new TypeSafeMatcher<OkCancelDialog>() {

            @SuppressWarnings("null")
            @Override
            public void describeTo(Description description) {
                description.appendText("an OkCancelDialog whose OK button is enabled");
            }

            @SuppressWarnings("null")
            @Override
            protected boolean matchesSafely(OkCancelDialog dialog) {
                return getOkButton(dialog).isEnabled();
            }
        };
    }

    Button getOkButton(OkCancelDialog dialog) {
        VerticalLayout layout = (VerticalLayout)dialog.getContent();
        return (Button)((HorizontalLayout)layout.getComponent(layout.getComponentCount() - 1)).getComponent(0);
    }

    static Stream<Component> components(VerticalLayout layout) {
        return StreamSupport.stream(layout.spliterator(), false);
    }

}
