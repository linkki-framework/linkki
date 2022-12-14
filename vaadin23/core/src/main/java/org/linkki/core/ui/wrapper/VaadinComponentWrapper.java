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


import java.util.Optional;
import java.util.regex.Pattern;

import org.linkki.core.binding.Binding;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Base class to wrap vaadin components.
 */
public abstract class VaadinComponentWrapper implements ComponentWrapper {

    private static final Pattern REGEX_HTML_TAGS = Pattern.compile("<[^>]*>");
    private static final Pattern REGEX_BREAK_TAG = Pattern.compile("(?i)<br */?>");

    private static final String SEVERITY_ATTRIBUTE_NAME = "severity";
    private static final String INVALID_ATTRIBUTE_NAME = "invalid";

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
        if (component instanceof HasEnabled) {
            HasEnabled field = (HasEnabled)component;
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
        if (messagesForProperty.isEmpty()) {
            clearValidation();
        } else {
            showValidation(messagesForProperty.getMessageWithHighestSeverity().get());
        }
    }

    private boolean isComponentReadOnly() {
        if (component instanceof HasValue) {
            HasValue<?, ?> readOnlyField = (HasValue<?, ?>)component;
            return readOnlyField.isReadOnly();
        } else {
            return false;
        }
    }

    private void showValidation(Message message) {
        String severity = message.getSeverity().name().toLowerCase();
        component.getElement().setAttribute(SEVERITY_ATTRIBUTE_NAME, severity);

        if (component instanceof HasValidation) {
            HasValidation validationField = (HasValidation)component;
            validationField.setErrorMessage(message.getText());
            validationField.setInvalid(true);
        } else {
            component.getElement().setAttribute(INVALID_ATTRIBUTE_NAME, "");
        }
    }

    private void clearValidation() {
        component.getElement().removeAttribute(SEVERITY_ATTRIBUTE_NAME);

        if (component instanceof HasValidation) {
            HasValidation validationField = (HasValidation)component;
            validationField.setErrorMessage(null);
            validationField.setInvalid(false);
        } else {
            component.getElement().removeAttribute(INVALID_ATTRIBUTE_NAME);
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