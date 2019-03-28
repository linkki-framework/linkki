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

package org.linkki.core.uicreation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;

/**
 * Reads the annotation {@link LinkkiComponent} to create component.
 */
public final class ComponentAnnotationReader {

    private ComponentAnnotationReader() {
        // do not instantiate
    }

    public static boolean isLinkkiComponentDefinition(Annotation annotation) {
        return annotation.annotationType().isAnnotationPresent(LinkkiComponent.class);
    }


    /**
     * Returns the component which is instantiated using the {@link LinkkiComponentDefinition} that is
     * defined in the {@link LinkkiComponent} annotation that must be present in the given annotation.
     * 
     * @param annotation annotation that defines a {@link LinkkiComponentDefinition}
     * @return the component described by the
     * @throws IllegalArgumentException if the definition could not be created
     */
    public static <ANNOTATION extends Annotation> LinkkiComponentDefinition getComponentDefinition(
            ANNOTATION annotation,
            AnnotatedElement annotatedElement) {
        try {
            @SuppressWarnings("unchecked")
            ComponentDefinitionCreator<ANNOTATION> creator = (ComponentDefinitionCreator<ANNOTATION>)getLinkkiComponentAnnotation(annotation)
                    .value()
                    .newInstance();
            return creator.create(annotation, annotatedElement);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(
                    String.format("Cannot instantiate %s",
                                  LinkkiComponentDefinition.class.getName()),
                    e);
        }
    }

    private static LinkkiComponent getLinkkiComponentAnnotation(Annotation annotation) {
        if (!annotation.annotationType().isAnnotationPresent(LinkkiComponent.class)) {
            throw new IllegalArgumentException(
                    String.format("%s has no %s annotation that defines a %s", annotation,
                                  LinkkiComponent.class.getName(),
                                  LinkkiComponentDefinition.class.getName()));
        } else {
            return annotation.annotationType().getAnnotation(LinkkiComponent.class);
        }
    }
}
