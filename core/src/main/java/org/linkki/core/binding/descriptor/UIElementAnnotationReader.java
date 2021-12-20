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
package org.linkki.core.binding.descriptor;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader;
import org.linkki.core.uicreation.ComponentAnnotationReader;
import org.linkki.core.uicreation.PositionAnnotationReader;

/**
 * Reads UI field annotations, e.g. {@code @UITextField}, {@code @UIComboBox}, etc. from a given
 * object's class.
 * <p>
 * Provides a set of {@link ElementDescriptor ElementDescriptors} for all found properties via
 * {@link #getUiElements()}
 */
public class UIElementAnnotationReader {

    private final Class<?> annotatedClass;
    private final Map<String, PropertyElementDescriptors> descriptorsByProperty = new HashMap<>();

    public UIElementAnnotationReader(Class<?> annotatedClass) {
        this.annotatedClass = requireNonNull(annotatedClass, "annotatedClass must not be null");
        initDescriptorMaps();
    }

    private void initDescriptorMaps() {
        Method[] methods = annotatedClass.getMethods();
        for (Method method : methods) {
            BoundPropertyAnnotationReader.findBoundProperty(method)
                    .map(BoundProperty::getPmoProperty)
                    .ifPresent(p -> Arrays.stream(method.getAnnotations())
                            .forEach(a -> createAndAddDescriptor(a, method, p)));
        }
    }

    private void createAndAddDescriptor(Annotation annotation, Method method, String pmoProperty) {
        List<LinkkiAspectDefinition> aspectDefs = AspectAnnotationReader.createAspectDefinitionsFrom(annotation);

        PropertyElementDescriptors elementDescriptors = descriptorsByProperty
                .computeIfAbsent(pmoProperty,
                                 PropertyElementDescriptors::new);

        if (ComponentAnnotationReader.isComponentDefinition(annotation)) {
            elementDescriptors.addDescriptor(annotation.annotationType(),
                                             new ElementDescriptor(
                                                     PositionAnnotationReader.getPosition(method),
                                                     ComponentAnnotationReader.getComponentDefinition(annotation,
                                                                                                      method),
                                                     BoundPropertyAnnotationReader.getBoundProperty(annotation, method),
                                                     aspectDefs),
                                             annotatedClass);
        } else {
            elementDescriptors.addAspect(aspectDefs);
        }
    }

    /**
     * Currently for testing purposes only!
     *
     * @return the descriptor for the given property
     *
     * @throws NoSuchElementException if no descriptor with the given property can be found
     */
    public PropertyElementDescriptors findDescriptors(String propertyName) {
        return getUiElements()
                .filter(el -> el.getPmoPropertyName().equals(propertyName))
                .findFirst()
                .get();
    }

    /**
     * Returns all descriptors that are found by this reader. The descriptors are ordered by their
     * position.
     * 
     * @return all descriptors ordered by position
     */
    public Stream<PropertyElementDescriptors> getUiElements() {
        validateNoDuplicatePosition();
        return descriptorsByProperty.values().stream()
                .filter(PropertyElementDescriptors::isNotEmpty)
                .sorted(Comparator.comparing(PropertyElementDescriptors::getPosition));
    }

    private void validateNoDuplicatePosition() {
        Map<Integer, List<String>> propertiesByPosition = descriptorsByProperty.values().stream()
                .filter(PropertyElementDescriptors::isNotEmpty)
                .collect(Collectors.groupingBy(PropertyElementDescriptors::getPosition,
                                               Collectors.mapping(PropertyElementDescriptors::getPmoPropertyName,
                                                                  Collectors.toList())));
        propertiesByPosition.values().stream()
                .filter(s -> s.size() > 1)
                .findFirst()
                .ifPresent(propertiesWithSamePosition -> {
                    throw new IllegalStateException(String.format("Duplicate position in properties %s of pmo class %s",
                                                                  propertiesWithSamePosition,
                                                                  annotatedClass.getName()));
                });
    }

}
