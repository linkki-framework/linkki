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

package org.linkki.core.ui.test;

import static com.github.mvysny.kaributesting.v10.LocatorJ._fireValueChange;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.awaitility.Awaitility.await;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.linkki.core.binding.validation.message.Severity;

import com.github.mvysny.kaributesting.v10.ComboBoxKt;
import com.github.mvysny.kaributesting.v10.GridKt;
import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.github.mvysny.kaributesting.v10.NotificationsKt;
import com.github.mvysny.kaributesting.v10.PrettyPrintTree;
import com.github.mvysny.kaributesting.v10.PrettyPrintTreeKt;
import com.github.mvysny.kaributesting.v10.TestingLifecycleHook;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.ComponentRenderer;

/**
 * Provides utility methods for testcases annotated with {@link KaribuUIExtension}
 */
public class KaribuUtils {

    public static final Logger LOGGER = Logger.getAnonymousLogger();

    private KaribuUtils() {
        // Utility Class
    }

    /**
     * Returns the currently opened notification. If zero or multiple notifications are present, a
     * test failure is caused.
     * 
     * @deprecated use {@link Notifications#get()} instead.
     */
    @Deprecated(since = "2.6.0")
    public static Notification getNotification() {
        return Notifications.get();
    }

    /**
     * Returns the title of a notification, assuming that the first child of the notification is a
     * wrapper component that contains an H3 as title.
     * 
     * @deprecated use {@link Notifications#getTitle(Notification)} instead.
     */
    @Deprecated(since = "2.6.0")
    public static String getNotificationTitle(Notification notification) {
        return Notifications.getTitle(notification);
    }

