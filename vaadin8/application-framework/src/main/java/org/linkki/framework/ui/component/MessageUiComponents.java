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

package org.linkki.framework.ui.component;

import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.core.message.ObjectProperty;
import org.linkki.core.message.Severity;
import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.core.ui.table.PmoBasedTableFactory;
import org.linkki.framework.ui.LinkkiStyles;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Utility class for the creation of different components to display {@link Message Messages} and
 * {@link MessageList MessageLists}.
 */
public final class MessageUiComponents {

    private MessageUiComponents() {
        // prevent instantiation
    }

    /**
     * Returns the CSS style name for the message's {@link Severity}.
     * 
     * @implNote the style names consist of the {@link LinkkiStyles#MESSAGE_PREFIX} and the
     *           {@link Severity#name() Severity's name}, for example {@code linkki-message-error}.
     */
    public static String getStyle(Severity severity) {
        return LinkkiStyles.MESSAGE_PREFIX + severity.name().toLowerCase();
    }

    /**
     * Returns an icon for the message's {@link Severity}.
     */
    public static VaadinIcons getIcon(Severity severity) {
        switch (severity) {
            case ERROR:
                return VaadinIcons.EXCLAMATION_CIRCLE;
            case WARNING:
                return VaadinIcons.WARNING;
            default:
                return VaadinIcons.INFO_CIRCLE;
        }
    }

    /**
     * Concatenates the {@link Message#getInvalidObjectProperties() message's invalid object
     * properties}.
     */
    public static String getInvalidObjectPropertiesAsString(Message message) {
        String text = message.getInvalidObjectProperties().stream()
                .map(MessageUiComponents::toString)
                .collect(Collectors.joining(", "));
        return text;
    }

    private static String toString(ObjectProperty op) {
        String simpleName = op.getObject().getClass().getSimpleName();
        if (StringUtils.isEmpty(op.getProperty())) {
            return simpleName;
        } else {
            return simpleName + ": " + op.getProperty();
        }
    }

    /**
     * Creates a label with the message's text and an icon representing its {@link Severity}. The label
     * can be styled with {@link ApplicationStyles#MESSAGE_LABEL} and a {@link #getStyle(Severity) style
     * derived from the severity}.
     */
    public static Label createMessageLabel(Message message) {
        Label messageLabel = new Label();
        messageLabel.setCaption(message.getText());
        Severity severity = message.getSeverity();
        messageLabel.setIcon(getIcon(severity));
        messageLabel.addStyleName(ApplicationStyles.MESSAGE_LABEL);
        messageLabel.addStyleName(getStyle(severity));
        messageLabel.setDescription(getInvalidObjectPropertiesAsString(message));
        messageLabel.setWidthUndefined();
        return messageLabel;
    }

    /**
     * Creates a table displaying all messages with the given {@link BindingContext}.
     */
    @SuppressWarnings("deprecation")
    public static Component createMessageTable(Supplier<MessageList> messages, BindingContext bindingContext) {
        com.vaadin.v7.ui.Table table = new PmoBasedTableFactory(new MessageTablePmo(messages), bindingContext)
                .createTable();
        table.setColumnHeaderMode(com.vaadin.v7.ui.Table.ColumnHeaderMode.HIDDEN);
        table.setStyleName(ApplicationStyles.MESSAGE_TABLE);
        table.setWidth("100%");
        return table;
    }

    /**
     * Creates a table displaying all messages under the given tile with the given
     * {@link BindingContext}.
     */
    public static Component createMessageTable(String title,
            Supplier<MessageList> messages,
            BindingContext bindingContext) {
        Component messageTable = createMessageTable(messages, bindingContext);
        messageTable.setCaption(title);
        return messageTable;
    }
}
