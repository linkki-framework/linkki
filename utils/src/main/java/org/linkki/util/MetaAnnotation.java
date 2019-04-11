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

package org.linkki.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Helper class for handling meta-annotations ({@link Annotation Annotations} with {@link Target}
 * {@link ElementType#ANNOTATION_TYPE}).
 * 
 * @param <META> the meta-annotation
 */
public class MetaAnnotation<META extends Annotation> {

    private final Class<META> metaAnnotationClass;

    public MetaAnnotation(Class<META> metaAnnotationClass) {
        this.metaAnnotationClass = metaAnnotationClass;
    }

    /**
     * Returns {@code true} if the meta-annotation is present on any {@link Annotation} on the given
     * {@link AnnotatedElement}, otherwise {@code false};
     * 
     * @param annotatedElement an {@link AnnotatedElement} to be checked for meta-annotations on its
     *            annotations
     * @return whether the meta-annotation is present on any {@link Annotation} on the given
     *         {@link AnnotatedElement}
     */
    public boolean isPresentOnAnyAnnotationOn(AnnotatedElement annotatedElement) {
        return Arrays.stream(annotatedElement.getAnnotations()).anyMatch(this::isPresentOn);
    }

    /**
     * Returns {@code true} if the meta-annotation is present on the given {@link Annotation}, otherwise
     * {@code false};
     * 
     * @param annotation an {@link Annotation} to be checked for the meta-annotation
     * @return whether the meta-annotation is present on the given {@link Annotation}
     */
    public boolean isPresentOn(@CheckForNull Annotation annotation) {
        return annotation != null && annotation.annotationType().isAnnotationPresent(metaAnnotationClass);
    }

    /**
     * Returns the meta-annotation if it is present on the given {@link Annotation}.
     * 
     * @param annotation an {@link Annotation}
     * @return the meta-annotation instance, if the annotation is annotated with it
     */
    public Optional<META> findOn(Annotation annotation) {
        return Optional.ofNullable(annotation.annotationType().getAnnotation(metaAnnotationClass));
    }

    /**
     * Finds all {@link Annotation Annotations} on the given {@link AnnotatedElement} that are annotated
     * with the meta-annotation.
     * <p>
     * 
     * @see #onlyOneOn(AnnotatedElement) use {@code reduce(onlyOneOn(annotatedElement))} if you expect
     *      there to be no more than one such annotation.
     * 
     * @param annotatedElement an {@link AnnotatedElement}
     * @return a {@link Stream} of the {@link Annotation Annotations} annotated with the meta-annotation
     */
    public Stream<Annotation> findAnnotatedAnnotationsOn(AnnotatedElement annotatedElement) {
        return Arrays.stream(annotatedElement.getAnnotations())
                .filter(this::isPresentOn);
    }

    /**
     * Creates a {@link BinaryOperator} usable for a {@link Stream#reduce(BinaryOperator)} operation on
     * a stream of annotations as returned by {@link #findAnnotatedAnnotationsOn(AnnotatedElement)} that
     * throws an {@link IllegalArgumentException} if there is more than one annotation in the stream.
     * 
     * @param annotatedElement the {@link AnnotatedElement} that was passed to
     *            {@link #findAnnotatedAnnotationsOn(AnnotatedElement)}; will be included in the
     *            exception message
     * @return a {@link BinaryOperator} usable for a {@link Stream#reduce(BinaryOperator)} operation on
     *         a stream of annotations as returned by
     *         {@link #findAnnotatedAnnotationsOn(AnnotatedElement)}
     */
    public BinaryOperator<Annotation> onlyOneOn(AnnotatedElement annotatedElement) {
        return (a1, a2) -> {
            throw new IllegalArgumentException(annotatedElement + " has multiple annotations with a @"
                    + metaAnnotationClass.getSimpleName() + " annotation");
        };
    }

    /**
     * Creates a {@link Supplier} for an {@link IllegalArgumentException} that names the given
     * {@link Annotation} on the {@link AnnotatedElement} as not having the meta-annotation and suggests
     * using the {@code checkerMethod} to safeguard against this exception.
     * 
     * @param annotation an {@link Annotation} on the {@link AnnotatedElement}
     * @param annotatedElement an {@link AnnotatedElement}
     * @param checkerMethod description of a method to be called before the method that throws this
     *            exception to safeguard against it
     * @return a {@link Supplier} for an {@link IllegalArgumentException}
     */
    public Supplier<? extends IllegalArgumentException> missingAnnotation(Annotation annotation,
            AnnotatedElement annotatedElement,
            String checkerMethod) {
        return () -> new IllegalArgumentException(
                "@" + annotation.annotationType().getSimpleName() + " on " + annotatedElement
                        + " is not annotated with @" + metaAnnotationClass.getSimpleName() + ". You can use "
                        + checkerMethod + " to check this beforehand.");
    }

}