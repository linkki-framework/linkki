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

import java.util.ArrayList;
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

import com.vaadin.ui.Component;

/**
 * This aspect sets a user defined style name using {@link Component#setStyleName(String)}. This will
 * overwrite any other user defined style names but not those from Vaadin.
 * 
 * @see Component#setStyleName(String)
 */
public class BindStyleNamesAspectDefinition extends ModelToUiAspectDefinition<Object> {

    public static final String NAME = "styleNames";

    private final boolean dynamic;

    private final List<String> staticStyleNames;

    public BindStyleNamesAspectDefinition(String... styleNames) {
        this.staticStyleNames = Arrays.asList(styleNames);
        this.dynamic = styleNames.length == 0;
    }

    public BindStyleNamesAspectDefinition(List<String> styleNames, boolean dynamic) {
        this.staticStyleNames = new ArrayList<>(styleNames);
        this.dynamic = dynamic;
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
        String predefinedStyleNames = ((Component)componentWrapper.getComponent()).getStyleName();
        return styleNames -> {
            if (styleNames instanceof String) {
                setStyleName(componentWrapper, predefinedStyleNames, (String)styleNames);
            } else {
                @SuppressWarnings("unchecked")
                String joinedStyleNames = String.join(" ", (Collection<String>)styleNames);
                setStyleName(componentWrapper, predefinedStyleNames, joinedStyleNames);
            }
        };
    }

    public void setStyleName(ComponentWrapper componentWrapper, String predefinedStyleNames, String styleNames) {
        ((Component)componentWrapper.getComponent()).setStyleName(predefinedStyleNames + " " + styleNames);
    }

}
