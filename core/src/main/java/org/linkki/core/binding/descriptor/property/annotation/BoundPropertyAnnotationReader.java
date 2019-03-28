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

package org.linkki.core.binding.descriptor.property.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.linkki.core.binding.descriptor.property.BoundProperty;

import edu.umd.cs.findbugs.annotations.NonNull;

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
     * Returns <code>true</code> if the annotated element (mostly a field or method) has exactly one an
     * annotation that describes a {@link BoundProperty}. This is done via the meta annotation
     * {@link LinkkiBoundProperty @LinkkiBoundProperty}. Having multiple annotations which describe a
     * {@link BoundProperty} is not valid and therefore an {@link IllegalArgumentException} is thrown.
     * 
     * @param annotatedElement the annotated element which might have an annotation that is annotated
     *            with {@link LinkkiBoundProperty @LinkkiBoundProperty}
     * @return <code>true</code> if there is a {@link LinkkiBoundProperty @LinkkiBoundProperty}
     *         annotation, <code>false</code> if not
     * 
     * @throws IllegalArgumentException if there are multiple annotations which are annotated with
     *             {@link LinkkiBoundProperty @LinkkiBoundProperty}
     */
    public static boolean isBoundPropertyPresent(AnnotatedElement annotatedElement) {
        return !getAnnotationsWithBoundPropertyDefinition(annotatedElement).isEmpty();
    }

    /**
     * Returns the {@link BoundProperty} which is instantiated using the {@link BoundPropertyCreator}
     * from the {@link LinkkiBoundProperty @LinkkiBoundProperty} annotation found at any annotation of
     * the {@code annotatedElement}. Before calling this method it is useful to check whether there is
     * such an annotation using {@link #isBoundPropertyPresent(AnnotatedElement)}.
     * 
     * @param annotatedElement the element which describes the {@link BoundProperty}
     * @return the {@link BoundProperty} described by the annotated element
     * @throws IllegalArgumentException if either
     *             <ul>
     *             <li>there is either no annotation that is annotated with
     *             {@link LinkkiBoundProperty}</li>
     *             <li>there are multiple annotations that are annotated with
     *             {@link LinkkiBoundProperty}, but deliver different {@link BoundProperty bound
     *             properties}</li>
     *             <li>if the {@link BoundPropertyCreator} can't be created.</li>
     *             </ul>
     */
    public static BoundProperty getBoundProperty(AnnotatedElement annotatedElement) {
        return findBoundProperty(annotatedElement)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s has no annotation that defines a %s", annotatedElement,
                                      BoundPropertyCreator.class.getName())));
    }

    /**
     * Returns the {@link BoundProperty} which is instantiated using the {@link BoundPropertyCreator}
     * from the {@link LinkkiBoundProperty @LinkkiBoundProperty} annotation found at any annotation of
     * the {@code annotatedElement} if there is such an annotation.
     * 
     * @param annotatedElement the element which describes the {@link BoundProperty}
     * @return the {@link BoundProperty} described by the annotated element
     * @throws IllegalArgumentException if either
     *             <ul>
     *             <li>there is either no annotation that is annotated with
     *             {@link LinkkiBoundProperty}</li>
     *             <li>there are multiple annotations that are annotated with
     *             {@link LinkkiBoundProperty}, but deliver different {@link BoundProperty bound
     *             properties}</li>
     *             <li>if the {@link BoundPropertyCreator} can't be created.</li>
     *             </ul>
     */
    public static Optional<BoundProperty> findBoundProperty(AnnotatedElement annotatedElement) {
        return getAnnotationsWithBoundPropertyDefinition(annotatedElement)
                .stream()
                .map(a -> createBoundProperty(annotatedElement, a))
                .reduce((b1, b2) -> {
                    if (b1.equals(b2)) {
                        return b1;
                    } else {
                        throw new IllegalArgumentException(
                                String.format("%s has annotations that define different bound properties (%s, %s)",
                                              annotatedElement,
                                              b1.toString(),
                                              b2.toString(),
                                              BoundPropertyCreator.class.getName()));
                    }
                });
    }

    private static List<Annotation> getAnnotationsWithBoundPropertyDefinition(AnnotatedElement annotatedElement) {
        return Arrays.stream(annotatedElement.getAnnotations())
                .filter(HAS_LINKKI_BOUND_PROPERTY)
                .collect(Collectors.toList());
    }

    private static <T extends Annotation> BoundProperty createBoundProperty(AnnotatedElement annotatedElement, T a) {
        @NonNull
        LinkkiBoundProperty boundPropertyAnnotation = a.annotationType().getAnnotation(LinkkiBoundProperty.class);
        try {
            @SuppressWarnings("unchecked")
            BoundPropertyCreator<T> boundPropertyCreator = (BoundPropertyCreator<T>)boundPropertyAnnotation.value()
                    .newInstance();
            return boundPropertyCreator.createBoundProperty(a, annotatedElement);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(
                    String.format("Cannot instantiate %s for %s",
                                  BoundPropertyCreator.class.getName(), annotatedElement),
                    e);
        }
    }


}
