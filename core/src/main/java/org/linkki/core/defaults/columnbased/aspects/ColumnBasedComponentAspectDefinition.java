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

package org.linkki.core.defaults.columnbased.aspects;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.columnbased.ColumnBasedComponentWrapper;

/**
 * Defines an {@link Aspect} bound to a {@link ColumnBasedComponentWrapper}.
 * 
 * @param <ROW> the row PMO class of the bound column based UI component
 * @param <VALUE> the type of the bound values
 * @param <WRAPPER> the concrete {@link ColumnBasedComponentWrapper}
 */
public abstract class ColumnBasedComponentAspectDefinition<ROW, VALUE, WRAPPER extends ColumnBasedComponentWrapper<ROW>>
        extends ModelToUiAspectDefinition<VALUE> {

    private final String name;
    private final BiConsumer<WRAPPER, VALUE> valueSetter;

    /**
     * Creates a new {@link ColumnBasedComponentAspectDefinition} with the given name using the
     * {@code valueSetter} to update the value.
     */
    public ColumnBasedComponentAspectDefinition(String name, BiConsumer<WRAPPER, VALUE> valueSetter) {
        super();
        this.name = name;
        this.valueSetter = valueSetter;
    }

    @Override
    public Aspect<VALUE> createAspect() {
        return Aspect.of(name);
    }

    @Override
    public Consumer<VALUE> createComponentValueSetter(ComponentWrapper componentWrapper) {
        @SuppressWarnings("unchecked")
        WRAPPER tableComponentWrapper = (WRAPPER)componentWrapper;
        return v -> valueSetter.accept(tableComponentWrapper, v);
    }

    @Override
    public boolean supports(WrapperType type) {
        return ColumnBasedComponentWrapper.COLUMN_BASED_TYPE.isAssignableFrom(type);
    }

}
