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

package org.linkki.core.ui.table.aspect;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.ModelToUiAspectDefinition;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.components.WrapperType;
import org.linkki.core.ui.table.TableComponentWrapper;

/**
 * Defines an {@link Aspect} bound to a {@link TableComponentWrapper}.
 * 
 * @param <ROW> the row PMO class of the bound table
 * @param <V> the type of the bound values
 */
public abstract class TableAspectDefinition<@NonNull ROW, V> extends ModelToUiAspectDefinition<V> {

    private final String name;
    private final BiConsumer<TableComponentWrapper<ROW>, V> valueSetter;

    /**
     * Creates a new {@link TableAspectDefinitions} with the given name using the {@code valueSetter} to
     * update the value.
     */
    public TableAspectDefinition(String name, BiConsumer<TableComponentWrapper<ROW>, V> valueSetter) {
        super();
        this.name = name;
        this.valueSetter = valueSetter;
    }

    @Override
    public Aspect<V> createAspect() {
        return Aspect.of(name);
    }

    @Override
    public Consumer<V> createComponentValueSetter(ComponentWrapper componentWrapper) {
        @SuppressWarnings("unchecked")
        TableComponentWrapper<ROW> tableComponentWrapper = (TableComponentWrapper<ROW>)componentWrapper;
        return v -> valueSetter.accept(tableComponentWrapper, v);
    }

    @Override
    public boolean supports(WrapperType type) {
        return TableComponentWrapper.TABLE_TYPE.isAssignableFrom(type);
    }

}
