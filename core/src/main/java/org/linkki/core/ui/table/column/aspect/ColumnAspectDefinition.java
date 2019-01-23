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

package org.linkki.core.ui.table.column.aspect;

import java.lang.annotation.Annotation;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.aspect.definition.ModelToUiAspectDefinition;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.components.WrapperType;
import org.linkki.core.ui.section.annotations.UITableColumn;
import org.linkki.core.ui.table.column.TableColumnWrapper;

/**
 * A {@link LinkkiAspectDefinition} that sets a value from the {@link UITableColumn @UITableColumn}
 * annotation on a {@link TableColumnWrapper}.
 */
public abstract class ColumnAspectDefinition<VALUE_TYPE> extends ModelToUiAspectDefinition<VALUE_TYPE> {

    private String name;
    private Function<UITableColumn, VALUE_TYPE> valueFromAnnotation;
    private BiConsumer<TableColumnWrapper, VALUE_TYPE> valueSetter;

    private VALUE_TYPE value;

    public ColumnAspectDefinition(String name, VALUE_TYPE defaultValue,
            Function<UITableColumn, VALUE_TYPE> valueFromAnnotation,
            BiConsumer<TableColumnWrapper, VALUE_TYPE> valueSetter) {
        this.name = name;
        value = defaultValue;
        this.valueFromAnnotation = valueFromAnnotation;
        this.valueSetter = valueSetter;
    }

    @Override
    public Aspect<VALUE_TYPE> createAspect() {
        return Aspect.of(name, value);
    }

    @Override
    public void initialize(Annotation uiTableColumn) {
        setValue(valueFromAnnotation.apply(((UITableColumn)uiTableColumn)));
    }

    public void setValue(VALUE_TYPE value) {
        this.value = value;
    }

    @Override
    public Consumer<VALUE_TYPE> createComponentValueSetter(ComponentWrapper componentWrapper) {
        TableColumnWrapper columnWrapper = (TableColumnWrapper)componentWrapper;
        return v -> valueSetter.accept(columnWrapper, v);
    }

    @Override
    public boolean supports(WrapperType type) {
        return TableColumnWrapper.COLUMN_TYPE.isAssignableFrom(type);
    }
}

