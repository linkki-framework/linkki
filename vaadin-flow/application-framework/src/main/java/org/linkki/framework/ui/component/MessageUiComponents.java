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

package org.linkki.framework.ui.component;

import java.util.List;
import java.util.function.Supplier;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.BindingDescriptor;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.vaadin.component.base.LinkkiText;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Utility class for the creation of different components to display {@link Message Messages} and
 * {@link MessageList MessageLists}.
 */
public final class MessageUiComponents {

    public static final String ICON_STYLE_CLASS_PREFIX = "icon-";

    private MessageUiComponents() {
        // prevent instantiation
    }

    /**
     * Returns the CSS style name for the message's {@link Severity}.
     * 
     * @implNote the style names consist of the {@link #ICON_STYLE_CLASS_PREFIX} and the
     *           {@link Severity#name() Severity's name}, for example {@code icon-error}.
     */
    public static String getStyle(Severity severity) {
        return ICON_STYLE_CLASS_PREFIX + severity.name().toLowerCase();
    }

    /**
     * Returns an icon for the message's {@link Severity}.
     */
    public static VaadinIcon getIcon(Severity severity) {
        return switch (severity) {
            case ERROR -> VaadinIcon.EXCLAMATION_CIRCLE;
            case WARNING -> VaadinIcon.WARNING;
            default -> VaadinIcon.INFO_CIRCLE;
        };
    }

    /**
     * Creates a {@link LinkkiText} that contains the message's text and an icon representing its
     * {@link Severity}.
     */
    public static Component createMessageComponent(Message message) {
        var severity = message.getSeverity();
        var messageLabel = new LinkkiText(message.getText(), getIcon(severity));
        messageLabel.addClassNames(getStyle(severity));
        return messageLabel;
    }

    /**
     * Creates a table displaying all messages with the given {@link BindingContext}. The messages
     * are updated upon model change in the given binding context.
     * <p>
     * If update with the binding context is not needed, consider using a {@link MessagesPanel}
     * instead.
     * <p>
     * If the component should only update after validation, consider using
     * {@link #createValidationMessagesPanel(String, BindingContext)} instead.
     */
    public static Component createMessageTable(Supplier<MessageList> messages, BindingContext bindingContext) {
        var messagesGrid = GridComponentCreator.createGrid(new MessageTablePmo(messages), bindingContext);
        messagesGrid.setWidthFull();
        messagesGrid.setSelectionMode(SelectionMode.NONE);
        return messagesGrid;
    }

    /**
     * Creates a table displaying all messages under the given title with the given
     * {@link BindingContext}.The messages are updated upon model change in the given binding
     * context.
     * <p>
     * If update with the binding context is not needed, consider using a {@link MessagesPanel}
     * instead.
     * <p>
     * If the component should only update after validation, consider using
     * {@link #createValidationMessagesPanel(String, BindingContext)} instead.
     */
    public static Component createMessageTable(String title,
            Supplier<MessageList> messages,
            BindingContext bindingContext) {
        var messageTable = createMessageTable(messages, bindingContext);
        var span = new Span(title);
        messageTable.getId().ifPresent(s -> span.setId(s + "_title"));
        var content = new VerticalLayout(span, messageTable);
        content.setPadding(false);
        return content;
    }

    /**
     * Creates a {@link MessagesPanel} that displays all validation messages in the given binding
     * context. The messages are updated after each validation.
     * 
     * @param caption caption of panel, may be empty.
     * @param bindingContext the binding context of which the validation messages should be
     *            displayed. The binding context should be managed by a
     *            {@link org.linkki.core.binding.manager.BindingManager}.
     * @return {@link MessagesPanel} that displays the validation messages after each validation
     * 
     * @since 2.10.0
     */
    public static MessagesPanel createValidationMessagesPanel(String caption, BindingContext bindingContext) {
        return (MessagesPanel)VaadinUiCreator.createComponent(new MessagesPanelPmo(caption), bindingContext);
    }

    /**
     * Binds the given {@link MessagesSplitLayout} to the given {@link BindingContext} so that the
     * messages are updated after each validation.
     *
     * @param component the component to display the messages in
     * @param bindingContext the binding context of which the validation messages should be
     *            displayed. The binding context should be managed by a
     *            {@link org.linkki.core.binding.manager.BindingManager}.
     * @since 2.10.0
     */
    public static void handleMessagesAfterValidation(MessagesSplitLayout component, BindingContext bindingContext) {
        var dummyPmo = new Object();
        bindingContext.bind(dummyPmo, new BindingDescriptor(BoundProperty.empty(), List.of(),
                ((messages, componentWrapper, propertyDispatcher) -> {
                    var comp = (MessagesSplitLayout)componentWrapper.getComponent();
                    comp.displayMessages(messages);
                    return new MessageList();
                })), new NoLabelComponentWrapper(component));
    }
}
