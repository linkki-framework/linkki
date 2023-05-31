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

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.framework.ui.LinkkiApplicationTheme;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

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
     * @implNote the style names consist of the {@link LinkkiApplicationTheme#MESSAGE_PREFIX} and the
     *           {@link Severity#name() Severity's name}, for example {@code linkki-message-error}.
     */
    public static String getStyle(Severity severity) {
        return LinkkiApplicationTheme.MESSAGE_PREFIX + severity.name().toLowerCase();
    }

    /**
     * Returns an icon for the message's {@link Severity}.
     */
    public static VaadinIcon getIcon(Severity severity) {
        switch (severity) {
            case ERROR:
                return VaadinIcon.EXCLAMATION_CIRCLE;
            case WARNING:
                return VaadinIcon.WARNING;
            default:
                return VaadinIcon.INFO_CIRCLE;
        }
    }

    /**
     * Creates a {@link LinkkiText} that contains the message's text and an icon representing its
     * {@link Severity}.
     */
    public static Component createMessageComponent(Message message) {
        Severity severity = message.getSeverity();
        LinkkiText messageLabel = new LinkkiText(message.getText(), getIcon(severity));
        messageLabel.addClassNames(getStyle(severity));
        return messageLabel;
    }

    /**
     * Creates a table displaying all messages with the given {@link BindingContext}.
     */
    public static Component createMessageTable(Supplier<MessageList> messages, BindingContext bindingContext) {
        Grid<MessageRowPmo> messagesGrid = GridComponentCreator
                .createGrid(new MessageTablePmo(messages), bindingContext);
        messagesGrid.setWidthFull();
        messagesGrid.setSelectionMode(SelectionMode.NONE);
        return messagesGrid;
    }

    /**
     * Creates a table displaying all messages under the given title with the given
     * {@link BindingContext}.
     */
    public static Component createMessageTable(String title,
            Supplier<MessageList> messages,
            BindingContext bindingContext) {
        Component messageTable = createMessageTable(messages, bindingContext);
        Span span = new Span(title);
        messageTable.getId().ifPresent(s -> span.setId(s + "_title"));
        VerticalLayout content = new VerticalLayout(span, messageTable);
        content.setPadding(false);
        return content;
    }
}
