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

package org.linkki.core.ui.section.annotations.aspect;

import java.util.function.Consumer;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.ModelToUiAspectDefinition;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.VisibleType;

/**
 * Aspect definition for {@link VisibleType}.
 * <p>
 * To access the annotation in which the {@link VisibleType} is defined use
 * {@link #initialize(java.lang.annotation.Annotation)}.
 */
public abstract class VisibleAspectDefinition extends ModelToUiAspectDefinition<Boolean> {

    public static final String NAME = "visible";

    @Override
    public Aspect<Boolean> createAspect() {
        VisibleType visibleType = getVisibleType();
        switch (visibleType) {
            case DYNAMIC:
                return Aspect.of(NAME);
            case INVISIBLE:
                return Aspect.of(NAME, false);
            case VISIBLE:
                return Aspect.of(NAME, true);
            default:
                throw new IllegalStateException("Unknown visible type: " + visibleType);
        }
    }

    @Override
    public Consumer<Boolean> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return componentWrapper::setVisible;
    }

    public abstract VisibleType getVisibleType();
}
