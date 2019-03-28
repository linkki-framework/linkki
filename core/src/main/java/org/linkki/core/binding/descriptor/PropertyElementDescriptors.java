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

import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspects;
import org.linkki.core.binding.dispatcher.reflection.accessor.PropertyAccessor;
import org.linkki.core.binding.dispatcher.reflection.accessor.PropertyAccessorCache;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * This class stores all {@link ElementDescriptor ElementDescriptors} of a PMO specified at the same
 * property. That includes different {@link LinkkiAspects} that are defined for this property as well as
 * multiple {@link LinkkiComponentDefinition component definitions} that may be present in case of
 * dynamic fields.
 * <p>
 * If multiple {@link LinkkiComponentDefinition component definitions} are present, a method with the
 * signature {@code public Class get[pmoPropertyName]ComponentType()} must exist. Also, the component
 * definitions must define the same position.
 */
public class PropertyElementDescriptors {

    private static final String COMPONENT_PROPERTY_SUFFIX = "ComponentType";

    private final Map<Class<? extends Annotation>, ElementDescriptor> descriptors;

    private final String pmoPropertyName;

    private int position;

    private final List<LinkkiAspectDefinition> additionalAspects = new ArrayList<>();

    PropertyElementDescriptors(String pmoPropertyName) {
        this.pmoPropertyName = pmoPropertyName;
        this.descriptors = new HashMap<>(2);
    }

    public int getPosition() {
        return position;
    }

    public String getPmoPropertyName() {
        return pmoPropertyName;
    }

    public ElementDescriptor getDescriptor(Object pmo) {
        ElementDescriptor descriptor = findDescriptor(pmo);
        descriptor.addAspectDefinitions(additionalAspects);
        return descriptor;
    }

    private ElementDescriptor findDescriptor(Object pmo) {
        if (descriptors.size() == 1) {
            return descriptors.values()
                    .iterator().next();
        } else {
            Class<? extends Annotation> initialAnnotation = getInitialAnnotationClassFromPmo(pmo);
            @CheckForNull
            ElementDescriptor descriptor = descriptors.get(initialAnnotation);
            if (descriptor == null) {
                throw new IllegalStateException(String.format("No descriptor found for annotation @%s for property %s",
                                                              initialAnnotation.getSimpleName(), pmoPropertyName));
            }
            return descriptor;
        }
    }

    /**
     * Adds the given descriptor that is derived from the annotation of the given annotation type. The
     * given annotation type is used as an identifier for the descriptor in case multiple descriptors
     * are present for the property.
     * 
     * @param uiElementAnnotationType type of the annotation that identifies the descriptor
     * @param descriptor information about the binding for this property
     * @param pmoClass the PMO class that must contain the component type discriminator in case multiple
     *            descriptors exist for the property
     */
    void addDescriptor(Class<? extends Annotation> uiElementAnnotationType,
            ElementDescriptor descriptor,
            Class<?> pmoClass) {
        Validate.isTrue(pmoPropertyName.equals(descriptor.getPmoPropertyName()), String
                .format("Cannot add descriptor for property %s to PropertyElementDescriptors intended for property %s",
                        descriptor.getPmoPropertyName(), pmoPropertyName));

        if (descriptors.isEmpty()) {
            position = descriptor.getPosition();
        } else {
            validateDynamicFieldDescriptor(descriptor, pmoClass);
        }

        descriptors.put(uiElementAnnotationType, descriptor);
    }

    /**
     * A property can only have two different descriptors if they have the same position and a method
     * with {@link #getComponentTypeProperty(String)} exists for the property.
     */
    private void validateDynamicFieldDescriptor(ElementDescriptor descriptor, Class<?> pmoClass) {
        Validate.validState(descriptor.getPosition() == position, String
                .format("UIElement annotations for property '%s' do not all have the same position",
                        pmoPropertyName));

        PropertyAccessor<?, ?> propertyAccessor = PropertyAccessorCache
                .get(pmoClass, getComponentTypeProperty(getPmoPropertyName()));
        if (!propertyAccessor.canRead()) {
            throw new IllegalStateException(String
                    .format("Method %s must be present in pmo class %s if more than one UIElement is defined for the same property %s",
                            "get" + StringUtils.capitalize(getComponentTypeProperty(getPmoPropertyName())),
                            pmoClass, pmoPropertyName));
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Annotation> getInitialAnnotationClassFromPmo(Object pmo) {
        PropertyAccessor<Object, ?> propertyAccessor = (PropertyAccessor<Object, ?>)PropertyAccessorCache
                .get(pmo.getClass(), getComponentTypeProperty(getPmoPropertyName()));
        return (Class<? extends Annotation>)propertyAccessor.getPropertyValue(pmo);
    }

    private String getComponentTypeProperty(String property) {
        return StringUtils.uncapitalize(property + COMPONENT_PROPERTY_SUFFIX);
    }

    public void addAspect(List<LinkkiAspectDefinition> aspectDefs) {
        additionalAspects.addAll(aspectDefs);
    }

    public boolean isNotEmpty() {
        return !descriptors.isEmpty();
    }

    /**
     * Returns all {@link LinkkiAspectDefinition aspect definitions} from all {@link ElementDescriptor
     * ElementDescriptors} and all {@link #addAspect(List) added aspect definitions}.
     */
    public List<LinkkiAspectDefinition> getAllAspects() {
        return Stream.concat(
                             descriptors.values().stream()
                                     .map(d -> d.getAspectDefinitions())
                                     .flatMap(Collection::stream),
                             additionalAspects.stream())
                .collect(toList());
    }

}
