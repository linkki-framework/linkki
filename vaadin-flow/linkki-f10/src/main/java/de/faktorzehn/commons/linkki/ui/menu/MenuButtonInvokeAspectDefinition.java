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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.contextmenu.MenuItem;

public class MenuButtonInvokeAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = StringUtils.EMPTY;

    public MenuButtonInvokeAspectDefinition() {
        //
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        return Handler.NOP_HANDLER;
    }

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelUpdated) {
        SingleItemMenuBar menuBar = (SingleItemMenuBar)componentWrapper.getComponent();
        MenuItem menuItem = menuBar.getItems().get(0);
        menuItem.addClickListener(selectedItem -> {
            propertyDispatcher.push(Aspect.of(NAME));
            modelUpdated.apply();
        });
    }
}