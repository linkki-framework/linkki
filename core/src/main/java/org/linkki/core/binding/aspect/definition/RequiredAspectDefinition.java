/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.core.binding.aspect.definition;

import java.util.function.Consumer;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyNamingConvention;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.util.Consumers;

import com.vaadin.ui.AbstractField;

/**
 * Aspect definition for {@link RequiredType}. Assumes that the given component is a
 * {@link AbstractField}.
 */
public abstract class RequiredAspectDefinition extends ModelToUiAspectDefinition<Boolean> {

    public static final String NAME = PropertyNamingConvention.REQUIRED_PROPERTY_SUFFIX;
    private EnabledAspectDefinition enabledAspectDefinition;

    public RequiredAspectDefinition(EnabledAspectDefinition enabledTypeAspectDefinition) {
        this.enabledAspectDefinition = enabledTypeAspectDefinition;
    }

    @Override
    public Aspect<Boolean> createAspect() {
        RequiredType requiredType = getRequiredType();
        switch (requiredType) {
            case DYNAMIC:
                return Aspect.newDynamic(NAME);
            case NOT_REQUIRED:
                return Aspect.ofStatic(NAME, false);
            case REQUIRED:
                return Aspect.ofStatic(NAME, true);
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
     * {@link RequiredType#NOT_REQUIRED}. In this case, {@link AbstractField#setRequired(boolean)}
     * is used.
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
        } else if (getRequiredType() == RequiredType.NOT_REQUIRED) {
            return Consumers.nopConsumer();
        } else {
            throw new IllegalArgumentException(
                    "Required type binding is not supported for component type " + component.getClass() + " ");
        }
    }

    public abstract RequiredType getRequiredType();
}
