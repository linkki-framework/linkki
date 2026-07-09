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

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.ComponentStyles;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;

/**
 * Aspect definition to set the label width of form item labels.
 * <p>
 * The width will only be set during initialization.
 * </p>
 */
public class BindFormItemLabelWidthAspectDefinition implements LinkkiAspectDefinition {
    private final String formItemWidth;

    public BindFormItemLabelWidthAspectDefinition(String formItemWidth) {
        this.formItemWidth = formItemWidth;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        ComponentStyles.setFormItemLabelWidth((Component)componentWrapper.getComponent(), formItemWidth);

        return Handler.NOP_HANDLER;
    }
}
