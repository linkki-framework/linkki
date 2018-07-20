/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section.descriptor;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.aspect.AspectAnnotationReader;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.TableColumnDescriptor;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UITableColumn;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.util.BeanUtils;

/**
 * Reads UIField annotations, e.g. {@link UITextField}, {@link UIComboBox}, etc. from a given
 * object's class.
 * <p>
 * Provides a set of {@link ElementDescriptor} for all found properties via {@link #getUiElements()}
 */
public class UIAnnotationReader {

    private final Class<?> annotatedClass;
    private final Map<String, PropertyElementDescriptors> descriptorsByProperty = new HashMap<>();
    private final Map<PropertyElementDescriptors, TableColumnDescriptor> columnDescriptors = new HashMap<>();

    public UIAnnotationReader(Class<?> annotatedClass) {
        this.annotatedClass = requireNonNull(annotatedClass, "annotatedClass must not be null");
        initDescriptorMaps();
    }

    @SuppressWarnings("null")
    private void initDescriptorMaps() {
        Method[] methods = annotatedClass.getMethods();
        for (Method method : methods) {
            Arrays.stream(method.getAnnotations()).forEach(a -> createAndAddDescriptor(a, method));
        }
    }

    private void createAndAddDescriptor(Annotation annotation, Method method) {

        List<LinkkiAspectDefinition> aspectDefs = AspectAnnotationReader.createAspectDefinitionsFrom(annotation);
        String pmoPropertyName = getPmoPropertyName(method);
        PropertyElementDescriptors elementDescriptors = descriptorsByProperty.computeIfAbsent(pmoPropertyName,
                                                                                              PropertyElementDescriptors::new);

        if (BindingDefinition.isLinkkiBindingDefinition(annotation)) {
            BindingDefinition uiElement = BindingDefinition.from(annotation);
            addDescriptor(elementDescriptors, uiElement, pmoPropertyName, annotation, aspectDefs);
        } else {
            elementDescriptors.addAspect(aspectDefs);

            if (annotation instanceof UITableColumn) {
                columnDescriptors.put(elementDescriptors,
                                      new TableColumnDescriptor(annotatedClass, method, (UITableColumn)annotation));
            }
        }
    }

    private PropertyElementDescriptors addDescriptor(PropertyElementDescriptors elementDescriptors,
            BindingDefinition uiElement,
            String pmoPropertyName,
            Annotation annotation,
            List<LinkkiAspectDefinition> aspectDefs) {

        elementDescriptors.addDescriptor(annotation,
                                         new ElementDescriptor(uiElement, pmoPropertyName,
                                                 annotatedClass,
                                                 aspectDefs),
                                         annotatedClass);

        return elementDescriptors;
    }

    private String getPmoPropertyName(Method method) {
        if (method.getReturnType() == Void.TYPE) {
            return method.getName();
        } else if (method.getName().startsWith("is")) {
            return StringUtils.uncapitalize(method.getName().substring(2));
        } else if (method.getName().startsWith("get")) {
            return StringUtils.uncapitalize(method.getName().substring(3));
        } else {
            return method.getName();
        }
    }


    /**
     * Currently for testing purposes only!
     *
     * @return the descriptor for the given property.
     *
     * @throws NoSuchElementException if no descriptor with the given property can be found
     */
    public PropertyElementDescriptors findDescriptors(String propertyName) {
        return getUiElements().stream()
                .filter(el -> el.getPmoPropertyName().equals(propertyName))
                .findFirst()
                .get();
    }

    public boolean hasTableColumnAnnotation(PropertyElementDescriptors d) {
        return columnDescriptors.containsKey(d);
    }

    /**
     * Returns all descriptors that are found by this reader. The descriptors are ordered by their
     * position.
     * 
     * @return all descriptors orders by the position.
     */
    public LinkedHashSet<PropertyElementDescriptors> getUiElements() {
        validateNoDuplicatePosition();
        return descriptorsByProperty.values().stream()
                .filter(PropertyElementDescriptors::isNotEmpty)
                .sorted(Comparator.comparing(PropertyElementDescriptors::getPosition))
                .collect(Collectors.toCollection(LinkedHashSet::new));
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

    public Optional<TableColumnDescriptor> getTableColumnDescriptor(PropertyElementDescriptors d) {
        return Optional.ofNullable(columnDescriptors.get(d));
    }

    /**
     * Reads the given presentation model object's class to find a method annotated with
     * {@link ModelObject @ModelObject} and the annotation's {@link ModelObject#name()} matching the
     * given model object name. Returns a supplier that supplies a model object by invoking that
     * method.
     *
     * @param pmo a presentation model object
     * @param modelObjectName the name of the model object as provided by a method annotated with
     *            {@link ModelObject @ModelObject}
     *
     * @return a supplier that supplies a model object by invoking the annotated method
     *
     * @throws ModelObjectAnnotationException if no matching method is found or the method has no
     *             return value
     */
    public static Supplier<?> getModelObjectSupplier(Object pmo, String modelObjectName) {
        requireNonNull(pmo, "pmo must not be null");
        requireNonNull(modelObjectName, "modelObjectName must not be null");
        Optional<Method> modelObjectMethod = BeanUtils
                .getMethod(requireNonNull(pmo, "pmo must not be null").getClass(),
                           (m) -> m.isAnnotationPresent(ModelObject.class)
                                   && m.getAnnotation(ModelObject.class).name().equals(modelObjectName));
        if (modelObjectMethod.isPresent()) {
            Method method = modelObjectMethod.get();
            if (Void.TYPE.equals(method.getReturnType())) {
                throw new ModelObjectAnnotationException(pmo, method);
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
        if (ModelObject.DEFAULT_NAME.equals(modelObjectName)) {
            throw new ModelObjectAnnotationException(pmo);
        } else {
            throw new ModelObjectAnnotationException(pmo, modelObjectName);
        }
    }

    /**
     * Tests if the presentation model object has a method annotated with
     * {@link ModelObject @ModelObject} using a given name
     *
     * @param pmo an object used for a presentation model
     * @param modelObjectName the name of the model object
     *
     * @return whether the object has a method annotated with {@link ModelObject @ModelObject} using
     *         the given name
     */
    public static boolean hasModelObjectAnnotatedMethod(Object pmo, @Nullable String modelObjectName) {
        return BeanUtils.getMethod(requireNonNull(pmo, "pmo must not be null").getClass(),
                                   (m) -> m.isAnnotationPresent(ModelObject.class)
                                           && m.getAnnotation(ModelObject.class).name().equals(modelObjectName))
                .isPresent();
    }

    /**
     * Thrown when trying to get a method annotated with {@link ModelObject @ModelObject} via
     * {@link UIAnnotationReader#getModelObjectSupplier(Object, String)} fails.
     */
    public static final class ModelObjectAnnotationException extends IllegalArgumentException {
        private static final long serialVersionUID = 1L;

        public ModelObjectAnnotationException(Object pmo) {
            super("Presentation model object " + pmo + " has no method annotated with @"
                    + ModelObject.class.getSimpleName());
        }

        public ModelObjectAnnotationException(Object pmo, String modelObjectName) {
            super("Presentation model object " + pmo + " has no method annotated with @"
                    + ModelObject.class.getSimpleName() + " for the model object named \"" + modelObjectName + "\"");
        }

        public ModelObjectAnnotationException(Object pmo, Method method) {
            super("Presentation model object " + pmo + "'s method " + method.getName() + " is annotated with @"
                    + ModelObject.class.getSimpleName() + " but returns void");
        }

    }

}
