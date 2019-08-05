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

import org.apache.commons.lang3.StringUtils;
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
 * Wraps a vaadin component and uses the vaadin built-in caption instead of an extra label component
 * like the {@link LabelComponentWrapper}.
 * <p>
 * Although it is common to create this kind of {@link ComponentWrapper} for
 * {@link WrapperType#COMPONENT} the type is not fixed to {@link WrapperType#COMPONENT} and should be as
 * narrow as possible (for example {@link WrapperType#FIELD} or {@link WrapperType#LAYOUT}.
 */
public class CaptionComponentWrapper implements ComponentWrapper {

    private static final long serialVersionUID = 1L;

    private final Component component;

    private final WrapperType wrapperType;

    public CaptionComponentWrapper(Component component, WrapperType wrapperType) {
        this.component = component;
        this.wrapperType = wrapperType;
    }

    public CaptionComponentWrapper(String id, Component component, WrapperType wrapperType) {
        this(component, wrapperType);
        component.setId(id);
    }

    @Override
    public void setId(String id) {
        component.setId(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote Vaadin distinguishes between no caption (caption is {@code null}) and an empty caption.
     *           In second case, some space is reserved for a caption but no text is displayed. In
     *           annnotations we cannot set the {@code null} value hence we cannot distinguish between
     *           {@code null} and empty string. That's why this implementation treats the empty string
     *           as no caption and will set {@code null} as the vaadin caption.
     */
    @Override
    public void setLabel(String labelText) {
        component.setCaption(StringUtils.isEmpty(labelText) ? null : labelText);
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
        String tooltip = HtmlSanitizer.sanitize(text);
        if (component instanceof AbstractComponent) {
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
    public WrapperType getType() {
        return wrapperType;
    }

    @Override
    public String toString() {
        return component.getCaption() + "(" + component.getClass().getSimpleName() + ")";
    }

}
