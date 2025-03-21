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

package de.faktorzehn.commons.linkki.ui.menu;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * A static aspect that creates sub menu items defined in the PMO.
 *
 * @deprecated Moved to linkki-core-vaadin-flow. Use
 *             {@link org.linkki.core.ui.element.annotation.UIMenuList.MenuItemsAspectDefinition}
 *             instead
 */
@Deprecated(since = "2.8.0")
public class MenuItemsAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = StringUtils.EMPTY;

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        return () -> {
            var itemDefinitions = propertyDispatcher.pull(Aspect.<List<MenuItemDefinition>> of(NAME));
            // for backward compatibility, null is treated as empty list
            if (itemDefinitions != null) {
                var menuBar = (SingleItemMenuBar)componentWrapper.getComponent();
                menuBar.updateSubMenuItems(itemDefinitions);
            }
        };
    }

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelChanged) {
        var itemDefinitions = propertyDispatcher.pull(Aspect.<List<MenuItemDefinition>> of(NAME));
        // for backward compatibility, null is treated as empty list
        if (itemDefinitions != null) {
            var menuBar = (SingleItemMenuBar)componentWrapper.getComponent();
            menuBar.createSubMenuItems(itemDefinitions, modelChanged);
        }
    }

}