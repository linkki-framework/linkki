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

package org.linkki.core.binding.property;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.property.LinkkiBoundProperty.Creator;

/**
 * Reads the annotation {@link LinkkiBoundProperty @LinkkiBoundProperty}.
 */
public final class BoundPropertyAnnotationReader {

    private static final Predicate<? super Annotation> HAS_LINKKI_BOUND_PROPERTY = a -> a.annotationType()
            .isAnnotationPresent(LinkkiBoundProperty.class);

    private BoundPropertyAnnotationReader() {
        // do not instantiate
    }

    /**
     * Returns <code>true</code> if the annotated element (mostly a field or method) has exactly one
     * an annotation that describes a {@link BoundProperty}. This is done via the meta annotation
     * {@link LinkkiBoundProperty @LinkkiBoundProperty}. Having multiple annotations which describe
     * a {@link BoundProperty} is not valid and therefore an {@link IllegalArgumentException} is
     * thrown.
     * 
     * @param annotatedElement the annotated element which might have an annotation that is
     *            annotated with {@link LinkkiBoundProperty @LinkkiBoundProperty}
     * @return <code>true</code> if there is a {@link LinkkiBoundProperty @LinkkiBoundProperty}
     *         annotation, <code>false</code> if not
     * 
     * @throws IllegalArgumentException if there are multiple annotations which are annotated with
     *             {@link LinkkiBoundProperty @LinkkiBoundProperty}
     */
    public static boolean isBoundPropertyPresent(AnnotatedElement annotatedElement) {
        return getAnnotationWithBoundPropertyDefinition(annotatedElement).isPresent();
    }

    /**
     * Returns the {@link BoundProperty} which is instantiated using the {@link Creator} from the
     * {@link LinkkiBoundProperty @LinkkiBoundProperty} annotation found at any annotation of the
     * {@code annotatedElement}. Before calling this method it is useful to check whether there is
     * such an annotation using {@link #isBoundPropertyPresent(AnnotatedElement)}.
     * 
     * @param annotatedElement the element which describes the {@link BoundProperty}
     * @return the {@link BoundProperty} described by the annotated element
     * @throws IllegalArgumentException if there is either no or more than one annotation annotated
     *             with {@link LinkkiBoundProperty @LinkkiBoundProperty}
     */
    public static BoundProperty getBoundProperty(AnnotatedElement annotatedElement) {
        return getAnnotationWithBoundPropertyDefinition(annotatedElement)
                .map(a -> createBoundProperty(annotatedElement, a))
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s has no annotation that defines a %s", annotatedElement,
                                      LinkkiBoundProperty.Creator.class.getName())));
    }

    private static Optional<Annotation> getAnnotationWithBoundPropertyDefinition(AnnotatedElement annotatedElement) {
        return Arrays.stream(annotatedElement.getAnnotations())
                .filter(HAS_LINKKI_BOUND_PROPERTY)
                .reduce((a, b) -> {
                    throw new IllegalArgumentException(
                            String.format("%s has more than one annotation that defines a %s", annotatedElement,
                                          LinkkiBoundProperty.Creator.class.getName()));
                });
    }

    private static BoundProperty createBoundProperty(AnnotatedElement annotatedElement, Annotation a) {
        @SuppressWarnings("null")
        @NonNull
        LinkkiBoundProperty boundPropertyAnnotation = a.annotationType().getAnnotation(LinkkiBoundProperty.class);
        try {
            LinkkiBoundProperty.Creator boundPropertyCreator = boundPropertyAnnotation.value().newInstance();
            return boundPropertyCreator.createBoundProperty(a, annotatedElement);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(
                    String.format("Cannot instantiate %s for %s",
                                  LinkkiBoundProperty.Creator.class.getName(), annotatedElement),
                    e);
        }
    }


}
