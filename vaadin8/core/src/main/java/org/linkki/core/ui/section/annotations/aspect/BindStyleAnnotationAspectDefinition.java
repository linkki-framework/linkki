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

package org.linkki.core.ui.section.annotations.aspect;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Component;

/**
 * This aspect sets a user defined style name using {@link Component#setStyleName(String)}. This will
 * overwrite any other user defined style names but not those from Vaadin.
 * 
 * @see Component#setStyleName(String)
 */
public class BindStyleAnnotationAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = "styleNames";

    private final StyleType type;
    private final String[] value;

    public BindStyleAnnotationAspectDefinition(String[] value, StyleType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        if (type == StyleType.STATIC) {
            return () -> setStaticStyleName(propertyDispatcher, componentWrapper);
        } else {
            return () -> setDynamicStyleName(propertyDispatcher, componentWrapper);
        }
    }

    private void setStaticStyleName(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Aspect<String[]> aspect = Aspect.of(NAME, value);
        setStyleName(componentWrapper, String.join(" ", propertyDispatcher.pull(aspect)));
    }

    private void setDynamicStyleName(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper) {
        Object pulledValue = propertyDispatcher.pull(Aspect.<Object> of(NAME));
        if (pulledValue instanceof String) {
            setStyleName(componentWrapper, (String)pulledValue);
        } else {
            try {
                @SuppressWarnings("unchecked")
                String joinedStyleNames = String.join(" ", (Collection<String>)pulledValue);
                setStyleName(componentWrapper, joinedStyleNames);
            } catch (ClassCastException e) {
                throw new LinkkiBindingException(
                        "The return type of the method get" +
                                StringUtils.capitalize(propertyDispatcher.getProperty())
                                + StringUtils.capitalize(NAME)
                                + " must be either String or Collection<String>",
                        e);
            }
        }
    }

    public void setStyleName(ComponentWrapper componentWrapper, String styleName) {
        ((Component)componentWrapper.getComponent()).setStyleName(styleName);
    }

    public enum StyleType {
        STATIC,
        DYNAMIC
    }
}

