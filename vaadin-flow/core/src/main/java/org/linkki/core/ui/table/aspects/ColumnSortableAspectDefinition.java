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

package org.linkki.core.ui.table.aspects;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.creation.table.GridColumnWrapper;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.util.handler.Handler;
import org.linkki.util.reflection.accessor.PropertyAccessor;

/**
 * Aspect definition to allow sorting of table columns.
 * 
 * @see UITableColumn#sortable()
 */
public class ColumnSortableAspectDefinition implements LinkkiAspectDefinition {

    private final boolean sortable;

    public ColumnSortableAspectDefinition(boolean sortable) {
        this.sortable = sortable;
    }

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelChanged) {
        if (sortable) {
            initComparator(propertyDispatcher, (GridColumnWrapper)componentWrapper);
        }
    }

    private void initComparator(PropertyDispatcher propertyDispatcher, GridColumnWrapper columnWrapper) {
        Class<?> pmoClass = (Class<?>)propertyDispatcher.getBoundObject();
        if (pmoClass == null) {
            throw new IllegalStateException("Could not obtain bound object");
        }

        PropertyAccessor<?, ?> accessor = PropertyAccessor.get(pmoClass, propertyDispatcher.getProperty());
        if (!accessor.canRead()) {
            String message =
                    "Could not read %s#%s. Using a model object is not supported when using sortable in @UITableColumn.";
            throw new IllegalStateException(
                    String.format(message, pmoClass.getSimpleName(), propertyDispatcher.getProperty()));
        }

        Class<?> valueClass = accessor.getValueClass();
        if (!Comparable.class.isAssignableFrom(valueClass)) {
            String message =
                    "Cannot sort by %s#%s as %s does not implement Comparable. This is required when using sortable in @UITableColumn.";
            throw new IllegalStateException(String.format(message,
                                                          pmoClass.getSimpleName(), propertyDispatcher.getProperty(),
                                                          valueClass.getCanonicalName()));
        }

        @SuppressWarnings("unchecked")
        var comparableAccessor = (PropertyAccessor<Object, Comparable<? super Comparable<?>>>)accessor;
        columnWrapper.getComponent().setComparator(comparableAccessor::getPropertyValue);
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        return Handler.NOP_HANDLER;
    }

    @Override
    public boolean supports(WrapperType type) {
        return GridColumnWrapper.COLUMN_TYPE.isAssignableFrom(type);
    }

}
