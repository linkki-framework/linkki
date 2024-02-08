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

package org.linkki.core.defaults.ui.aspects;

import static org.linkki.core.defaults.ui.aspects.UiUpdateUtil.handleUiUpdateException;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.util.handler.Handler;

/**
 * Aspect definition for {@link VisibleType}.
 */
public class VisibleAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = "visible";

    private final VisibleType visibleType;

    public VisibleAspectDefinition(VisibleType visibleType) {
        this.visibleType = visibleType;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        var setter = createComponentValueSetter(componentWrapper);

        return () -> {
            var aspect = createAspect(() -> propertyDispatcher.pull(Aspect.of(VALUE_ASPECT_NAME)));
            try {
                var aspectValue = propertyDispatcher.pull(aspect);
                if (aspectValue != null) {
                    setter.accept(aspectValue);
                } else {
                    handleNullValue(setter, componentWrapper);
                }
                // CSOFF: IllegalCatch
            } catch (RuntimeException e) {
                handleUiUpdateException(e, propertyDispatcher, aspect);
                // CSON: IllegalCatch
            }
        };
    }

    public Consumer<Boolean> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return componentWrapper::setVisible;
    }

    public Aspect<Boolean> createAspect(Supplier<?> componentValue) {
        return switch (visibleType) {
            case DYNAMIC -> Aspect.of(NAME);
            case INVISIBLE -> Aspect.of(NAME, false);
            case VISIBLE -> Aspect.of(NAME, true);
            case INVISIBLE_IF_EMPTY -> Aspect.of(NAME, isNotEmpty(componentValue.get()));
        };
    }

    private boolean isNotEmpty(Object componentValue) {
        if (componentValue == null) {
            return false;
        }

        if (componentValue instanceof String stringValue) {
            return StringUtils.isNotEmpty(stringValue);
        }

        return true;
    }

    public void handleNullValue(Consumer<Boolean> componentValueSetter, ComponentWrapper componentWrapper) {
        // this case can only occur if the valueGetter is implemented in the model instead of the PMO
        componentValueSetter.accept(false);
    }
}
