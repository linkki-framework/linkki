package org.linkki.framework.ui.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.framework.ui.notifications.NotificationUtil;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;

@ExtendWith(KaribuUIExtension.class)
class KaribuUtilsTest {

    @Test
    void testFields_SetValue() {
        var pmo = new FieldsTestPmo();
        var bindingContext = new BindingContext();
        var textField = (TextField)UiCreator.<Component, NoLabelComponentWrapper> createUiElements(pmo, bindingContext,
                                                                                                   NoLabelComponentWrapper::new)
                .findFirst().get().getComponent();

        textField.setValue("value");
        assertThat(pmo.getValue())
                .as("setValue does not work. If this works, the tested method is not necessary anymore")
                .isNotEqualTo("value");

        KaribuUtils.Fields.setValue(textField, "value");
        assertThat(pmo.getValue()).isEqualTo("value");
    }

    @Test
    void testNotifications_Get() {
        var notification = NotificationUtil.showInfo("title", "description");

        assertThatNoException().isThrownBy(KaribuUtils.Notifications::get);
        assertThat(KaribuUtils.Notifications.get()).isEqualTo(notification);
    }

    @Test
    void testNotifications_Get_NoNotifications() {
        assertThatExceptionOfType(AssertionError.class).isThrownBy(KaribuUtils.Notifications::get);
    }

    @Test
    void testNotifications_Get_MultipleNotifications() {
        NotificationUtil.showInfo("title", "description");
        NotificationUtil.showInfo("title", "description");

        assertThatExceptionOfType(AssertionError.class).isThrownBy(KaribuUtils.Notifications::get);
    }

    @Test
    void testNotifications_GetTitle() {
        var title = "title";
        var notification = NotificationUtil.showInfo(title, "description");

        assertThatNoException().isThrownBy(() -> KaribuUtils.Notifications.getTitle(notification));
        assertThat(KaribuUtils.Notifications.getTitle(notification)).isEqualTo(title);
    }

    @Test
    void testNotifications_GetTitle_NotCreatedWithNotificationUtil() {
        var notification = Notification.show("text");

        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> KaribuUtils.Notifications.getTitle(notification));
    }

    @Test
    void testNotifications_GetDescription() {
        var description = "description";
        var notification = NotificationUtil.showInfo("title", description);

        assertThatNoException().isThrownBy(() -> KaribuUtils.Notifications.getDescription(notification));
        assertThat(KaribuUtils.Notifications.getDescription(notification)).isEqualTo(description);
    }

    @Test
    void testNotifications_GetDescription_NotCreatedWithDescription() {
        var notification = NotificationUtil.show("title", new MessageList());

        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> KaribuUtils.Notifications.getDescription(notification));
    }

    @Test
    void testNotifications_GetContentComponents() {
        var component1 = new Div();
        var component2 = new Div();
        var notification = NotificationUtil.createNotification(Severity.INFO, "title", component1, component2);
        notification.open();

        assertThat(KaribuUtils.Notifications.getContentComponents(notification)).containsExactly(component1,
                                                                                                 component2);
    }

    @Test
    void testNotifications_getSeverity_Info() {
        var notification = NotificationUtil.showInfo("title", "");

        assertThat(KaribuUtils.Notifications.getSeverity(notification)).isEqualTo(Severity.INFO);
    }

    @Test
    void testNotifications_getSeverity_Warning() {
        var notification = NotificationUtil.showWarning("title", "");

        assertThat(KaribuUtils.Notifications.getSeverity(notification)).isEqualTo(Severity.WARNING);
    }

    @Test
    void testNotifications_getSeverity_Error() {
        var notification = NotificationUtil.showError("title", "");

        assertThat(KaribuUtils.Notifications.getSeverity(notification)).isEqualTo(Severity.ERROR);
    }

    @Test
    void testNotifications_getSeverity_NotCreatedWithNotificationUtil() {
        var notification = Notification.show("");

        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> KaribuUtils.Notifications.getSeverity(notification));
    }

    @Test
    void testDialogs_GetOkButton() {
        var caption = "OK caption";
        var dialog = OkCancelDialog.builder("caption").okCaption(caption).build();

        var button = KaribuUtils.Dialogs.getOkButton(dialog);

        assertThat(button.getText()).isEqualTo(caption);
    }

    @Test
    void testDialogs_GetCancelButton() {
        var caption = "cancel caption";
        var dialog = OkCancelDialog.builder("caption").cancelCaption(caption).build();
        dialog.open();

        var button = KaribuUtils.Dialogs.getCancelButton(dialog);

        assertThat(button.getText()).isEqualTo(caption);
    }

    @Test
    void testDialogs_ClickOkButton_OpenedDialog() {
        var okHandler = mock(Handler.class);
        OkCancelDialog.builder("caption").okHandler(okHandler).build().open();

        KaribuUtils.Dialogs.clickOkButton();

        verify(okHandler).apply();
    }

    @Test
    void testDialogs_ClickCancelButton() {
        var cancelHandler = mock(Handler.class);
        OkCancelDialog.builder("caption").cancelHandler(cancelHandler).build().open();

        KaribuUtils.Dialogs.clickCancelButton();

        verify(cancelHandler).apply();
    }

    @Test
    void testDialogs_getContents() {
        var component1 = new Div();
        var component2 = new Div();
        var dialog = OkCancelDialog.builder("caption").content(component1, component2).build();

        var components = KaribuUtils.Dialogs.getContents(dialog);

        assertThat(components).containsExactly(component1, component2);
    }

    public static class FieldsTestPmo {
        private String value;

        @UITextField(position = 0)
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
