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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.aspect.AspectAnnotationReader;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.util.BeanUtils;

/**
 * Reads UI field annotations, e.g. {@code @UITextField}, {@code @UIComboBox}, etc. from a given
 * object's class.
 * <p>
 * Provides a set of {@link ElementDescriptor ElementDescriptors} for all found properties via
 * {@link #getUiElements()}
 */
public class UIAnnotationReader {

    private final Class<?> annotatedClass;
    private final Map<@NonNull String, @NonNull PropertyElementDescriptors> descriptorsByProperty = new HashMap<>();

    public UIAnnotationReader(Class<?> annotatedClass) {
        this.annotatedClass = requireNonNull(annotatedClass, "annotatedClass must not be null");
        initDescriptorMaps();
    }

    private void initDescriptorMaps() {
        Method[] methods = annotatedClass.getMethods();
        for (Method method : methods) {
            Arrays.stream(method.getAnnotations()).forEach(a -> createAndAddDescriptor(a, method));
        }
    }

    private void createAndAddDescriptor(Annotation annotation, Method method) {

        List<LinkkiAspectDefinition> aspectDefs = AspectAnnotationReader.createAspectDefinitionsFrom(annotation);
        String pmoPropertyName = getPmoPropertyName(method);
        @NonNull
        PropertyElementDescriptors elementDescriptors = descriptorsByProperty.computeIfAbsent(pmoPropertyName,
                                                                                              PropertyElementDescriptors::new);

        if (BindingDefinition.isLinkkiBindingDefinition(annotation)) {
            BindingDefinition uiElement = BindingDefinition.from(annotation);
            addDescriptor(elementDescriptors, uiElement, pmoPropertyName, annotation, aspectDefs);
        } else {
            elementDescriptors.addAspect(aspectDefs);
        }
    }

    private PropertyElementDescriptors addDescriptor(PropertyElementDescriptors elementDescriptors,
            BindingDefinition uiElement,
            String pmoPropertyName,
            Annotation annotation,
            List<LinkkiAspectDefinition> aspectDefs) {

        elementDescriptors.addDescriptor(annotation,
                                         new ElementDescriptor(uiElement, pmoPropertyName,
                                                 aspectDefs),
                                         annotatedClass);

        return elementDescriptors;
    }

