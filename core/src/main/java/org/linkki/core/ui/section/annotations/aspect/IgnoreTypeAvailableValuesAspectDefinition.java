/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.core.ui.section.annotations.aspect;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.AbstractSelect;

/**
 * Defines aspects that update the set of available value of an {@link AbstractSelect}.
 */
public abstract class IgnoreTypeAvailableValuesAspectDefinition extends AvailableValuesAspectDefinition {

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        if (componentWrapper.getComponent() instanceof AbstractSelect) {
            Consumer<Collection<?>> setter = createComponentValueSetter(componentWrapper);
            Aspect<List<?>> aspect = createAspect(propertyDispatcher.getProperty(),
                                                  propertyDispatcher.getValueClass());
            return () -> setter.accept(propertyDispatcher.pull(aspect));
        } else {
            return Handler.NOP_HANDLER;
        }
    }
}
