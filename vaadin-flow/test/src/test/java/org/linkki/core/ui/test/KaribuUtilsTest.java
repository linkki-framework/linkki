/*******************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************/
package org.linkki.core.ui.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.io.Serial;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.shared.communication.PushMode;

@ExtendWith(KaribuUIExtension.class)
class KaribuUtilsTest {

    @Test
    void testPrintComponentTree() {
        TestLoggingHandler testLoggingHandler = new TestLoggingHandler();
        KaribuUtils.LOGGER.addHandler(testLoggingHandler);

        KaribuUtils.printComponentTree();

        assertThat(testLoggingHandler.getMessage()).contains("MockedUI");
        KaribuUtils.LOGGER.removeHandler(testLoggingHandler);
    }

    @Test
    void testGetWithId() {
        var layout1 = new HorizontalLayout();
        layout1.setId("hl-1");
        var layout2 = new HorizontalLayout();
        layout2.setId("hl-2");
        UI.getCurrent().add(layout1, layout2);

        assertThat(KaribuUtils.getWithId(HorizontalLayout.class, "hl-1")).isSameAs(layout1);
        assertThat(KaribuUtils.getWithId(HorizontalLayout.class, "hl-2")).isSameAs(layout2);
    }

    @Test
    void testGetWithId_NotFound() {
        var layout1 = new HorizontalLayout();
        layout1.setId("hl-1");
        var layout2 = new HorizontalLayout();
        layout2.setId("hl-2");
        UI.getCurrent().add(layout1, layout2);

        assertThrows(AssertionError.class, () -> KaribuUtils.getWithId(HorizontalLayout.class, "non-existent"));
    }

    @Test
    void testGetWithId_NotUnique() {
        var layout1 = new HorizontalLayout();
        layout1.setId("duplicate");
        var layout2 = new HorizontalLayout();
        layout2.setId("duplicate");
        UI.getCurrent().add(layout1, layout2);

        assertThrows(AssertionError.class, () -> KaribuUtils.getWithId(HorizontalLayout.class, "duplicate"));
    }

    @Test
    void testGetWithId_WithParent() {
        var layout1 = new HorizontalLayout();
        layout1.setId("layout-1");
        var label1 = new NativeLabel("Test Label within parent layout");
        label1.setId("label-1");
        layout1.add(label1);
        UI.getCurrent().add(layout1);

        assertThat(KaribuUtils.getWithId(layout1, NativeLabel.class, "label-1")).isSameAs(label1);
    }

    @Test
    void testGetWithId_WithParent_NotAChild() {
        var layout1 = new HorizontalLayout();
        layout1.setId("layout-1");
        var label1 = new NativeLabel("Test Label within parent layout");
        label1.setId("label-1");
        layout1.add(label1);

        var labelWithoutParent = new NativeLabel("Test Label without any parent");
        labelWithoutParent.setId("label-without-parent");

        UI.getCurrent().add(layout1, labelWithoutParent);

        assertThrows(AssertionError.class,
                     () -> KaribuUtils.getWithId(layout1, NativeLabel.class, "label-without-parent"));
    }

    @Test
    void testGetTextContent_EmptyLayout() {
        var component = new HorizontalLayout();
        assertThat(KaribuUtils.getTextContent(component)).isEqualTo("");
    }

    @Test
    void testGetTextContent_TextComponent() {
        var text = "Lorem ipsum";
        var component = new NativeLabel(text);
        assertThat(KaribuUtils.getTextContent(component)).isEqualTo(text);
    }

    @Test
    void testGetTextContent_ValueComponent() {
        var text = "Lorem ipsum";
        var component = new TextField();
        component.setValue(text);
        assertThat(KaribuUtils.getTextContent(component)).isEqualTo(text);
    }

    @Test
    void testGetTextContent_Span() {
        var text = "Lorem ipsum";
        var component = new Span(text);
        assertThat(KaribuUtils.getTextContent(component)).isEqualTo(text);
    }

    @Nested
    class UITests {

        @Test
        void testPush() {
            var ui = com.vaadin.flow.component.UI.getCurrent();
            ui.getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
            var command = mock(Command.class);
            ui.access(command);
            verifyNoInteractions(command);

            KaribuUtils.UI.push(ui);

            verify(command, times(1)).execute();
        }

        @Test
        void testPush_CurrentUI() {
            var ui = com.vaadin.flow.component.UI.getCurrent();
            ui.getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
            var command = mock(Command.class);
            ui.access(command);
            verifyNoInteractions(command);

            KaribuUtils.UI.push();

            verify(command, times(1)).execute();
        }

    }

    @Nested
    class NotificationsTests {

