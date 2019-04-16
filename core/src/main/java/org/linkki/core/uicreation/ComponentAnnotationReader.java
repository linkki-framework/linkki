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
import java.util.Optional;

import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.util.Classes;
import org.linkki.util.MetaAnnotation;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Reads the annotation {@link LinkkiComponent @LinkkiComponent} to create a
 * {@link LinkkiComponentDefinition}.
 */
public final class ComponentAnnotationReader {

    private static final MetaAnnotation<LinkkiComponent> LINKKI_COMPONENT_ANNOTATION = MetaAnnotation
            .of(LinkkiComponent.class);

    private ComponentAnnotationReader() {
        // do not instantiate
    }

    public static boolean isComponentDefinition(@CheckForNull Annotation annotation) {
        return LINKKI_COMPONENT_ANNOTATION.isPresentOn(annotation);
    }

    /**
     * Returns the component which is instantiated using the {@link LinkkiComponentDefinition} that is
     * defined in the {@link LinkkiComponent @LinkkiComponent} annotation that must be present in the
     * given annotation.
     * 
     * @param annotation annotation that defines a {@link LinkkiComponentDefinition}
     * @return the component definition
     * @throws IllegalArgumentException if the definition could not be created
     */
    public static <A extends Annotation> LinkkiComponentDefinition getComponentDefinition(
            A annotation,
            AnnotatedElement annotatedElement) {
        LinkkiComponent linkkiComponent = LINKKI_COMPONENT_ANNOTATION.findOn(annotation)
                .orElseThrow(LINKKI_COMPONENT_ANNOTATION
                        .missingAnnotation(annotation, annotatedElement, ComponentAnnotationReader.class.getSimpleName()
                                + "#isComponentDefinition(Annotation)"));
        @SuppressWarnings("unchecked")
        Class<ComponentDefinitionCreator<A>> creatorClass = (Class<ComponentDefinitionCreator<A>>)linkkiComponent
                .value();
        return Classes.instantiate(creatorClass).create(annotation, annotatedElement);
    }

    /**
     * Returns the component which is instantiated using the {@link LinkkiComponentDefinition} that is
     * defined in the {@link LinkkiComponent @LinkkiComponent} annotation if one is found on an
     * annotation of the given element.
     * 
     * @param annotatedElement an element annotated with an annotation that defines a
     *            {@link LinkkiComponentDefinition}
     * @return the component definition if one is defined
     * @throws IllegalArgumentException if the definition could not be created or there are multiple
     *             annotations that could create one
     */
    public static Optional<LinkkiComponentDefinition> findComponentDefinition(AnnotatedElement annotatedElement) {
        return LINKKI_COMPONENT_ANNOTATION
                .findAnnotatedAnnotationsOn(annotatedElement)
                .reduce(LINKKI_COMPONENT_ANNOTATION.onlyOneOn(annotatedElement))
                .map(annotation -> getComponentDefinition(annotation, annotatedElement));
    }
}
