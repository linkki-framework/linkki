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


package org.linkki.core.ui.test;

import java.util.List;
import java.util.logging.Logger;

import com.github.mvysny.kaributesting.v10.NotificationsKt;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasOrderedComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;

/**
 * Provides utility methods for testcases annotated with {@link KaribuUIExtension}
 */
public class KaribuUtils {

    public static final Logger LOGGER = Logger.getAnonymousLogger();

    private KaribuUtils() {
        // Utility Class
    }

    /**
     * Returns the currently opened notification. If zero or multiple notifications are present, a test
     * failure is caused.
     */
    public static Notification getNotification() {

        List<Notification> notifications = NotificationsKt.getNotifications();
        if (notifications.isEmpty()) {
            throw new AssertionError("No notifications are open");
        } else if (notifications.size() == 1) {
            return notifications.get(0);
        } else {
            throw new AssertionError("Multiple notifications are open");
        }
    }

    /**
     * Returns the title of a notification, assuming that the first child of the notification is a
     * wrapper component that contains an H3 as title.
     */
    public static String getNotificationTitle(Notification notification) {
        var wrapper = notification.getChildren()
                .filter(HasOrderedComponents.class::isInstance)
                .findFirst().orElseThrow(() -> new IllegalArgumentException(
                        "Notification must have a child that is a wrapper component"));
        return wrapper.getChildren()
                .filter(H3.class::isInstance)
                .map(H3.class::cast)
                .findFirst()
                .map(H3::getText)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Content wrapper of the notification does not contain a H3 as title"));
    }


    /**
     * Returns the root component. If it is not of the given type, a test failure is caused.
     */
    public static <T extends Component> T getRootComponent(Class<T> type) {
        List<Component> components = UI.getCurrent().getChildren()
                .filter(c -> !(c instanceof Notification))
                .toList();

        if (components.isEmpty()) {
            throw new AssertionError("No components attached to UI");
        } else if (components.size() == 1) {
            var component = components.get(0);
            if (type.isInstance(component)) {
                return type.cast(component);
            } else {
                throw new AssertionError("Root component is of type " + component.getClass().getName());
            }
        } else {
            throw new AssertionError("Multiple root components attached to UI: " + components);
        }
    }

    /**
     * Prints out all {@link Component component}-classnames in {@link UI#getCurrent()} in hierarchical
     * order
     */
    public static void printComponentTree() {
        printComponentTree(UI.getCurrent(), 0);
    }

    private static void printComponentTree(Component component, int level) {
        String msg = " ".repeat(level) + component.getClass().getName();
        LOGGER.info(msg);
        component.getChildren().forEach(c -> printComponentTree(c, level + 2));
    }

}
