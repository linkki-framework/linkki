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

package org.linkki.core.ui.aspects;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.button.Button;

/**
 * Dynamically invoke a method when the button is clicked.
 */
public class ButtonInvokeAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = StringUtils.EMPTY;

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        return Handler.NOP_HANDLER;
    }

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelUpdated) {
        Button button = (Button)componentWrapper.getComponent();
        // Prevents additional calls during binding update. Buttons are enabled again with binding
        // update.
        button.setDisableOnClick(true);
        button.addClickListener(e -> {
            propertyDispatcher.push(Aspect.of(NAME));
            modelUpdated.apply();
        });
    }

}
