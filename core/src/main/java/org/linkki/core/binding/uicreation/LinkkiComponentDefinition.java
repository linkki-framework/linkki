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

package org.linkki.core.binding.uicreation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Defines the creation of a component based on an annotation.
 */
public interface LinkkiComponentDefinition<A extends Annotation> {

    /**
     * Creates a new component based on the given annotation.
     */
    Object createComponent(A annotation, AnnotatedElement annotatedElement);

    /**
     * Defines the position of the created component in the parent layout.
     * <p>
     * When adding multiple components to a parent, the positions of all components are compared to
     * determine the order in which they should be added. Note that the final positioning of the
     * component depends on the parent layout.
     * 
     * @return a number indicating the position of the component in relation to others
     */
    int getPosition(A annotation);

}
