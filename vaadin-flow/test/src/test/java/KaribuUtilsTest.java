
/*******************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************/

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.shared.communication.PushMode;

@ExtendWith(KaribuUIExtension.class)
class KaribuUtilsTest {

    @Test
    void testGetNotification() {

        Notification notification = new Notification();
        notification.open();
        assertThat(KaribuUtils.getNotification()).isSameAs(notification);
    }

    @Test
    void testGetNotification_NoNotifications() {
        assertThrows(AssertionError.class, KaribuUtils::getNotification);
    }

    @Test
    void testGetNotification_MultipleNotifications() {
        new Notification().open();
        new Notification().open();
        assertThrows(AssertionError.class, KaribuUtils::getNotification);
    }

    @Test
    void testGetNotificationTitle() {
        var notification = new Notification(new Div(new H3("notification title")));

        notification.open();

        assertThat(KaribuUtils.getNotificationTitle(notification)).isEqualTo("notification title");
    }

    @Test
    void testPrintComponentTree() {
        TestLoggingHandler testLoggingHandler = new TestLoggingHandler();
        KaribuUtils.LOGGER.addHandler(testLoggingHandler);

        KaribuUtils.printComponentTree();

        assertThat(testLoggingHandler.getMessage()).isEqualTo("com.github.mvysny.kaributesting.v10.mock.MockedUI");
        KaribuUtils.LOGGER.removeHandler(testLoggingHandler);
    }

    @Test
    void testPush() {
        var ui = UI.getCurrent();
        ui.getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
        var command = mock(Command.class);
        ui.access(command);
        verifyNoInteractions(command);

        KaribuUtils.UI.push(ui);

        verify(command, times(1)).execute();
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
