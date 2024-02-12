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

package org.linkki.core.uicreation.layout;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import org.linkki.util.reflection.Classes;
import org.linkki.util.reflection.MetaAnnotation;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Reads the annotation {@link LinkkiLayout} to create a {@link LinkkiLayoutDefinition}.
 */
public final class LayoutAnnotationReader {

    private static final MetaAnnotation<LinkkiLayout> LINKKI_LAYOUT_ANNOTATION = MetaAnnotation.of(LinkkiLayout.class);

    private LayoutAnnotationReader() {
        // do not instantiate
    }

    /**
     * Checks whether the given {@link Annotation} is annotated with
     * {@link LinkkiLayout @LinkkiLayout}.
     */
    public static boolean isLayoutDefinition(@CheckForNull Annotation annotation) {
        return LINKKI_LAYOUT_ANNOTATION.isPresentOn(annotation);
    }

    /**
     * Returns the {@link LinkkiLayoutDefinition} that is defined in the
     * {@link LinkkiLayout @LinkkiLayout} annotation if one is found on an annotation of the given
     * element.
     * 
     * @param annotatedElement an element annotated with an annotation that defines a
     *            {@link LinkkiLayoutDefinition}
     * @return the layout definition described by the {@link LinkkiLayout @LinkkiLayout} annotation
     * @throws IllegalArgumentException if the definition could not be created or there are multiple
     *             annotations that could create one
     */
    public static Optional<LinkkiLayoutDefinition> findLayoutDefinition(AnnotatedElement annotatedElement) {
        return LINKKI_LAYOUT_ANNOTATION
                .findAnnotatedAnnotationsOn(annotatedElement)
                .reduce(LINKKI_LAYOUT_ANNOTATION.onlyOneOn(annotatedElement))
                .map(annotation -> getLayoutDefinition(annotation, annotatedElement));
    }

    private static <A extends Annotation> LinkkiLayoutDefinition getLayoutDefinition(
            A annotation,
            AnnotatedElement annotatedElement) {
        LinkkiLayout linkkiLayout = LINKKI_LAYOUT_ANNOTATION.findOn(annotation)
                .orElseThrow(LINKKI_LAYOUT_ANNOTATION
                        .missingAnnotation(annotation, annotatedElement, LayoutAnnotationReader.class.getSimpleName()
                                + "#isLayoutDefinition(Annotation)"));
        @SuppressWarnings("unchecked")
        Class<LayoutDefinitionCreator<A>> creatorClass = (Class<LayoutDefinitionCreator<A>>)linkkiLayout
                .value();
        return Classes.instantiate(creatorClass).create(annotation, annotatedElement);
    }
}
