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

package org.linkki.framework.ui.notification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.linkki.framework.ui.notifications.NotificationUtil.LINKKI_NOTIFICATION_ERROR;
import static org.linkki.framework.ui.notifications.NotificationUtil.LINKKI_NOTIFICATION_INFO;
import static org.linkki.framework.ui.notifications.NotificationUtil.LINKKI_NOTIFICATION_WARNING;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.WithLocale;
import org.linkki.framework.ui.notifications.NotificationUtil;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.dom.Element;

@WithLocale("en_US")
@ExtendWith(KaribuUIExtension.class)
class NotificationUtilTest {

    // Reset durations to avoid conflicts
    @AfterEach
    void cleanup() {
        NotificationUtil.setInfoDuration(3000);
        NotificationUtil.setWarningDuration(3000);
    }

    @Test
    void testSetInfoDuration() {
        NotificationUtil.setInfoDuration(3600);

        Notification notification = NotificationUtil.createNotification(Severity.INFO, "title", new Div());
        assertThat(notification.getDuration(), is(3600));
        assertThat(hasCloseButton(notification), is(false));
    }

    @Test
    void testSetWarningDuration() {
        NotificationUtil.setWarningDuration(1800);

        Notification notification = NotificationUtil.createNotification(Severity.WARNING, "title", new Div());
        assertThat(notification.getDuration(), is(1800));
        assertThat(hasCloseButton(notification), is(false));
    }

    @Test
    void testCreateNotification_InfoTheme() {
        Notification notification = NotificationUtil.createNotification(Severity.INFO, "title", new Div());

        assertThat(notification.hasThemeName(LINKKI_NOTIFICATION_INFO), is(true));
    }

    @Test
    void testCreateNotification_WarningTheme() {
        Notification notification = NotificationUtil.createNotification(Severity.WARNING, "title", new Div());

        assertThat(notification.hasThemeName(LINKKI_NOTIFICATION_WARNING), is(true));
    }

    @Test
    void testCreateNotification_ErrorTheme() {
        Notification notification = NotificationUtil.createNotification(Severity.ERROR, "title", new Div());

        assertThat(notification.hasThemeName(LINKKI_NOTIFICATION_ERROR), is(true));
        assertThat(hasCloseButton(notification), is(true));
    }

    @Test
    void testShowInfo_HtmlContent() {
        Notification notification = NotificationUtil.showInfo("title", "<b>bold</b><a>anchor</a>");

        Element description = getContent(notification).get(1).getElement();
        assertThat(description.getProperty("innerHTML"), is("<b>bold</b><a rel=\"nofollow\">anchor</a>"));
    }

    @Test
    void testShowWarning_HtmlContent() {
        Notification notification = NotificationUtil.showWarning("title", "<b>bold</b><a>anchor</a>");

        Element description = getContent(notification).get(1).getElement();
        assertThat(description.getProperty("innerHTML"), is("<b>bold</b><a rel=\"nofollow\">anchor</a>"));
    }

    @Test
    void testShowError_HtmlContent() {
        Notification notification = NotificationUtil.showError("title", "<b>bold</b><a>anchor</a>");

        Element description = getContent(notification).get(1).getElement();
        assertThat(description.getProperty("innerHTML"), is("<b>bold</b><a rel=\"nofollow\">anchor</a>"));
    }

    @Test
    void testShow_HtmlContent() {
        MessageList messageList = new MessageList(Message.newInfo("infoTitle", "<b>bold</b><a>anchor</a>"),
                Message.newWarning("warningTitle", "<b>bold</b><i>italic</i>"));
        Notification notification = NotificationUtil.show("title", messageList);

        String infoMessageDescription = getContent(notification).get(1).getElement().getChild(0).getChild(0)
                .getProperty("innerHTML");
        String warningMessageDescription = getContent(notification).get(1).getElement().getChild(1).getChild(0)
                .getProperty("innerHTML");

        assertThat(infoMessageDescription, is("<b>bold</b><a rel=\"nofollow\">anchor</a>"));
        assertThat(warningMessageDescription, is("<b>bold</b><i>italic</i>"));
    }

    @Test
    void testSetInfoDuration_BelowZero_CloseButton() {
        NotificationUtil.setInfoDuration(-1);

        Notification notification = NotificationUtil.createNotification(Severity.INFO, "title", new Div());

        assertThat(notification.getDuration(), is(-1));
        assertThat(hasCloseButton(notification), is(true));
    }

    @Test
    void testSetWarningDuration_BelowZero_CloseButton() {
        NotificationUtil.setWarningDuration(-1);

        Notification notification = NotificationUtil.createNotification(Severity.WARNING, "title", new Div());

        assertThat(notification.getDuration(), is(-1));
        assertThat(hasCloseButton(notification), is(true));
    }

    private List<Component> getContent(Notification notification) {
        Component content = notification.getChildren()
                .filter(c -> c.getElement().getClassList().contains("linkki-notification-content"))
                .findFirst()
                .orElseThrow();

        return content.getChildren().collect(Collectors.toList());
    }

    private boolean hasCloseButton(Notification notification) {
        String expected = "<vaadin-button>\n Close\n</vaadin-button>";
        return notification.getChildren()
                .anyMatch(c -> expected.equals(c.getElement().getOuterHTML()));
    }
}
