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

package org.linkki.core.ui.aspects;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;

import com.vaadin.flow.component.HasStyle;

/**
 * This aspect sets a user defined style name using {@link HasStyle#setClassName(String)}. This will
 * overwrite any other user defined style names but not those from Vaadin.
 * 
 * @see HasStyle#setClassName(String)
 */
public class BindStyleNamesAspectDefinition extends ModelToUiAspectDefinition<Object> {

    public static final String NAME = "styleNames";

    private final boolean dynamic;

    private final List<String> staticStyleNames;

    public BindStyleNamesAspectDefinition(String... styleNames) {
        this.staticStyleNames = Arrays.asList(styleNames);
        this.dynamic = styleNames.length == 0;
    }

    @Override
    public Aspect<Object> createAspect() {
        return dynamic ? Aspect.of(NAME) : Aspect.of(NAME, staticStyleNames);
    }

    @Override
    protected void handleUiUpdateException(RuntimeException e,
            PropertyDispatcher propertyDispatcher,
            Aspect<Object> aspect) {
        if (e instanceof ClassCastException) {
            throw new LinkkiBindingException(
                    "The return type of the method get" +
                            StringUtils.capitalize(propertyDispatcher.getProperty())
                            + StringUtils.capitalize(NAME)
                            + " must be either String or Collection<String>",
                    e);
        } else {
            super.handleUiUpdateException(e, propertyDispatcher, aspect);
        }
    }

    @Override
    public Consumer<Object> createComponentValueSetter(ComponentWrapper componentWrapper) {
        Object wrappedComponent = componentWrapper.getComponent();
        if (!(wrappedComponent instanceof HasStyle)) {
            throw new IllegalArgumentException(
                    String.format("Could not set styles names. Component %s must implement %s interface.",
                                  wrappedComponent.getClass().getName(), HasStyle.class.getName()));
        }

        HasStyle component = (HasStyle)wrappedComponent;
        String predefinedStyleNames = component.getClassName();

        return styleNames -> {
            if (styleNames instanceof String) {
                setClassName(component, predefinedStyleNames, (String)styleNames);
            } else {
                @SuppressWarnings("unchecked")
                String joinedStyleNames = String.join(" ", (Collection<String>)styleNames);
                setClassName(component, predefinedStyleNames, joinedStyleNames);
            }
        };
    }

    private void setClassName(HasStyle component, String predefinedStyleNames, String styleNames) {
        if (predefinedStyleNames != null) {
            component.setClassName(predefinedStyleNames + " " + styleNames);
        } else {
            component.setClassName(styleNames);
        }
    }

}