        @SuppressWarnings("deprecation")
        @Test
        void testGetNotification() {

            Notification notification = new Notification();
            notification.open();
            assertThat(KaribuUtils.getNotification()).isSameAs(notification);
        }

        @SuppressWarnings("deprecation")
        @Test
        void testGetNotification_NoNotifications() {
            assertThrows(AssertionError.class, KaribuUtils::getNotification);
        }

        @SuppressWarnings("deprecation")
        @Test
        void testGetNotification_MultipleNotifications() {
            new Notification().open();
            new Notification().open();
            assertThrows(AssertionError.class, KaribuUtils::getNotification);
        }

        @SuppressWarnings("deprecation")
        @Test
        void testGetNotificationTitle() {
            var content = new Div(new H3("notification title"));
            content.getClassNames().add("linkki-notification-content");
            var notification = new Notification(content);
            notification.open();

            assertThat(KaribuUtils.getNotificationTitle(notification)).isEqualTo("notification title");
        }

    }

    @Nested
    class FieldsTests {

        @Test
        void testSetValue() {
            var testValue = "test-value";
            var textfield = new TextField();
            KaribuUtils.Fields.setValue(textfield, testValue);

            assertThat(textfield.getValue()).isEqualTo(testValue);
        }

        @Test
        void testSetValueOnComponent() {
            var testValue = "test-value";
            var layout1 = new HorizontalLayout();
            var textfield1 = new TextField();
            textfield1.setId("textfield");
            var textfield2 = new TextField();
            var textfield3 = new TextField();
            layout1.add(textfield2, textfield1, textfield3);

            UI.getCurrent().add(layout1);

            KaribuUtils.Fields.setValue(TextField.class, "textfield", testValue);

            assertThat(textfield1.getValue()).isEqualTo(testValue);
            assertThat(textfield2.getValue()).isEmpty();
            assertThat(textfield3.getValue()).isEmpty();
        }

    }

    @Nested
    class ComboboxesTests {

        @Test
        void testGetComboBoxWithId() {
            var layout1 = new HorizontalLayout();
            var combobox1 = new ComboBox<>();
            combobox1.setId("combobox-1");
            layout1.add(combobox1);

            UI.getCurrent().add(layout1);

            assertThat(KaribuUtils.ComboBoxes.getComboBoxWithId("combobox-1")).isSameAs(combobox1);
        }

        @Test
        void testGetComboBoxWithId_NotInUI() {
            var layout1 = new HorizontalLayout();
            var combobox1 = new ComboBox<>();
            combobox1.setId("combobox-1");
            layout1.add(combobox1);

            assertThrows(AssertionError.class, () -> KaribuUtils.ComboBoxes.getComboBoxWithId("combobox-1"));
        }

        @Test
        void testGetComboBoxWithId_WithParent() {
            var layout1 = new HorizontalLayout();
            var combobox1 = new ComboBox<>();
            combobox1.setId("combobox-1");
            layout1.add(combobox1);

            UI.getCurrent().add(layout1);

            assertThat(KaribuUtils.ComboBoxes.getComboBoxWithId(layout1, "combobox-1")).isSameAs(combobox1);
        }

        @Test
        void testGetComboBoxWithId_WithParent_WrongParent() {
            var layout1 = new HorizontalLayout();
            var layout2 = new HorizontalLayout();
            var combobox1 = new ComboBox<>();
            combobox1.setId("combobox-1");
            layout1.add(combobox1);

            UI.getCurrent().add(layout1, layout2);

            assertThrows(AssertionError.class, () -> KaribuUtils.ComboBoxes.getComboBoxWithId(layout2, "combobox-1"));
        }

        @ParameterizedTest
        @MethodSource("provideValues")
        void testSetComboboxValue(String value) {
            var id = "combobox-1";

            var combobox = new ComboBox<String>();
            combobox.setId(id);
            combobox.setItems(provideValues());

            UI.getCurrent().add(combobox);

            KaribuUtils.ComboBoxes.setComboBoxValue(id, value);

            assertThat(combobox.getValue()).isEqualTo(value);
        }

        private static List<String> provideValues() {
            return List.of("Value 1", "Value 2", "Value 3");
        }

    }

    @Nested
    class DialogsTests {

        private final org.linkki.util.handler.Handler okHandler = spy(org.linkki.util.handler.Handler.class);
        private final org.linkki.util.handler.Handler cancelHandler = spy(org.linkki.util.handler.Handler.class);
        private final NativeLabel label = new NativeLabel();
        private final TestDialogLayout dialogLayout = new TestDialogLayout(Stream.of(label), okHandler, cancelHandler);

        @Test
        void testGetOkButton() {
            assertThat(KaribuUtils.Dialogs.getOkButton(dialogLayout)).isSameAs(dialogLayout.getOkButton());
        }

