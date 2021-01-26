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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader;
import org.linkki.core.uicreation.LinkkiPositioned.Position;
import org.linkki.util.BeanUtils;

/**
 * Reads the position from an annotated element.
 * 
 * @see #getPosition(AnnotatedElement)
 * @see LinkkiPositioned
 */
@SuppressWarnings("deprecation")
public class PositionAnnotationReader {

    private PositionAnnotationReader() {
        // do not instantiate utility class
    }

    /**
     * Returns the position of the {@link AnnotatedElement}. There must be at least one annotation that
     * is marked with {@link LinkkiPositioned @LinkkiPositioned}. If there is more than one annotation,
     * all positioned annotations must define the same position.
     * 
     * @param element The annotated element from which the position should be retrieved
     * @return the position of the element
     * @throws IllegalArgumentException if no position was found or multiple different positions were
     *             defined.
     * @throws RuntimeException if there was an exception while accessing the position property.
     * @throws ClassCastException if the position property is not of type {@link Integer#TYPE int}
     */
    public static int getPosition(AnnotatedElement element) {
        return Arrays.stream(element.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(LinkkiPositioned.class))
                .map(PositionAnnotationReader::getPosition)
                .reduce((a, b) -> verifySamePosition(element, a, b))
                .orElseGet(() -> getDeprecatedPosition(element));
    }

    /**
     * Returns the position that was found at the {@link Annotation}. The annotation must have one
     * property that is marked with {@link LinkkiPositioned.Position @LinkkiPositioned.Position}. If
     * there is no such property or more than one property, an {@link IllegalArgumentException} is
     * thrown.
     * 
     * @param annotation the {@link Annotation} that defines the position
     * @return the position value
     * @throws IllegalArgumentException if there is no property marked with
     *             {@link LinkkiPositioned.Position} or there is more than one property marked with
     *             {@link LinkkiPositioned.Position}
     * @throws ClassCastException if the position property is not of type {@link Integer#TYPE int}
     */
    public static int getPosition(Annotation annotation) {
        return BeanUtils
                .getMethods(annotation.annotationType(), m -> m.isAnnotationPresent(LinkkiPositioned.Position.class))
                .map(m -> getPosition(m, annotation))
                .map(Integer.class::cast)
                .reduce(($1, $2) -> {
                    throw new IllegalArgumentException("The annotation " + annotation.annotationType().getName()
                            + " defines more than one property that is annotated with @"
                            + Position.class.getSimpleName() + ".");
                })
                .orElseThrow(() -> new IllegalArgumentException("The annotation "
                        + annotation.annotationType().getName()
                        + " defines no property that is annotated with @" + Position.class.getSimpleName() + "."));
    }

    private static Object getPosition(Method m, Annotation posAnnotation) {
        try {
            return m.invoke(posAnnotation);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(
                    "Cannot get value from " + m + " on " + posAnnotation + " to get the position",
                    e);
        }
    }

    private static Integer verifySamePosition(AnnotatedElement element, Integer a, Integer b) {
        if (!a.equals(b)) {
            throw new IllegalArgumentException(
                    "Multiple annotations of " + element + " do not define the same position.");
        } else {
            return a;
        }
    }

    private static Integer getDeprecatedPosition(AnnotatedElement element) {
        return Arrays.stream(element.getAnnotations())
                .filter(org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition::isLinkkiBindingDefinition)
                .map(a -> getDeprecatedPosition(a))
                .reduce((a, b) -> verifySamePosition(element, a, b))
                .orElseThrow(() -> new IllegalArgumentException(
                        "There is no annotation at " + element + " that defines the position."));
    }

    private static Integer getDeprecatedPosition(Annotation a) {
        Logger.getLogger(PositionAnnotationReader.class.getName())
                .warning("Getting position from " + a.annotationType().getName()
                        + " using deprecated BindingDefinition#position. Use @LinkkiPositioned instead!");
        org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition bindingDefinition = org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition
                .from(a);
        return bindingDefinition.position();
    }

    /**
     * Returns a {@link Comparator} for {@link AnnotatedElement AnnotatedElements} with annotations that
     * are {@link LinkkiPositioned}.
     * 
     * @param pmoClass the presentation model class containing the {@link AnnotatedElement
     *            AnnotatedElements}; used for error handling only
     * @throws IllegalStateException if {@link AnnotatedElement AnnotatedElements} with the same
     *             {@link #getPosition(AnnotatedElement) position} are compared
     */
    public static Comparator<AnnotatedElement> comparingUniquePositions(Class<?> pmoClass) {
        return (e1, e2) -> {
            int p1 = getPosition(e1);
            int p2 = getPosition(e2);
            if (p1 == p2) {
                throw new IllegalStateException(
                        String.format("Duplicate position in properties %s and %s of pmo class %s",
                                      BoundPropertyAnnotationReader.getBoundProperty(e1).getPmoProperty(),
                                      BoundPropertyAnnotationReader.getBoundProperty(e2).getPmoProperty(),
                                      pmoClass));
            } else {
                return p1 - p2;
            }
        };
    }

}
