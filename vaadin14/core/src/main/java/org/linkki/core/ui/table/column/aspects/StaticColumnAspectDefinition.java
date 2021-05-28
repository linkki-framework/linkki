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

package org.linkki.core.ui.table.column.aspects;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.creation.table.GridColumnWrapper;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

/**
 * A {@link LinkkiAspectDefinition} that sets a value from the {@link UITableColumn @UITableColumn}
 * annotation on a {@link GridColumnWrapper}. This aspect is applied only once when initializing the
 * table. It is <em>not</em> updated with {@link BindingContext#uiUpdated()}!
 */
public abstract class StaticColumnAspectDefinition<VALUE_TYPE>
        extends StaticModelToUiAspectDefinition<VALUE_TYPE> {

    private String name;
    private BiConsumer<GridColumnWrapper, VALUE_TYPE> valueSetter;

    private VALUE_TYPE value;

    public StaticColumnAspectDefinition(String name, VALUE_TYPE defaultValue,
            BiConsumer<GridColumnWrapper, VALUE_TYPE> valueSetter) {
        this.name = name;
        this.value = defaultValue;
        this.valueSetter = valueSetter;
    }

    @Override
    public Aspect<VALUE_TYPE> createAspect() {
        return Aspect.of(name, value);
    }

    public void setValue(VALUE_TYPE value) {
        this.value = value;
    }

    @Override
    public Consumer<VALUE_TYPE> createComponentValueSetter(ComponentWrapper componentWrapper) {
        GridColumnWrapper columnWrapper = (GridColumnWrapper)componentWrapper;
        return v -> valueSetter.accept(columnWrapper, v);
    }

    @Override
    public boolean supports(WrapperType type) {
        return GridColumnWrapper.COLUMN_TYPE.isAssignableFrom(type);
    }

}

