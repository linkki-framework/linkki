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

package org.linkki.core.binding.descriptor.aspect.annotation;

import java.lang.annotation.Annotation;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;

/**
 * Implementations of this interface describe how a {@link LinkkiAspectDefinition} is created from the
 * context of an {@link Annotation}. The implementation class is defined in
 * {@link LinkkiAspect#value()}.
 * <p>
 * 
 * @implSpec The implementation must have a default constructor which will be called while reading the
 *           annotations of a PMO class.
 */
public interface AspectDefinitionCreator<T extends Annotation> {

    /**
     * Creates the aspect definition from the annotation that was annotated with {@link LinkkiAspect}.
     * The annotation may hold information such as a static value or anything necessary for value post
     * processing.
     * <p>
     * The {@link LinkkiAspectDefinition} is instantiated for every property. That means it is valid to
     * store the given annotation in a field.
     * 
     * @param annotation the annotation that is annotated with {@link LinkkiAspect}
     * @return the new {@link LinkkiAspectDefinition} initialized with the given annotation
     */
    LinkkiAspectDefinition create(T annotation);

}