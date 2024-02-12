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

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.dom.Element;

/**
 * {@link StaticModelToUiAspectDefinition} that sets an {@link Element}'s attribute to a value.
 * Useful when implementing {@link AspectDefinitionCreator}s which manipulate attributes of
 * elements.
 */
public class ElementAttributeAspectDefinition extends StaticModelToUiAspectDefinition<String> {

    public static final String ATTRIBUTE_ASPECT_NAME_SUFFIX = "Attribute";

    private final String attribute;
    private final String value;

    /**
     * Creates an {@link ElementAttributeAspectDefinition} that sets an {@link Element}'s attribute
     * to a value.
     * 
     * @param attribute The attribute's name
     * @param value The attributes' value
     */
    public ElementAttributeAspectDefinition(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public Aspect<String> createAspect() {
        return Aspect.of(attribute + ATTRIBUTE_ASPECT_NAME_SUFFIX, value);
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return v -> ((Component)componentWrapper.getComponent()).getElement().setAttribute(attribute, value);
    }
}