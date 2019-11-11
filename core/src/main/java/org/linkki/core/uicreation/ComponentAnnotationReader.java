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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader;
import org.linkki.core.binding.dispatcher.reflection.accessor.PropertyAccessor;
import org.linkki.core.binding.dispatcher.reflection.accessor.PropertyAccessorCache;
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

    public static final String COMPONENT_PROPERTY_SUFFIX = "ComponentType";

    private static final MetaAnnotation<LinkkiComponent> LINKKI_COMPONENT_ANNOTATION = MetaAnnotation
            .of(LinkkiComponent.class);

    private ComponentAnnotationReader() {
        // do not instantiate
    }

    /**
     * Checks whether the given {@link Annotation} defines a {@link LinkkiComponentDefinition} using the
     * {@link LinkkiComponent} annotation.
     * 
     * @param annotation the annotation that should be checked
     * @return <code>true</code> if the annotation defines a {@link LinkkiComponentDefinition} otherwise
     *         <code>false</code>
     */
    public static boolean isComponentDefinition(@CheckForNull Annotation annotation) {
        return LINKKI_COMPONENT_ANNOTATION.isPresentOn(annotation);
    }

    /**
     * Checks whether the given {@link AnnotatedElement} defines a {@link LinkkiComponentDefinition}
     * that means it is annotated with at least one annotation that
     * {@link #isComponentDefinition(Annotation) is a component definition annotation}.
     * 
     * @param annotatedElement the element that should be checked
     * @return <code>true</code> if the element defines a {@link LinkkiComponentDefinition} otherwise
     *         <code>false</code>
     */
    public static boolean isComponentDefinitionPresent(AnnotatedElement annotatedElement) {
        return LINKKI_COMPONENT_ANNOTATION
                .isPresentOnAnyAnnotationOn(annotatedElement);
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
     * <p>
     * 
     * @implSpec This method can only be used if there is maximum one component definition annotation.
     *           If the {@link AnnotatedElement} supports dynamic fields (multiple component
     *           definitions, selected by an extra method) use
     *           {@link #getComponentDefinitionAnnotation(AnnotatedElement, Object)} instead.
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

    /**
     * Finds the annotation of the {@link AnnotatedElement} that defines a
     * {@link LinkkiComponentDefinition}.
     * <p>
     * In most cases only one component definition is used for an annotated element. If there are more
     * than one such annotations the PMO must have an additional method to select the one that should be
     * active for an PMO instance. That method must be a getter with the same property and suffix
     * {@value #COMPONENT_PROPERTY_SUFFIX}.
     * 
     * @param annotatedElement the {@link AnnotatedElement} that is annotated with the component
     *            definition annotations.
     */
    public static Annotation getComponentDefinitionAnnotation(AnnotatedElement annotatedElement, Object pmo) {
        List<Annotation> annotations = LINKKI_COMPONENT_ANNOTATION
                .findAnnotatedAnnotationsOn(annotatedElement)
                .collect(Collectors.toList());
        if (annotations.size() == 1) {
            return annotations.get(0);
        } else {
            String pmoProperty = BoundPropertyAnnotationReader.getBoundProperty(annotatedElement).getPmoProperty();
            Class<? extends Annotation> type = getComponentDefinitionAnnotationClass(pmo, pmoProperty);
            return annotations.stream()
                    .filter(a -> a.annotationType().equals(type))
                    .reduce((a, b) -> {
                        throw new IllegalStateException(
                                "There are multiple annotations of type " + type + " on " + annotatedElement);
                    })
                    .orElseThrow(() -> new IllegalStateException(
                            "There is no annotation of type " + type + " on " + annotatedElement));
        }
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Annotation> getComponentDefinitionAnnotationClass(Object pmo,
            String pmoPropertyName) {
        PropertyAccessor<Object, ?> propertyAccessor = (PropertyAccessor<Object, ?>)PropertyAccessorCache
                .get(pmo.getClass(), getComponentTypeProperty(pmoPropertyName));
        return (Class<? extends Annotation>)propertyAccessor.getPropertyValue(pmo);
    }

    private static String getComponentTypeProperty(String property) {
        return StringUtils.uncapitalize(property + COMPONENT_PROPERTY_SUFFIX);
    }
}
