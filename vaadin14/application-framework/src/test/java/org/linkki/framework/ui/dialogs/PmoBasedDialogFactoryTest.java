/*******************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 *
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************/

package org.linkki.framework.ui.dialogs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.dialogs.OkCancelDialog.ButtonOption;
import org.linkki.util.handler.Handler;
import org.mockito.Mockito;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

public class PmoBasedDialogFactoryTest {

    @Test
    public void testNewOkCancelDialog_CreationOfDialog() {
        Handler okHandler = mock(Handler.class);
        Handler cancelHandler = mock(Handler.class);

        OkCancelDialog dialog = new PmoBasedDialogFactory()
                .newOkCancelDialog("test", okHandler, cancelHandler, ButtonOption.OK_CANCEL, new TestPmo());

        assertThat(dialog.getCaption(), is("test"));

        dialog.ok();
        verify(okHandler).apply();
        verify(cancelHandler, never()).apply();

        Mockito.reset(okHandler, cancelHandler);

        dialog.cancel();
        verify(cancelHandler).apply();
        verify(okHandler, never()).apply();

        assertThat(DialogTestUtil.getButtons(dialog), hasSize(2));
        assertThat(DialogTestUtil.getContents(dialog), hasSize(1));
    }

    @Test
    public void testNewOkCancelDialog_CreationOfDialog_NoPmo() {
        OkCancelDialog dialog = new PmoBasedDialogFactory()
                .newOkCancelDialog("test", Handler.NOP_HANDLER, Handler.NOP_HANDLER, ButtonOption.OK_CANCEL);
        assertThat(DialogTestUtil.getContents(dialog), hasSize(0));
    }

    @Test
    public void testNewOkCancelDialog_CreationOfDialog_MultiplePmo() {
        OkCancelDialog dialog = new PmoBasedDialogFactory()
                .newOkCancelDialog("test", Handler.NOP_HANDLER, Handler.NOP_HANDLER, ButtonOption.OK_CANCEL,
                                   new TestPmo(), new TestPmo());
        assertThat(DialogTestUtil.getContents(dialog), hasSize(2));
    }

    @Test
    public void testNewOkCancelDialog_ValidationService() {
        MessageList ml = new MessageList();
        ValidationService validationService = mock(ValidationService.class);
        when(validationService.getValidationMessages()).thenReturn(ml);

        PmoBasedDialogFactory pmoBasedDialogFactory = new PmoBasedDialogFactory(validationService,
                PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        OkCancelDialog dialog = pmoBasedDialogFactory.newOkCancelDialog("test", Handler.NOP_HANDLER,
                                                                        Handler.NOP_HANDLER,
                                                                        ButtonOption.OK_CANCEL);
        assertThat(dialog.getValidationService(), is(validationService));
    }

    @Test
    public void testNewOkCancelDialog_PropertyBehaviorProvider() {
        PmoBasedDialogFactory dialogFactoryWithoutBehavior = new PmoBasedDialogFactory(
                ValidationService.NOP_VALIDATION_SERVICE,
                PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        OkCancelDialog dialog = dialogFactoryWithoutBehavior.newOkCancelDialog("test", Handler.NOP_HANDLER,
                                                                               Handler.NOP_HANDLER,
                                                                               ButtonOption.OK_CANCEL,
                                                                               new TestPmo(), new TestPmo());
        getAllFields(dialog).forEach(c -> {
            assertFalse(c.isReadOnly());
        });

        PmoBasedDialogFactory dialogFactoryWithBehavior = new PmoBasedDialogFactory(
                ValidationService.NOP_VALIDATION_SERVICE,
                PropertyBehaviorProvider.with(PropertyBehavior.readOnly()));

        OkCancelDialog readOnlyDialog = dialogFactoryWithBehavior.newOkCancelDialog("test", Handler.NOP_HANDLER,
                                                                                    Handler.NOP_HANDLER,
                                                                                    ButtonOption.OK_CANCEL,
                                                                                    new TestPmo(), new TestPmo());
        getAllFields(readOnlyDialog).forEach(c -> {
            assertTrue(c.isReadOnly());
        });
    }

    @Test
    public void testNewOkDialog() {
        OkCancelDialog dialog = new PmoBasedDialogFactory().newOkDialog("title", new TestPmo());
        List<AbstractField<?, ?>> fields = getAllFields(dialog);
        assertThat(fields, hasSize(1));
        assertThat(fields.get(0), is(instanceOf(TextField.class)));
        assertThat(dialog.getCaption(), is("title"));
    }

    @Test
    public void testNewOkDialog_noPmo() {
        OkCancelDialog dialog = new PmoBasedDialogFactory().newOkDialog("title");
        assertThat(getAllFields(dialog), hasSize(0));
    }

    @Test
    public void testNewOkDialog_MultiplePmos() {
        OkCancelDialog dialog = new PmoBasedDialogFactory().newOkDialog("title", new TestPmo(), new TestPmo());
        List<AbstractField<?, ?>> fields = getAllFields(dialog);
        assertThat(fields, hasSize(2));
        fields.forEach(f -> assertThat(f, is(instanceOf(TextField.class))));
        assertThat(dialog.getCaption(), is("title"));
    }

    private static List<AbstractField<?, ?>> getAllFields(OkCancelDialog dialog) {
        return DialogTestUtil.getContents(dialog)
                .stream()
                .flatMap(c -> getAllFields(c).stream())
                .collect(Collectors.toList());
    }

    private static List<AbstractField<?, ?>> getAllFields(Component component) {
        ArrayList<AbstractField<?, ?>> inputs = new ArrayList<>();

        if (component instanceof AbstractField) {
            inputs.add((AbstractField<?, ?>)component);
        } else {
            component.getChildren().forEach(child -> inputs.addAll(getAllFields(child)));
        }

        return inputs;
    }

    @UISection
    public static class TestPmo {

        private String text = "text";

        @UITextField(position = 0, label = "text field")
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}