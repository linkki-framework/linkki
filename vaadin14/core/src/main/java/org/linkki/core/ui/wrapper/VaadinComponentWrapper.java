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

package org.linkki.core.ui.wrapper;


import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.linkki.core.binding.Binding;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.validation.message.SeverityErrorLevelConverter;
import org.linkki.util.HtmlSanitizer;

import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Base class to wrap vaadin components.
 */
public abstract class VaadinComponentWrapper implements ComponentWrapper {

    private static final long serialVersionUID = 1L;

    private final Component component;

    private final WrapperType type;

    public VaadinComponentWrapper(Component component, WrapperType type) {
        this.component = component;
        this.type = type;
    }

    @Override
    public void setId(String id) {
        component.setId(id);
    }

    @Override
    public void setEnabled(boolean enabled) {
        component.setEnabled(enabled);
    }

    @Override
    public void setVisible(boolean visible) {
        component.setVisible(visible);
    }

    @Override
    public void setTooltip(String text) {
        if (component instanceof AbstractComponent) {
            String tooltip = HtmlSanitizer.sanitize(text);
            ((AbstractComponent)component).setDescription(tooltip, com.vaadin.shared.ui.ContentMode.HTML);
        }
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void setValidationMessages(MessageList messagesForProperty) {
        if (component instanceof AbstractComponent) {
            AbstractComponent field = (AbstractComponent)component;
            field.setComponentError(getErrorHandler(messagesForProperty));
        }
    }

    @CheckForNull
    private UserError getErrorHandler(MessageList messages) {
        return messages.getSeverity()
                .map(SeverityErrorLevelConverter::convertToErrorLevel)
                .map(e -> new UserError(formatMessages(messages), ContentMode.PREFORMATTED, e))
                .orElse(null);
    }

    private String formatMessages(MessageList messages) {
        return StreamSupport.stream(messages.spliterator(), false)
                .map(Message::getText)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void registerBinding(Binding binding) {
        if (((AbstractComponent)component).getData() != null) {
            throw new RuntimeException("Data was not empty, component was already bound or data was used by others.");
        }
        ((AbstractComponent)component).setData(binding);
    }

    @Override
    public WrapperType getType() {
        return type;
    }

}