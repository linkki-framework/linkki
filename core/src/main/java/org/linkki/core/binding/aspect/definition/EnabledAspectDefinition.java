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

package org.linkki.core.binding.aspect.definition;

import java.util.function.Consumer;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyNamingConvention;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.EnabledType;

/**
 * Asepct definition for {@link EnabledType}
 */
public abstract class EnabledAspectDefinition extends ModelToUiAspectDefinition<Boolean> {

    public static final String NAME = PropertyNamingConvention.ENABLED_PROPERTY_SUFFIX;

    @Override
    public Aspect<Boolean> createAspect() {
        EnabledType enabledType = getEnabledType();
        switch (enabledType) {
            case DISABLED:
                return Aspect.ofStatic(NAME, false);
            case DYNAMIC:
                return Aspect.newDynamic(NAME);
            case ENABLED:
                return Aspect.ofStatic(NAME, true);
            default:
                throw new IllegalStateException("Unknown enabled type: " + enabledType);
        }
    }

    @Override
    public Consumer<Boolean> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return componentWrapper::setEnabled;
    }

    public abstract EnabledType getEnabledType();
}
