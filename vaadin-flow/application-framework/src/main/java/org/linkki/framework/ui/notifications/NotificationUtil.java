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

package org.linkki.framework.ui.notifications;

import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.util.HtmlSanitizer;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.framework.ui.nls.NlsText;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/linkki-notification.css")
public class NotificationUtil {

    public static final String LINKKI_NOTIFICATION_INFO = "info";
    public static final String LINKKI_NOTIFICATION_WARNING = "warning";
    public static final String LINKKI_NOTIFICATION_ERROR = "error";
    public static final int DEFAULT_DURATION_INFO = 3000;
    public static final int DEFAULT_DURATION_WARNING = 6000;

    private static int infoDuration = DEFAULT_DURATION_INFO;
    private static int warningDuration = DEFAULT_DURATION_WARNING;

    private NotificationUtil() {
        // prevent instantiation
    }

    /**
     * Sets the duration in milliseconds for info notifications created using this class. The
     * default is {@value DEFAULT_DURATION_INFO}. If the duration is less than/equal to 0, info
     * notifications will not close automatically.
     * 
     * @see Notification#setDuration(int)
     */
    public static void setInfoDuration(int duration) {
        infoDuration = duration;
    }

    /**
     * Sets the duration in milliseconds for warning notifications created using this class. The
     * default is {@value DEFAULT_DURATION_WARNING}. If the duration is less than/equal to 0,
     * warning notifications will not close automatically.
     * 
     * @see Notification#setDuration(int)
     */
    public static void setWarningDuration(int duration) {
        warningDuration = duration;
    }

    /**
     * Creates and opens a notification with the {@link MessageList#getSeverity() maximum severity}
     * contained in the message list. The notification contains a list of all messages with an icon
     * corresponding to their individual severity. If the given message list is empty, an info
     * notification containing only the title will be displayed. The messages support HTML content,
     * which will be {@link HtmlSanitizer#sanitizeText(String) sanitized}.
     */
    public static Notification show(String title, MessageList messages) {
        if (!messages.isEmpty()) {
            Severity severity = messages.getSeverity().orElseThrow();
            Notification notification = createNotification(severity, title, createMessageListComponent(messages));
            notification.open();
            return notification;
        } else {
            Notification notification = createNotification(Severity.INFO, title);
            notification.open();
            return notification;
        }
    }

    /**
     * Creates and opens an info notification. Info notifications close automatically after the
     * {@link #setInfoDuration(int) specified duration} unless this duration is less than/equal to
     * 0. The description supports HTML content, which will be
     * {@link HtmlSanitizer#sanitizeText(String) sanitized}.
     * 
     * @return the shown notification
     */
    public static Notification showInfo(String title, String description) {
        Notification notification = createNotification(Severity.INFO, title, createContent(description));
        notification.open();
        return notification;
    }

    /**
     * Creates and opens a warning notification. Warning notifications close automatically after the
     * {@link #setWarningDuration(int) specified duration} unless this duration is less than/equal
     * to 0. The description supports HTML content, which will be
     * {@link HtmlSanitizer#sanitizeText(String) sanitized}.
     * 
     * @return the shown notification
     */
    public static Notification showWarning(String title, String description) {
        Notification notification = createNotification(Severity.WARNING, title, createContent(description));
        notification.open();
        return notification;
    }

    /**
     * Creates and opens an error notification. Error notifications do not close automatically, the
     * close button has to be clicked. The description supports HTML content, which will be
     * {@link HtmlSanitizer#sanitizeText(String) sanitized}.
     * 
     * @return the shown notification
     */
    public static Notification showError(String title, String description) {
        Notification notification = createNotification(Severity.ERROR, title, createContent(description));
        notification.open();
        return notification;
    }

    /**
     * Creates a notification without opening it.
     * 
     * @return the notification
     */
    public static Notification createNotification(Severity severity, String title, Component... components) {
        Div content = new Div();
        content.addClassName("linkki-notification-content");
        content.add(new H3(title));
        content.add(components);

        Notification notification = new Notification(content);
        if (severity == Severity.INFO) {
            prepareNotification(notification, infoDuration, LINKKI_NOTIFICATION_INFO);
        } else if (severity == Severity.WARNING) {
            prepareNotification(notification, warningDuration, LINKKI_NOTIFICATION_WARNING);
        } else if (severity == Severity.ERROR) {
            prepareNotification(notification, 0, LINKKI_NOTIFICATION_ERROR);
        }

        return notification;
    }

    private static void prepareNotification(Notification notification, int duration, String themeName) {
        notification.setDuration(duration);
        notification.addThemeName(themeName);
        notification.setPosition(Position.TOP_CENTER);

        Button closeButton = new Button(NlsText.getString("NotificationUtil.CloseButtonCaption"),
                e -> notification.close());
        notification.add(closeButton);
    }

    private static Div createContent(String description) {
        Div content = new Div();
        content.getElement().setProperty("innerHTML", HtmlSanitizer.sanitizeText(description));
        return content;
    }

    private static Component createMessageListComponent(MessageList messages) {
        VerticalLayout component = new VerticalLayout();
        component.setClassName("linkki-notification-messagelist");
        component.setPadding(false);
        messages.forEach(m -> {
            LinkkiText m2 = new LinkkiText();
            m2.setText(HtmlSanitizer.sanitizeText(m.getText()), true);
            if (m.getSeverity() == Severity.INFO) {
                m2.setIcon(VaadinIcon.INFO_CIRCLE);
            } else if (m.getSeverity() == Severity.WARNING) {
                m2.setIcon(VaadinIcon.WARNING);
            } else if (m.getSeverity() == Severity.ERROR) {
                m2.setIcon(VaadinIcon.CLOSE_CIRCLE);
            }

            component.add(m2);
        });

        return component;
    }
}
