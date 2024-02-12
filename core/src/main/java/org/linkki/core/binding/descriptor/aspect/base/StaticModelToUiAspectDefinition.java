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

package org.linkki.core.binding.descriptor.aspect.base;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * A convenience implementation for {@link LinkkiAspectDefinition LinkkiAspectDefinitions} that only
 * updates the UI once when it is created.
 * <p>
 * This is a special case of {@link ModelToUiAspectDefinition} where the {@link ComponentWrapper}
 * should <em>not</em> be updated with every update event.
 */
public abstract class StaticModelToUiAspectDefinition<VALUE_TYPE> extends ModelToUiAspectDefinition<VALUE_TYPE> {

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        super.createUiUpdater(propertyDispatcher, componentWrapper).apply();
        return Handler.NOP_HANDLER;
    }

}
