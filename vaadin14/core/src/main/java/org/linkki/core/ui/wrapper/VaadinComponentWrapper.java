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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasValidation;

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
        // TODO LIN-2054
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void setValidationMessages(MessageList messagesForProperty) {
        if (component instanceof HasValidation) {
            HasValidation field = (HasValidation)component;
            field.setErrorMessage(formatMessages(messagesForProperty));
            field.setInvalid(messagesForProperty.containsErrorMsg());
        }
    }

    private String formatMessages(MessageList messages) {
        return StreamSupport.stream(messages.spliterator(), false)
                .map(Message::getText)
                .collect(Collectors.joining("\n"));
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

}