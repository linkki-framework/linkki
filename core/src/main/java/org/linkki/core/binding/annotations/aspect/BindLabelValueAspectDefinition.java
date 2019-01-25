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
package org.linkki.core.binding.annotations.aspect;

import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.aspect.LabelValueAspectDefinition;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Label;

/**
 * Value aspect binding that is only used if the component is an instance of {@link Label}. The
 * {@link Bind} annotation could be used with different kind of fields, hence we do not want to throw an
 * exception if this aspect is not applicable.
 */
class BindLabelValueAspectDefinition extends LabelValueAspectDefinition {

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelUpdated) {
        if (isApplicableFor(componentWrapper)) {
            super.initModelUpdate(propertyDispatcher, componentWrapper, modelUpdated);
        }
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        if (isApplicableFor(componentWrapper)) {
            return super.createUiUpdater(propertyDispatcher, componentWrapper);
        } else {
            return Handler.NOP_HANDLER;
        }
    }

    public boolean isApplicableFor(ComponentWrapper componentWrapper) {
        return componentWrapper.getComponent() instanceof Label;
    }
}