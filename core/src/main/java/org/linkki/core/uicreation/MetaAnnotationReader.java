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
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Helper class for meta-annotation readers.
 */
public final class MetaAnnotationReader {

    private MetaAnnotationReader() {
        // do not instantiate
    }

    /**
     * Checks whether the given {@link Annotation} is annotated with the given meta-annotation.
     */
    public static boolean isMetaAnnotationPresent(@CheckForNull Annotation annotation,
            Class<? extends Annotation> metaAnnotationClass) {
        return annotation != null && annotation.annotationType().isAnnotationPresent(metaAnnotationClass);
    }

    /**
     * Reads the meta-annotation from the given {@link Annotation}, extracts the
     * {@link ObjectFromAnnotationCreator} via the {@code valueGetter}, instantiates it and returns the
     * object created by a call to the creator's
     * {@link ObjectFromAnnotationCreator#create(Annotation, AnnotatedElement) create} method with the
     * given {@link Annotation} and {@link AnnotatedElement} from the meta-annotation (which must be
     * present on the annotation).
     * 
     * @param a annotation that is annotated with the meta-annotation
     * @param annotatedElement the element annotated with the annotation
     * @param metaAnnotationClass the class of the meta-{@link Annotation} (must be present on the
     *            annotation)
     * @param valueGetter the function to extract the meta-annotation's {@code value}
     * @param creatorClass the class defined by the value, used to create the object
     * @return the object created by the {@link ObjectFromAnnotationCreator}
     * @throws IllegalArgumentException if the object could not be created
     */
    public static <T, META extends Annotation, C extends ObjectFromAnnotationCreator<T>> T create(
            Annotation a,
            AnnotatedElement annotatedElement,
            Class<META> metaAnnotationClass,
            Function<META, Class<? extends C>> valueGetter,
            Class<C> creatorClass) {
        try {
            C creator = valueGetter
                    .apply(getMetaAnnotation(a, metaAnnotationClass, creatorClass))
                    .getConstructor()
                    .newInstance();
            return creator.create(a, annotatedElement);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(
                    String.format("Cannot instantiate %s", creatorClass.getName()),
                    e);
        }
    }

    /**
     * Reads the meta-annotations from the {@link Annotation Annotations} on the given
     * {@link AnnotatedElement}, extracts the {@link ObjectFromAnnotationCreator} via the
     * {@code valueGetter}, instantiates it and returns the object created by a call to the creator's
     * {@link ObjectFromAnnotationCreator#create(Annotation, AnnotatedElement) create} method.
     * 
     * @param annotatedElement the element annotated with an annotation that defines the creator
     * @param metaAnnotationClass the class of the meta-{@link Annotation} (must be present on the
     *            annotation)
     * @param valueGetter the function to extract the meta-annotation's {@code value}
     * @param creatorClass the class defined by the value, used to create the object
     * @return the object created by the {@link ObjectFromAnnotationCreator}
     * @throws IllegalArgumentException if the object could not be created or there are multiple
     *             annotations that could create one
     */
    public static <T, META extends Annotation, C extends ObjectFromAnnotationCreator<T>> Optional<T> find(
            AnnotatedElement annotatedElement,
            Class<META> metaAnnotationClass,
            Function<META, Class<? extends C>> valueGetter,
            Class<C> creatorClass) {
        BinaryOperator<T> conflictResuolution = (a1, a2) -> {
            throw new IllegalArgumentException(annotatedElement + " has multiple annotations with a "
                    + metaAnnotationClass.getSimpleName());
        };
        return find(annotatedElement, metaAnnotationClass, valueGetter, creatorClass, conflictResuolution);
    }

    /**
     * Reads the meta-annotations from the {@link Annotation Annotations} on the given
     * {@link AnnotatedElement}, extracts the {@link ObjectFromAnnotationCreator} via the
     * {@code valueGetter}, instantiates it and returns the object created by a call to the creator's
     * {@link ObjectFromAnnotationCreator#create(Annotation, AnnotatedElement) create} method.
     * <p>
     * Should multiple annotations crate an object, the given conflict resolution operator is called to
     * reduce the objects to one or throw an exception if that is an illegal configuration.
     * 
     * @param annotatedElement the element annotated with an annotation that defines the creator
     * @param metaAnnotationClass the class of the meta-{@link Annotation} (must be present on the
     *            annotation)
     * @param valueGetter the function to extract the meta-annotation's {@code value}
     * @param creatorClass the class defined by the value, used to create the object
     * @param conflictResuolution called if there are multiple creators defined by annotations
     *
     * @return the object created by the {@link ObjectFromAnnotationCreator}
     *
     * @throws IllegalArgumentException if the object could not be created or there are multiple
     *             annotations that could create one
     */
    public static <T, META extends Annotation, C extends ObjectFromAnnotationCreator<T>> Optional<T> find(
            AnnotatedElement annotatedElement,
            Class<META> metaAnnotationClass,
            Function<META, Class<? extends C>> valueGetter,
            Class<C> creatorClass,
            BinaryOperator<T> conflictResuolution) {
        return Arrays.stream(annotatedElement.getAnnotations())
                .filter(a -> MetaAnnotationReader.isMetaAnnotationPresent(a, metaAnnotationClass))
                .map(a -> create(a, annotatedElement, metaAnnotationClass, valueGetter, creatorClass))
                .reduce(conflictResuolution);
    }

    private static <META extends Annotation, C> META getMetaAnnotation(Annotation annotation,
            Class<META> metaAnnotationClass,
            Class<C> definitionCreatorClass) {
        if (!isMetaAnnotationPresent(annotation, metaAnnotationClass)) {
            throw new IllegalArgumentException(
                    String.format("%s has no % annotation that defines a %s", annotation,
                                  metaAnnotationClass.getName(),
                                  definitionCreatorClass.getName()));
        } else {
            return annotation.annotationType().getAnnotation(metaAnnotationClass);
        }
    }
}
