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

package org.linkki.core.ui.table;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.columnbased.ColumnBasedComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * Aspect definition to handle selection and double click on a table.
 * 
 * @implNote This definition contains two aspects: {@value #SELECTION_ASPECT_NAME} and
 *           {@value #DOUBLE_CLICK_ASPECT_NAME}. This is due to the fact that the selection aspect must
 *           be evaluated before the double click aspect.
 */
@SuppressWarnings("deprecation")
public class TableSelectionAspectDefinition implements LinkkiAspectDefinition {

    public static final String SELECTION_ASPECT_NAME = "selection";
    public static final String DOUBLE_CLICK_ASPECT_NAME = "onDoubleClick";

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelChanged) {
        Object component = componentWrapper.getComponent();
        com.vaadin.v7.ui.Table table = (com.vaadin.v7.ui.Table)component;
        table.setSelectable(true);
        table.setNullSelectionAllowed(false);
        table.addValueChangeListener(e -> {
            propertyDispatcher.push(Aspect.of(SELECTION_ASPECT_NAME, e.getProperty().getValue()));
            modelChanged.apply();
        });
        table.addItemClickListener(e -> {
            if (e.isDoubleClick()) {
                propertyDispatcher.push(Aspect.of(DOUBLE_CLICK_ASPECT_NAME));
                modelChanged.apply();
            }
        });
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Object component = componentWrapper.getComponent();
        if (component instanceof com.vaadin.v7.ui.Table) {
            com.vaadin.v7.ui.Table table = (com.vaadin.v7.ui.Table)componentWrapper.getComponent();
            return () -> table.setValue(propertyDispatcher.pull(Aspect.of(SELECTION_ASPECT_NAME)));
        } else {
            return Handler.NOP_HANDLER;
        }
    }

    @Override
    public boolean supports(WrapperType type) {
        return ColumnBasedComponentWrapper.COLUMN_BASED_TYPE.isAssignableFrom(type);
    }

}
