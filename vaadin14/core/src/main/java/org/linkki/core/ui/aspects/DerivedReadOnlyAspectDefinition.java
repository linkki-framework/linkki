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

package org.linkki.core.ui.aspects;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.data.HasValue;

/**
 * Derives the read-only-state from the absence of a setter for the bound property.
 */
public class DerivedReadOnlyAspectDefinition implements LinkkiAspectDefinition {

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper) {
        HasValue<?> field = (HasValue<?>)componentWrapper.getComponent();
        return () -> field.setReadOnly(!propertyDispatcher
                .isPushable(Aspect.of(VALUE_ASPECT_NAME, field.getValue())));
    }

}
