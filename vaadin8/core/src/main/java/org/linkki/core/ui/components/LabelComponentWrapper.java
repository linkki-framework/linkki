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

package org.linkki.core.ui.components;

import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.core.message.SeverityErrorLevelConverter;
import org.linkki.util.StreamUtil;

import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Implementation of the {@link ComponentWrapper} with a Vaadin {@link Component} and a {@link Label}
 * component.
 */
public class LabelComponentWrapper implements ComponentWrapper {

    private static final long serialVersionUID = 1L;

    @Nullable
    private final Label label;
    private final Component component;

    public LabelComponentWrapper(Component component) {
        this(null, component);
    }

    public LabelComponentWrapper(@Nullable Label label, Component component) {
        this.label = label;
        this.component = component;
    }

    @Override
    public void setId(String id) {
        component.setId(id);
    }

    @Override
    public void setLabel(String labelText) {
        if (label != null) {
            label.setValue(labelText);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (label != null) {
            label.setEnabled(enabled);
        }
        component.setEnabled(enabled);
    }

    @Override
    public void setVisible(boolean visible) {
        if (label != null) {
            label.setVisible(visible);
        }
        component.setVisible(visible);
    }

    @Override
    public void setTooltip(String text) {
        if (component instanceof AbstractComponent) {
            ((AbstractComponent)component).setDescription(text);
        }
        getLabelComponent().ifPresent(l -> l.setDescription(text));
    }

    public Optional<Label> getLabelComponent() {
        return Optional.ofNullable(label);
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

    @Nullable
    private UserError getErrorHandler(MessageList messages) {
        return messages.getSeverity()
                .map(SeverityErrorLevelConverter::convertToErrorLevel)
                .map(e -> new UserError(formatMessages(messages), ContentMode.PREFORMATTED, e))
                .orElse(null);
    }

    private String formatMessages(MessageList messages) {
        return StreamUtil.stream(messages)
                .map(Message::getText)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public WrapperType getType() {
        return WrapperType.FIELD;
    }

    @Override
    public String toString() {
        return Optional.ofNullable(label).map(Label::getValue).orElse("<no label>") + "("
                + component.getClass().getSimpleName() + ")";
    }
}