    /**
     * Returns the root component. If it is not of the given type, a test failure is caused.
     */
    public static <T extends Component> T getRootComponent(Class<T> type) {
        List<Component> components = com.vaadin.flow.component.UI.getCurrent().getChildren()
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
     * Gets a component of the given class with the given ID.
     *
     * @see com.github.mvysny.kaributesting.v10.LocatorJ#_get(Class, Consumer)
     */
    public static <T extends Component> T getWithId(Class<T> componentClass, String id) {
        return _get(componentClass, sp -> sp.withId(id));
    }

    /**
     * Gets a component of the given class with the given ID in the given parent component.
     *
     * @see com.github.mvysny.kaributesting.v10.LocatorJ#_get(Class, Consumer)
     */
    public static <T extends Component> T getWithId(Component parent, Class<T> componentClass, String id) {
        return _get(parent, componentClass, sp -> sp.withId(id));
    }

    /**
     * Prints out all {@link Component component}-classnames in
     * {@link com.vaadin.flow.component.UI#getCurrent()} in hierarchical order
     */
    public static String printComponentTree() {
        return printComponentTree(com.vaadin.flow.component.UI.getCurrent());
    }

    /**
     * Prints out the given component.
     */
    public static String printComponentTree(Component component) {
        var tree = getComponentTree(component);
        LOGGER.info(tree);
        return tree;
    }

    /**
     * Gets the DOM of the given {@link Component component} in a hierarchical order
     */
    public static String getComponentTree(Component component) {
        return getPrettyPrintTree(component).print();
    }

    private static PrettyPrintTree getPrettyPrintTree(Component component) {
        var result = new PrettyPrintTree(PrettyPrintTreeKt.toPrettyString(component), new ArrayList<>());
        for (var child : getAllChildren(component)) {
            result.getChildren().add(getPrettyPrintTree(child));
        }
        return result;
    }

    private static List<Component> getAllChildren(Component component) {
        var children = new ArrayList<>(TestingLifecycleHook.getDefault().getAllChildren(component));
        if (component instanceof Grid<?> grid) {
            children.addAll(Row.fromGrid(grid));
        }
        return children;
    }

    /**
     * Gets the text content of the given component.
     */
    @SuppressWarnings("RedundantExplicitVariableType")
    public static String getTextContent(Component component) {
        if (component instanceof HasValue<?, ?> hasValue) {
            return String.valueOf(hasValue.getValue());
        } else {
            return component.getElement().getTextRecursively();
        }
    }

    @Tag("irrelevant")
    private static class Row extends Component {

        @Serial
        private static final long serialVersionUID = 1L;
        private final String prettyPrintRow;

        private Row(String prettyPrintRow) {
            this.prettyPrintRow = prettyPrintRow;
        }

        public String toString() {
            return prettyPrintRow;
        }

        private static List<Row> fromGrid(Grid<?> grid) {
            return IntStream.range(0, GridKt._size(grid))
                    .mapToObj(i -> getFormattedRow(grid, i))
                    .map(Row::new)
                    .toList();
        }

        private static <T> String getFormattedRow(Grid<T> grid, int rowIndex) {
            var formattedRow = GridKt._getFormattedRow(grid, rowIndex).toString();

            if (grid instanceof TreeGrid<T> treeGrid) {
                var row = GridKt._get(grid, rowIndex);
                if (treeGrid.isExpanded(row)) {
                    formattedRow += ", expanded";
                } else {
                    if (GridKt._rowSequence(treeGrid.getDataProvider(), row).iterator().hasNext()) {
                        formattedRow += ", collapsed";
                    } else {
                        formattedRow += ", isLeaf";
                    }
                }
            }
            return formattedRow;
        }
    }

    /**
     * Utilities for the {@link com.vaadin.flow.component.UI} class.
     */
    public static class UI {

        private UI() {
            // utility class
        }

        /**
         * Pushes the current UI
         *
         * @see #push()
         */
        public static void push() {
            push(com.vaadin.flow.component.UI.getCurrent());
        }

        /**
         * Manually executes the UI access as atmosphere is not present with the Karibu setup.
         * <p>
         * Waits until the UI.access call has reached the session, then manually executes the
         * command.
         */
        public static void push(com.vaadin.flow.component.UI ui) {
            await().atMost(1, TimeUnit.SECONDS).until(() -> !ui.getSession().getPendingAccessQueue().isEmpty());
            ui.getSession().getService().runPendingAccessTasks(ui.getSession());
        }
    }

    /**
     * Utilities for the {@link AbstractField} und its subclasses (i.e.
     * {@link com.vaadin.flow.component.textfield.TextField},
     * {@link com.vaadin.flow.component.checkbox.Checkbox}, ...).
     */
    public static class Fields {

        private Fields() {
            // utility class
        }

        public static <C extends AbstractField<C, T>, T> void setValue(C field, T value) {
            field.setValue(value);
            LocatorJ._fireValueChange(field, true);
        }

        /**
         * To set the value of a field in a parent layout, use
         * {@link #getWithId(Component, Class, String)} with
         * {@link #setValue(AbstractField, Object)} instead.
         */
        public static <C extends AbstractField<C, T>, T> void setValue(Class<C> clazz,
                String id,
                T value) {
            var component = _get(clazz, ss -> ss.withId(id));
            setValue(component, value);
        }

    }

    /**
     * Utilities for {@link ComboBox}.
     */
    public static class ComboBoxes extends Fields {

        private ComboBoxes() {
            // utility class
        }

        /**
         * Gets a combo box with the given ID in the given parent without producing unchecked
         * warning.
         */
        @SuppressWarnings("unchecked")
        public static <T> ComboBox<T> getWithId(Component parent, String id) {
            return KaribuUtils.getWithId(parent, ComboBox.class, id);
        }

        /**
         * Gets a combo box with the given ID without producing unchecked warning.
         */
        @SuppressWarnings("unchecked")
        public static <T> ComboBox<T> getWithId(String id) {
            return KaribuUtils.getWithId(ComboBox.class, id);
        }

        /**
         * Sets the value in a combo box without producing unchecked warning
         */
        @SuppressWarnings("unchecked")
        public static <T> void setValue(String id, T value) {
            setValue(ComboBox.class, id, value);
        }

        /**
         * Sets the value in a combo box to the value that corresponds to the given label
         */
        public static void setValueByLabel(String id, String label) {
            var comboBox = getWithId(id);
            setValueByLabel(comboBox, label);
        }

        /**
         * Sets the value in a combo box to the value that corresponds to the given label
         */
        public static void setValueByLabel(Component parent, String id, String label) {
            var comboBox = getWithId(parent, id);
            setValueByLabel(comboBox, label);
        }

        /**
         * Sets the value in a combo box to the value that corresponds to the given label
         */
        public static void setValueByLabel(ComboBox<?> comboBox, String label) {
            ComboBoxKt.selectByLabel(comboBox, label);
            _fireValueChange(comboBox, true);
        }
    }

    /**
     * Utilities for notifications created with NotificationUtil.
     */
    public static class Notifications {

        private Notifications() {
            // utility class
        }

        /**
         * Returns the currently opened notification. If zero or multiple notifications are present,
         * a test failure is caused.
         */
        public static Notification get() {
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
         * Returns the title of a notification that is created with NotificationUtil.
         */
        public static String getTitle(Notification notification) {
            return getTitleComponent(notification).getText();
        }

        /**
         * Returns the description of the notification that is created with NotificationUtil.
         * <p>
         * If the notification is not created with a description, use
         * {@link #getContentComponents(Notification)} instead.
         */
        public static String getDescription(Notification notification) {
            var content = getContent(notification);
            return LocatorJ._get(content, Div.class, ss -> ss.withPredicate(c -> c != content))
                    .getElement().getProperty("innerHTML");
        }

        /**
         * Returns the content components of a notification that is created with NotificationUtil.
         */
        public static List<Component> getContentComponents(Notification notification) {
            return getContent(notification).getChildren()
                    .filter(c -> c != getTitleComponent(notification)).toList();
        }

        public static Severity getSeverity(Notification notification) {
            var themeNames = notification.getThemeNames();
            if (themeNames.contains(Severity.ERROR.name().toLowerCase())) {
                return Severity.ERROR;
            } else if (themeNames.contains(Severity.WARNING.name().toLowerCase())) {
                return Severity.WARNING;
            } else if (themeNames.contains(Severity.INFO.name().toLowerCase())) {
                return Severity.INFO;
            } else {
                throw new AssertionError(
                        "No severity theme found on the notification. " +
                                "This is probably because the Notification is not created with NotificationUtil.");
            }
        }

        private static Div getContent(Notification notification) {
            return LocatorJ._get(notification, Div.class, ss -> ss.withClasses("linkki-notification-content"));
        }

        private static H3 getTitleComponent(Notification notification) {
            return LocatorJ._get(getContent(notification), H3.class);
        }

    }

    /**
     * Provides methods to interact with OkCancelDialog.
     * <p>
     * To retrieve the currently opened OkCancelDialog, use
     * {@link com.github.mvysny.kaributesting.v10.LocatorJ#_get(Class)}.
     */
    public static class Dialogs {

        private Dialogs() {
            // utility class
        }

        /**
         * Returns the OK button of the OkCancelDialog.
         * <p>
         * Note that the handler will only be triggered if the dialog is opened.
         */
        public static Button getOkButton(Composite<Dialog> dialog) {
            return getOkButton(dialog.getContent());
        }

        /**
         * Clicks on the OK button in the dialog, assuming that there is only one visible dialog.
         */
        public static void clickOkButton() {
            getOkButton(_get(Dialog.class)).click();
        }

        /**
         * Returns the cancel button of the OkCancelDialog.
         * <p>
         * Note that the handler will only be triggered if the dialog is opened.
         */
        public static Button getCancelButton(Composite<Dialog> dialog) {
            return getCancelButton(dialog.getContent());
        }

        /**
         * Clicks on the cancel button in the dialog, assuming that there is only one visible
         * dialog.
         */
        public static void clickCancelButton() {
            getCancelButton(_get(Dialog.class)).click();
        }

        /**
         * Returns the content components in a dialog.
         */
        public static List<Component> getContents(Composite<Dialog> dialog) {
            return _get(dialog.getContent(), VerticalLayout.class, ss -> ss.withClasses("linkki-dialog-content-area"))
                    .getChildren().toList();
        }

        /**
         * Returns the component of the first shown validation message in the OkCancelDialog.
         */
        public static Component getFirstMessage(Composite<Dialog> dialog) {
            return _get(dialog, Div.class, ss -> ss.withClasses("linkki-dialog-message-area"))
                    .getChildren().findFirst().orElseThrow(() -> new AssertionError("No visible message found"));
        }

        private static Button getOkButton(Dialog dialog) {
            return _get(dialog, Button.class, ss -> ss.withId("okButton"));
        }

        private static Button getCancelButton(Dialog dialog) {
            return _get(dialog, Button.class, ss -> ss.withId("cancelButton"));
        }

    }

    /**
     * Utilities for {@link Grid}.
     */
    public static class Grids {

        private Grids() {
            // utility class
        }

        /**
         * Gets the grid without producing unchecked warning.
         */
        @SuppressWarnings("unchecked")
        public static <T> Grid<T> get(Component parent) {
            return (Grid<T>)_get(parent, Grid.class);
        }

        /**
         * Gets the grid that is created from the given PMO class without producing unchecked
         * warning.
         */
        @SuppressWarnings("unchecked")
        public static <T> Grid<T> getWithPmo(Class<?> pmoClass) {
            return (Grid<T>)_get(Grid.class, sp -> sp.withId(pmoClass.getSimpleName() + "_table"));
        }

        /**
         * Returns a list of the texts displayed in each row of a {@link Grid.Column}.
         */
        public static <T> List<String> getTextContentsInColumn(Grid<T> grid, String columnKey) {
            return IntStream.range(0, GridKt._size(grid))
                    .mapToObj(i -> grid.getColumnByKey(columnKey).getRenderer() instanceof ComponentRenderer<?, ?>
                            ? getTextContent(GridKt._getCellComponent(grid, i, columnKey))
                            : GridKt._getFormatted(grid, i, columnKey))
                    .toList();
        }

    }

    /**
     * Utilities for layouts.
     */
    public static class Layouts {

        private Layouts() {
            // utility class
        }

        /**
         * Gets the layout that is based on a specific pmo class.
         */
        public static <T extends Component> T getWithPmo(Class<T> componentClass, Class<?> pmoClass) {
            return _get(componentClass, sp -> sp.withId(pmoClass.getSimpleName()));
        }

    }

}
