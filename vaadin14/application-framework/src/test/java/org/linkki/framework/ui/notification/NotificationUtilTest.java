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
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.framework.ui.notifications.NotificationUtil;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class NotificationUtilTest {

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

        assertThat(notification.getThemeName(), is(nullValue()));
    }

    @Test
    public void testCreateNotification_WarningTheme() {
        Notification notification = NotificationUtil.createNotification(Severity.WARNING, "title", new Div());

        String variant = NotificationVariant.LUMO_PRIMARY.getVariantName();
        assertThat(notification.hasThemeName(variant), is(true));
    }

    @Test
    public void testCreateNotification_ErrorTheme() {
        Notification notification = NotificationUtil.createNotification(Severity.ERROR, "title", new Div());

        String variant = NotificationVariant.LUMO_ERROR.getVariantName();
        assertThat(notification.hasThemeName(variant), is(true));
    }

}
