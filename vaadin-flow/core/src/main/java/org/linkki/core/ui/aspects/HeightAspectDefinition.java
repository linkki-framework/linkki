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
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;

import com.vaadin.flow.component.HasSize;

/**
 * Aspect definition to bind the height of a UI component.
 * <p>
 * The height can only be defined using a static value that is interpreted as a CSS-compatible
 * string, such as {@code "100px"}, {@code "50%"}, or {@code "10em"}.
 */
public class HeightAspectDefinition extends ModelToUiAspectDefinition<String> {
    public static final String NAME = "height";

    private final String value;

    /**
     * Creates a new {@link HeightAspectDefinition} that represents a static height.
     *
     * @param value the height value to be applied to the component
     */
    public HeightAspectDefinition(String value) {
        this.value = value;
    }

    @Override
    public Aspect<String> createAspect() {
        return Aspect.of(NAME, value);
    }

    @Override
    protected Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        var component = (HasSize)componentWrapper.getComponent();
        return component::setHeight;
    }
}