    private String getPmoPropertyName(Method method) {
        return BeanUtils.getPropertyName(method);
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

    /**
     * Reads the given presentation model object's class to find a method or field annotated with
     * {@link ModelObject @ModelObject} and the annotation's {@link ModelObject#name()} matching the
     * given model object name. Returns a supplier that supplies a model object by invoking that method
     * or retrieving the field value.
     *
     * @param pmo a presentation model object
     * @param modelObjectName the name of the model object as provided by a method/field annotated with
     *            {@link ModelObject @ModelObject}
     *
     * @return a supplier that supplies a model object by invoking the annotated method or retrieving
     *         the field value
     *
     * @throws ModelObjectAnnotationException if no matching method or field is found, the method has no
     *             return value or the field has the type {@link Void}.
     */
    public static Supplier<?> getModelObjectSupplier(Object pmo, String modelObjectName) {
        requireNonNull(pmo, "pmo must not be null");
        requireNonNull(modelObjectName, "modelObjectName must not be null");

        Optional<Method> annotatedMethod = getModelObjectMethod(pmo, modelObjectName);
        Optional<Field> annotatedField = getModelObjectField(pmo, modelObjectName);

        if (annotatedMethod.isPresent() && annotatedField.isPresent()) {
            throw ModelObjectAnnotationException.multipleMembersAnnotated(pmo, modelObjectName, annotatedMethod.get(),
                                                                          annotatedField.get());
        }

        return annotatedMethod
                .map(m -> getModelObjectSupplier(pmo, m))
                .orElseGet(() -> annotatedField
                        .map(f -> getModelObjectSupplier(pmo, f))
                        .orElseThrow(() -> ModelObjectAnnotationException.noAnnotatedMember(pmo, modelObjectName)));
    }

    @SuppressWarnings("rawtypes")
    private static Supplier getModelObjectSupplier(Object pmo, Method method) {
        if (Void.TYPE.equals(method.getReturnType())) {
            throw ModelObjectAnnotationException.voidMethod(pmo, method);
        }
        return () -> {
            try {
                return method.invoke(pmo);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new LinkkiBindingException(
                        "Cannot call method to get model object " + pmo.getClass().getName() + "#"
                                + method.getName(),
                        e);
            }
        };
    }

    private static Supplier<?> getModelObjectSupplier(Object pmo, Field field) {
        if (Void.TYPE.equals(field.getType())) {
            throw ModelObjectAnnotationException.voidField(pmo, field);
        }
        return () -> {
            field.setAccessible(true);
            try {
                return field.get(pmo);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new LinkkiBindingException(
                        "Cannot get field value to get model object " + pmo.getClass().getName() + "#"
                                + field.getName(),
                        e);
            }
        };
    }

    /**
     * Tests if the presentation model object has a method annotated with
     * {@link ModelObject @ModelObject} using a given name
     *
     * @param pmo an object used for a presentation model
     * @param modelObjectName the name of the model object
     *
     * @return whether the object has a method annotated with {@link ModelObject @ModelObject} using the
     *         given name
     */
    public static boolean hasModelObjectAnnotation(Object pmo, String modelObjectName) {
        return getModelObjectField(pmo, modelObjectName).isPresent()
                || getModelObjectMethod(pmo, modelObjectName).isPresent();
    }

    private static Optional<Method> getModelObjectMethod(Object pmo, String modelObjectName) {
        return BeanUtils.getMethod(requireNonNull(pmo, "pmo must not be null").getClass(),
                                   (m) -> m.isAnnotationPresent(ModelObject.class)
                                           && requireNonNull(m.getAnnotation(ModelObject.class)).name()
                                                   .equals(modelObjectName));
    }

    private static Optional<Field> getModelObjectField(Object pmo, String modelObjectName) {
        return FieldUtils.getFieldsListWithAnnotation(requireNonNull(pmo, "pmo must not be null").getClass(),
                                                      ModelObject.class)
                .stream()
                .filter(f -> requireNonNull(f.getAnnotation(ModelObject.class)).name().equals(modelObjectName))
                .reduce((f1, f2) -> {
                    throw ModelObjectAnnotationException.multipleMembersAnnotated(pmo, modelObjectName, f1, f2);
                });
    }

    /**
     * Thrown when trying to get a method annotated with {@link ModelObject @ModelObject} via
     * {@link UIAnnotationReader#getModelObjectSupplier(Object, String)} fails.
     */
    public static final class ModelObjectAnnotationException extends IllegalArgumentException {
        private static final long serialVersionUID = 1L;

        private ModelObjectAnnotationException(String description) {
            super(description);
        }

        public static ModelObjectAnnotationException noAnnotatedMember(Object pmo, String modelObjectName) {
            return new ModelObjectAnnotationException("Presentation model object class " + pmo.getClass()
                    + " has no method or field annotated with " + getDescriptionForAnnotation(modelObjectName));
        }

        public static ModelObjectAnnotationException voidField(Object pmo, Field field) {
            return new ModelObjectAnnotationException(
                    "Presentation model object " + pmo + "'s field " + field.getName() + " is annotated with @"
                            + ModelObject.class.getSimpleName() + " but is of type Void");
        }

        public static ModelObjectAnnotationException voidMethod(Object pmo, Method method) {
            return new ModelObjectAnnotationException(
                    "Presentation model object " + pmo + "'s method " + method.getName() + " is annotated with @"
                            + ModelObject.class.getSimpleName() + " but returns void");
        }

        public static ModelObjectAnnotationException multipleMembersAnnotated(Object pmo,
                String modelObjectName,
                Member... annotatedMembers) {
            return new ModelObjectAnnotationException(String.format(
                                                                    "Presentation model object class %s has multiple members (%s) that are annotated with %s",
                                                                    pmo.getClass(),
                                                                    Arrays.stream(annotatedMembers).map(Member::getName)
                                                                            .collect(Collectors.joining(", ")),
                                                                    getDescriptionForAnnotation(modelObjectName)));
        }

        private static String getDescriptionForAnnotation(String modelObjectName) {
            String annotation = "@" + ModelObject.class.getSimpleName();
            return ModelObject.DEFAULT_NAME.equals(modelObjectName)
                    ? annotation
                    : annotation + " for the model object named \"" + modelObjectName + "\"";
        }
    }

}
