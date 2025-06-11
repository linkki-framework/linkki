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

package org.linkki.core.ui.aspects;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.types.PlaceholderType;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasPlaceholder;
import com.vaadin.flow.component.grid.Grid;

/**
 * An aspect definition to set a placeholder for a {@link Component} that displays a message when
 * there is no content to show. The text should give a brief hint to the user that provides context
 * for the empty state, or what kind of data should be entered into the control.
 */
public class PlaceholderAspectDefinition extends ModelToUiAspectDefinition<String> {
    public static final String NAME = "placeholder";
    public static final String HAS_PLACEHOLDER = "has-placeholder";

    private final String value;
    private final PlaceholderType type;

    public PlaceholderAspectDefinition(String value, PlaceholderType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public Aspect<String> createAspect() {
        switch (type) {
            case DYNAMIC:
                return Aspect.of(NAME);
            case AUTO:
                if (StringUtils.isEmpty(value)) {
                    return Aspect.of(NAME);
                } else {
                    return Aspect.of(NAME, value);
                }
            case STATIC:
                return Aspect.of(NAME, value);
            default:
                throw new IllegalStateException("Unknown " + NAME + " type: " + type);
        }
    }

    /**
     * @implNote For elements that are not a {@link Grid} and which are not implementing
     *           {@link HasPlaceholder}, this method sets the HTML property "placeholder". This
     *           should have no effect on components that do not support a placeholder.
     */
    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        Component component = (Component)componentWrapper.getComponent();
        return placeholder -> {
            if (component instanceof Grid<?>) {
                component.getElement().getStyle().set("--" + NAME, "'" + placeholder + "'");
                // Needs to be set as attribute as properties cannot be used in css selectors
                component.getElement().setAttribute(HAS_PLACEHOLDER, placeholder != null);
            } else if (component instanceof HasPlaceholder hasPlaceholder) {
                hasPlaceholder.setPlaceholder(placeholder);
            } else {
                component.getElement().setProperty(NAME, placeholder == null ? "" : placeholder);
            }
        };
    }

}
