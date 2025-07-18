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

import java.util.Objects;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.columnbased.ColumnBasedComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.data.selection.SingleSelect;

/**
 * Aspect definition to handle selection and double-click on a table.
 *
 * @implNote This definition contains two aspects: {@value #SELECTION_ASPECT_NAME} and
 *           {@value #DOUBLE_CLICK_ASPECT_NAME}. This is due to the fact that the selection aspect
 *           must be evaluated before the double click aspect.
 */
public class GridSelectionAspectDefinition implements LinkkiAspectDefinition {

    public static final String SELECTION_ASPECT_NAME = "selection";
    public static final String DOUBLE_CLICK_ASPECT_NAME = "onDoubleClick";

    private final boolean visualOnly;
    private final Grid.SelectionMode selectionMode;

    public GridSelectionAspectDefinition() {
        this(false, SelectionMode.SINGLE);
    }

    public GridSelectionAspectDefinition(boolean visualOnly, Grid.SelectionMode selectionMode) {
        this.visualOnly = visualOnly;
        this.selectionMode = selectionMode;
    }

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelChanged) {

        if (selectionMode.equals(SelectionMode.NONE)) {
            return;
        }

        var grid = (Grid<?>)componentWrapper.getComponent();
        grid.setSelectionMode(selectionMode);

        if (!visualOnly) {
            handleGridSelection(grid, propertyDispatcher, modelChanged);
        }
    }

    private void handleGridSelection(Grid<?> grid, PropertyDispatcher propertyDispatcher, Handler modelChanged) {
        if (selectionMode.equals(SelectionMode.MULTI)) {
            handleMultiSelection(grid, propertyDispatcher, modelChanged);
        } else if (selectionMode.equals(SelectionMode.SINGLE)) {
            handleSingleSelection(grid, propertyDispatcher, modelChanged);
        }
    }

    private void handleMultiSelection(Grid<?> grid, PropertyDispatcher propertyDispatcher, Handler modelChanged) {
        MultiSelect<?, ?> multiSelect = grid.asMultiSelect();
        multiSelect.addSelectionListener(e -> {
            if (e.isFromClient()) {
                propertyDispatcher.push(Aspect.of(SELECTION_ASPECT_NAME, e.getAllSelectedItems()));
                modelChanged.apply();
            }
        });
    }

    private void handleSingleSelection(Grid<?> grid, PropertyDispatcher propertyDispatcher, Handler modelChanged) {
        SingleSelect<?, ?> singleSelect = grid.asSingleSelect();
        singleSelect.addValueChangeListener(e -> {
            if (e.isFromClient()) {
                Object newSelection = e.getValue();
                if (newSelection != null) {
                    propertyDispatcher.push(Aspect.of(SELECTION_ASPECT_NAME, newSelection));
                }
                modelChanged.apply();
            }
        });
        grid.addItemDoubleClickListener(e -> {
            if (e.isFromClient()) {
                Object clickedItem = e.getItem();
                if (singleSelect.getOptionalValue().map(v -> !Objects.equals(v, clickedItem)).orElse(true)) {
                    propertyDispatcher.push(Aspect.of(SELECTION_ASPECT_NAME, e.getItem()));
                }
                propertyDispatcher.push(Aspect.of(DOUBLE_CLICK_ASPECT_NAME));
                modelChanged.apply();
            }
        });
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Object component = componentWrapper.getComponent();
        if (!visualOnly && component instanceof Grid<?> grid) {
            return switch (selectionMode) {
                case SINGLE -> () -> grid.asSingleSelect()
                        .setValue(propertyDispatcher.pull(Aspect.of(SELECTION_ASPECT_NAME)));
                case MULTI -> () -> grid.asMultiSelect()
                        .setValue(propertyDispatcher.pull(Aspect.of(SELECTION_ASPECT_NAME)));
                case NONE -> Handler.NOP_HANDLER;
            };
        } else {
            return Handler.NOP_HANDLER;
        }
    }

    @Override
    public boolean supports(WrapperType type) {
        return ColumnBasedComponentWrapper.COLUMN_BASED_TYPE.isAssignableFrom(type);
    }

    boolean isVisualOnly() {
        return visualOnly;
    }
}
