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
import java.util.function.Function;

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
    public static boolean isMetaAnnotationPresent(Annotation annotation,
            Class<? extends Annotation> metaAnnotationClass) {
        return annotation.annotationType().isAnnotationPresent(metaAnnotationClass);
    }

    /**
     * Reads the meta-annotation from the given {@link Annotation}, extracts the
     * {@link ObjectFromAnnotationCreator} via the {@code valueGetter}, instantiates it and returns the
     * object created by a call to the creator's
     * {@link ObjectFromAnnotationCreator#create(Annotation, AnnotatedElement) create} method with the
     * given {@link Annotation} and {@link AnnotatedElement}. from the meta-annotation (which must be
     * present on the annotation).
     * 
     * @param annotation annotation that is annotated with the meta-annotation
     * @param annotatedElement the element annotated with the annotation
     * @param metaAnnotationClass the class of the meta-{@link Annotation} (must be present on the
     *            annotation)
     * @param valueGetter the function to extract the meta-annotation's {@code value}
     * @param creatorClass the class defined by the value, used to create the object
     * @return the object created by the {@link ObjectFromAnnotationCreator}
     * @throws IllegalArgumentException if the object could not be created
     */
    public static <ANNOTATION extends Annotation, T, META extends Annotation, C extends ObjectFromAnnotationCreator<ANNOTATION, T>> T create(
            ANNOTATION annotation,
            AnnotatedElement annotatedElement,
            Class<META> metaAnnotationClass,
            Function<META, Class<? extends C>> valueGetter,
            Class<C> creatorClass) {
        try {
            C creator = valueGetter
                    .apply(getMetaAnnotation(annotation, metaAnnotationClass, creatorClass))
                    .getConstructor()
                    .newInstance();
            return creator.create(annotation, annotatedElement);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(
                    String.format("Cannot instantiate %s", creatorClass.getName()),
                    e);
        }
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
