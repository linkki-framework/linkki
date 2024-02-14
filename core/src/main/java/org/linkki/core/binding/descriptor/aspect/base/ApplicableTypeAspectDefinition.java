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
import org.linkki.core.binding.wrapper.ComponentWrapper;

/**
 * Wraps a {@link LinkkiAspectDefinition} that will only be called if the {@link ComponentWrapper}
 * wraps a Component of type {@code<T>}.
 */
public class ApplicableTypeAspectDefinition extends ApplicableAspectDefinition {

    private ApplicableTypeAspectDefinition(LinkkiAspectDefinition wrappedAspectDefinition, Class<?> applicableType) {
        super(wrappedAspectDefinition, w -> applicableType.isInstance(w.getComponent()));
    }

    public static ApplicableTypeAspectDefinition ifComponentTypeIs(Class<?> applicableType,
            LinkkiAspectDefinition wrappedAspectDefinition) {
        return new ApplicableTypeAspectDefinition(wrappedAspectDefinition, applicableType);
    }
}