        @Test
        void testCickOkButton() {
            UI.getCurrent().add(dialogLayout.getContent());
            dialogLayout.getContent().open();

            KaribuUtils.Dialogs.clickOkButton();

            verify(okHandler).apply();
            verifyNoInteractions(cancelHandler);
        }

        @Test
        void testGetCancelButton() {
            assertThat(KaribuUtils.Dialogs.getCancelButton(dialogLayout)).isSameAs(dialogLayout.getCancelButton());
        }

        @Test
        void testCickCancelButton() {
            UI.getCurrent().add(dialogLayout.getContent());
            dialogLayout.getContent().open();

            KaribuUtils.Dialogs.clickCancelButton();

            verify(cancelHandler).apply();
            verifyNoInteractions(okHandler);
        }

        @Test
        void testGetContents() {
            assertThat(KaribuUtils.Dialogs.getContents(dialogLayout)).contains(label);
        }

        static class TestDialogLayout extends Composite<Dialog> {

            @Serial
            private static final long serialVersionUID = -5126222507978828977L;

            private final Button okButton;
            private final Button cancelButton;

            TestDialogLayout(Stream<Component> content, org.linkki.util.handler.Handler okHandler,
                    org.linkki.util.handler.Handler cancelHandler) {
                okButton = new Button();
                okButton.addClickListener(event -> okHandler.apply());
                okButton.setId("okButton");

                cancelButton = new Button();
                cancelButton.addClickListener(event -> cancelHandler.apply());
                cancelButton.setId("cancelButton");

                var contentLayout = new VerticalLayout();
                contentLayout.addClassName("linkki-dialog-content-area");

                content.forEach(contentLayout::add);

                getContent().add(okButton, cancelButton, contentLayout);
            }

            public Button getOkButton() {
                return okButton;
            }

            public Button getCancelButton() {
                return cancelButton;
            }

        }

    }

    @Nested
    class GridsTests {

        @Test
        void testGet() {
            var layout1 = new HorizontalLayout();
            var grid = new Grid<String>();
            layout1.add(new NativeLabel("Test label"), grid, new NativeLabel("Another test label"));

            assertThat(KaribuUtils.Grids.get(layout1)).isSameAs(grid);
        }

        @Test
        void testGetWithPmo() {
            var layout1 = new HorizontalLayout();
            var grid = new Grid<String>();
            grid.setId(TestPmo.class.getSimpleName() + "_table");
            layout1.add(new NativeLabel("Test label"), grid, new NativeLabel("Another test label"));

            UI.getCurrent().add(layout1);

            assertThat(KaribuUtils.Grids.getWithPmo(TestPmo.class)).isSameAs(grid);
        }

        @Test
        void testGetTextContentsInColumn() {
            Grid<TestPersonEntry> grid = new Grid<>();
            grid.setItems(List.of(new TestPersonEntry("Max", "Mustermann"), new TestPersonEntry("Eva", "Musterfrau")));
            grid.addColumn(new ComponentRenderer<>(person -> new NativeLabel(person.firstname))).setKey("firstname");
            grid.addColumn(new ComponentRenderer<>(person -> new HorizontalLayout(new NativeLabel(person.lastname))))
                    .setKey("lastname");

            assertThat(KaribuUtils.Grids.getTextContentsInColumn(grid, "firstname")).contains("Max", "Eva");
            assertThat(KaribuUtils.Grids.getTextContentsInColumn(grid, "lastname")).contains("Mustermann",
                                                                                             "Musterfrau");
        }

        @Test
        void testGetTextContentsInColumn_EmptyTable() {
            assertThat(KaribuUtils.Grids.getTextContentsInColumn(new Grid<String>(), "colum-1")).isEmpty();
        }

        static final class TestPmo {
            // empty pmo class
        }

        record TestPersonEntry(String firstname, String lastname) {
        }

    }

    @Nested
    class LayoutsTests {

        @Test
        void testLayoutsGetWithPmo() {
            var layout1 = new HorizontalLayout();
            layout1.setId("layout-1");
            var layoutPmoBased = new HorizontalLayout();
            layoutPmoBased.setId(TestPmo.class.getSimpleName());

            UI.getCurrent().add(layout1, layoutPmoBased);

            assertThat(KaribuUtils.Layouts.getWithPmo(HorizontalLayout.class, TestPmo.class)).isSameAs(layoutPmoBased);
        }

        static final class TestPmo {
            // empty pmo class
        }

    }

    static class TestLoggingHandler extends Handler {

        private String message;

        @Override
        public void publish(LogRecord record) {
            message = record.getMessage();
        }

        public String getMessage() {
            return message;
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }

}
