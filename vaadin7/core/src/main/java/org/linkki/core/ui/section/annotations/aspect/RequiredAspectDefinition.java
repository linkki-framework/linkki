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

import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.util.Consumers;

import com.vaadin.ui.AbstractField;

/**
 * Aspect definition for {@link RequiredType}. Assumes that the given component is a
 * {@link AbstractField}.
 */
public class RequiredAspectDefinition extends ModelToUiAspectDefinition<Boolean> {

    public static final String NAME = "required";
    private EnabledAspectDefinition enabledAspectDefinition;
    private final RequiredType requiredType;

    public RequiredAspectDefinition(RequiredType requiredType, EnabledAspectDefinition enabledTypeAspectDefinition) {
        this.requiredType = requiredType;
        this.enabledAspectDefinition = enabledTypeAspectDefinition;
    }

    @Override
    public Aspect<Boolean> createAspect() {
        switch (requiredType) {
            case DYNAMIC:
                return Aspect.of(NAME);
            case NOT_REQUIRED:
                return Aspect.of(NAME, false);
            case REQUIRED:
                return Aspect.of(NAME, true);
            case REQUIRED_IF_ENABLED:
                return enabledAspectDefinition.createAspect();
            default:
                throw new IllegalStateException("Unknown required type: " + requiredType);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method only supports {@link AbstractField} if the {@link RequiredType} is not
     * {@link RequiredType#NOT_REQUIRED}. In this case, {@link AbstractField#setRequired(boolean)} is
     * used.
     * 
     * @throws IllegalArgumentException if the {@link RequiredType} is not
     *             {@link RequiredType#NOT_REQUIRED} and the component wrapped by given
     *             {@link ComponentWrapper} is not an {@link AbstractField}
     */
    @Override
    public Consumer<Boolean> createComponentValueSetter(ComponentWrapper componentWrapper) {
        Object component = componentWrapper.getComponent();
        if (component instanceof AbstractField) {
            AbstractField<?> field = (AbstractField<?>)componentWrapper.getComponent();
            return field::setRequired;
        } else if (requiredType == RequiredType.NOT_REQUIRED) {
            return Consumers.nopConsumer();
        } else {
            throw new IllegalArgumentException(
                    "Required type binding is not supported for a component of type " + component.getClass() + " ");
        }
    }

}
