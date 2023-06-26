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

package org.linkki.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
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
    private final boolean repeatable;

    private MetaAnnotation(Class<META> metaAnnotationClass) {
        this.metaAnnotationClass = metaAnnotationClass;
        repeatable = metaAnnotationClass.isAnnotationPresent(Repeatable.class);
    }

    /**
     * Creates a new {@link MetaAnnotation MetaAnnotation&lt;META&gt;} from the given {@link Annotation}
     * class.
     * 
     * @param <META> the meta-annotation class
     * @param metaAnnotationClass the meta-annotation class
     * @return a {@link MetaAnnotation} for the {@code metaAnnotationClass}
     * @throws IllegalArgumentException if the given {@link Annotation} class is not a meta-annotation
     */
    public static <META extends Annotation> MetaAnnotation<META> of(Class<META> metaAnnotationClass) {
        Target target = metaAnnotationClass.getAnnotation(Target.class);
        if (target == null) {
            throw new IllegalArgumentException(
                    metaAnnotationClass.getSimpleName() + " has no @" + Target.class.getSimpleName() + " annotation");
        }
        if (Arrays.stream(target.value()).noneMatch(t -> t == ElementType.ANNOTATION_TYPE || t == ElementType.TYPE)) {
            throw new IllegalArgumentException(
                    metaAnnotationClass.getSimpleName() + " is no meta-annotation. @" + Target.class.getSimpleName()
                            + " is " + Arrays.toString(target.value()) + " but should be "
                            + ElementType.ANNOTATION_TYPE);
        }
        return new MetaAnnotation<>(metaAnnotationClass);
    }

    /**
     * Returns {@code true} if the meta-annotation is {@link Repeatable @Repeatable}.
     * 
     * @return whether the meta-annotation is {@link Repeatable @Repeatable}
     */
    public boolean isRepeatable() {
        return repeatable;
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
        if (annotation != null) {
            return repeatable
                    ? annotation.annotationType().getAnnotationsByType(metaAnnotationClass).length > 0
                    : annotation.annotationType().isAnnotationPresent(metaAnnotationClass);
        } else {
            return false;
        }
    }

    /**
     * Returns the meta-annotation if it is present on the given {@link Annotation}.
     * <p>
     * If this meta-annotation {@link #isRepeatable() is repeatable}, this method returns an
     * {@link Optional} with the single meta-annotation if it was not repeated but throws an
     * {@link IllegalArgumentException} if it was.
     * 
     * @param annotation an {@link Annotation}
     * @return the meta-annotation instance, if the annotation is annotated with it
     * @throws IllegalArgumentException if the meta-annotation is repeatable and present multiple times.
     * @see #findAllOn(Annotation) findAllOn(Annotation) for repeatable annotations
     */
    public Optional<META> findOn(Annotation annotation) {
        return repeatable ? findAllOn(annotation).reduce((a1, a2) -> {
            throw new IllegalArgumentException(annotation.annotationType() + " has multiple @"
                    + metaAnnotationClass.getSimpleName() + " annotations. Please use "
                    + MetaAnnotation.class.getSimpleName() + "#findAllOn(Annotation) for repeatable annotations.");
        })
                : Optional.ofNullable(annotation.annotationType().getAnnotation(metaAnnotationClass));
    }

    /**
     * Returns all meta-annotations present on the given {@link Annotation}.
     * 
     * @param annotation an {@link Annotation}
     * @return the meta-annotation instance, if the annotation is annotated with it
     */
    public Stream<META> findAllOn(Annotation annotation) {
        return Arrays.stream(annotation.annotationType().getAnnotationsByType(metaAnnotationClass));
    }

    /**
     * Finds all {@link Annotation Annotations} on the given {@link AnnotatedElement} that are annotated
     * with the meta-annotation.
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
    public <A extends Annotation> BinaryOperator<A> onlyOneOn(AnnotatedElement annotatedElement) {
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