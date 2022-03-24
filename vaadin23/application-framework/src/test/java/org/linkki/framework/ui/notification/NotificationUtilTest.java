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

package org.linkki.framework.ui.notification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.linkki.framework.ui.notifications.NotificationUtil.LINKKI_NOTIFICATION_ERROR;
import static org.linkki.framework.ui.notifications.NotificationUtil.LINKKI_NOTIFICATION_INFO;
import static org.linkki.framework.ui.notifications.NotificationUtil.LINKKI_NOTIFICATION_WARNING;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.framework.ui.notifications.NotificationUtil;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.dom.Element;

public class NotificationUtilTest {

    private final UI ui = new UI();

    @BeforeEach
    public void setUp() {
        UI.setCurrent(ui);
    }

    @Test
    public void testSetInfoDuration() {
        NotificationUtil.setInfoDuration(3600);

        Notification notification = NotificationUtil.createNotification(Severity.INFO, "title", new Div());
        assertThat(notification.getDuration(), is(3600));
    }

    @Test
    public void testSetWarningDuration() {
        NotificationUtil.setWarningDuration(1800);

        Notification notification = NotificationUtil.createNotification(Severity.WARNING, "title", new Div());
        assertThat(notification.getDuration(), is(1800));
    }

    @Test
    public void testCreateNotification_InfoTheme() {
        Notification notification = NotificationUtil.createNotification(Severity.INFO, "title", new Div());

        assertThat(notification.hasThemeName(LINKKI_NOTIFICATION_INFO), is(true));
    }

    @Test
    public void testCreateNotification_WarningTheme() {
        Notification notification = NotificationUtil.createNotification(Severity.WARNING, "title", new Div());

        assertThat(notification.hasThemeName(LINKKI_NOTIFICATION_WARNING), is(true));
    }

    @Test
    public void testCreateNotification_ErrorTheme() {
        Notification notification = NotificationUtil.createNotification(Severity.ERROR, "title", new Div());

        assertThat(notification.hasThemeName(LINKKI_NOTIFICATION_ERROR), is(true));
    }

    @Test
    public void testShowInfo_HtmlContent() {
        Notification notification = NotificationUtil.showInfo("title", "<b>bold</b><a>anchor</a>");

        Element description = getContent(notification).get(1).getElement();
        assertThat(description.getProperty("innerHTML"), is("<b>bold</b>&lt;a&gt;anchor&lt;/a&gt;"));
    }

    @Test
    public void testShowWarning_HtmlContent() {
        Notification notification = NotificationUtil.showWarning("title", "<b>bold</b><a>anchor</a>");

        Element description = getContent(notification).get(1).getElement();
        assertThat(description.getProperty("innerHTML"), is("<b>bold</b>&lt;a&gt;anchor&lt;/a&gt;"));
    }

    @Test
    public void testShowError_HtmlContent() {
        Notification notification = NotificationUtil.showError("title", "<b>bold</b><a>anchor</a>");

        Element description = getContent(notification).get(1).getElement();
        assertThat(description.getProperty("innerHTML"), is("<b>bold</b>&lt;a&gt;anchor&lt;/a&gt;"));
    }

    private List<Component> getContent(Notification notification) {
        Component content = notification.getChildren()
                .filter(c -> c.getElement().getClassList().contains("linkki-notification-content"))
                .findFirst()
                .orElseThrow();

        return content.getChildren().collect(Collectors.toList());
    }

}
