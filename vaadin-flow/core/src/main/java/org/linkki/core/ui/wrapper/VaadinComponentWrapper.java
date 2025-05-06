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

package org.linkki.core.ui.wrapper;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasValidation;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Base class to wrap vaadin components.
 */
public abstract class VaadinComponentWrapper implements ComponentWrapper {

    private static final String ATTRIBUTE_SEVERITY = "severity";
    /**
     * DOM property for invalid state. Should be the same property as in
     * {@link com.vaadin.flow.component.shared.HasValidationProperties#setInvalid(boolean)}.
     */
    private static final String PROPERTY_INVALID = "invalid";

    /**
     * DOM property for the error message. Should be the same property as in
     * {@link com.vaadin.flow.component.shared.HasValidationProperties#setErrorMessage(String)}.
     */
    private static final String PROPERTY_ERROR_MESSAGE = "errorMessage";

    private static final Pattern REGEX_HTML_TAGS = Pattern.compile("<[^>]*>");
    private static final Pattern REGEX_BREAK_TAG = Pattern.compile("(?i)<br */?>");

    @Serial
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
        if (component instanceof HasEnabled field) {
            field.setEnabled(enabled);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        component.setVisible(visible);
    }

    @Override
    public void setTooltip(String text) {
        component.getElement().setAttribute("title", clearHtmlAndFormat(text));
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void setValidationMessages(MessageList messagesForProperty) {
        var message = messagesForProperty.getMessageWithHighestSeverity();
        setErrorMessage(message.map(Message::getText).orElse(StringUtils.EMPTY));
        setInvalid(message.isPresent());
        setSeverity(message.map(Message::getSeverity).orElse(null));
    }

    private void setErrorMessage(String text) {
        if (component instanceof HasValidation validationField) {
            validationField.setErrorMessage(text);
        } else {
            component.getElement().setProperty(PROPERTY_ERROR_MESSAGE, text);
        }
    }

    private void setInvalid(boolean invalid) {
        if (component instanceof HasValidation validationField) {
            validationField.setInvalid(invalid);
        } else {
            component.getElement().setProperty(PROPERTY_INVALID, invalid);
        }
    }

    private void setSeverity(@CheckForNull Severity severity) {
        if (severity != null) {
            component.getElement().setAttribute(ATTRIBUTE_SEVERITY, severity.name().toLowerCase());
        } else {
            component.getElement().removeAttribute(ATTRIBUTE_SEVERITY);
        }
    }

    @Override
    public void registerBinding(Binding binding) {
        if (ComponentUtil.getData(component, Binding.class) != null) {
            throw new RuntimeException("Data was not empty, component was already bound");
        }

        ComponentUtil.setData(component, Binding.class, binding);
    }

    @Override
    public WrapperType getType() {
        return type;
    }

    /**
     * Removes all HTML tags from the given text and replaces all &#60;br&#62; with a \n. The title
     * attribute cannot handle HTML tags but with \n a line break is possible.
     *
     * @return The formatted String or an empty String if the given text is null
     */
    private String clearHtmlAndFormat(@CheckForNull String text) {
        return Optional.ofNullable(text)
                .map(html -> html.replaceAll(REGEX_BREAK_TAG.pattern(), "\n"))
                .map(html -> html.replaceAll(REGEX_HTML_TAGS.pattern(), ""))
                .orElse("");
    }

}