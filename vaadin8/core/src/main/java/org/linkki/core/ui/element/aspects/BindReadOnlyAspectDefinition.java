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

package org.linkki.core.ui.element.aspects;

import org.apache.commons.lang3.BooleanUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.element.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.util.handler.Handler;

import com.vaadin.data.HasValue;

/**
 * Aspect definition for read-only state.
 */
public class BindReadOnlyAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = "readOnly";

    private final ReadOnlyType value;

    public BindReadOnlyAspectDefinition(ReadOnlyType value) {
        this.value = value;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        HasValue<?> field = (HasValue<?>)componentWrapper.getComponent();

        switch (value) {
            case ALWAYS:
                return () -> field.setReadOnly(true);
            case DYNAMIC:
                Aspect<Boolean> aspect = Aspect.of(NAME);
                return () -> field.setReadOnly(field.isReadOnly()
                        || BooleanUtils.toBoolean(propertyDispatcher.pull(aspect)));
            default:
                return Handler.NOP_HANDLER;
        }
    }

}
