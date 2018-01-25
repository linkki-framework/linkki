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
package org.linkki.core.ui.section.annotations;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.linkki.core.binding.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyNamingConvention;
import org.linkki.core.binding.dispatcher.accessor.PropertyAccessor;
import org.linkki.core.binding.dispatcher.accessor.PropertyAccessorCache;

/**
 * This class stores all {@link ElementDescriptor}s of a PMO specified at the same property.
 * <p>
 * In case only one {@link ElementDescriptor} exists for a position, no additional method is needed
 * in the PMO. <br>
 * Otherwise to resolve which ElementDescriptor shall be used, the PMO must provide a method with
 * the signature: {@code public Class get[pmoPropertyName]ComponentType()} see
 * {@link PropertyNamingConvention#getComponentTypeProperty(String)}.
 * <p>
 * NOTE: If more UIElements are specified for a property, the annotations must have the same
 * position and the {@code label}'s must be equal otherwise an {@link IllegalStateException} will be
 * thrown.
 */
public class PropertyElementDescriptors {

    private static final PropertyNamingConvention PROPERTY_NAMING_CONVENTION = new PropertyNamingConvention();

    private final Map<Class<? extends Annotation>, ElementDescriptor> descriptors;
    private final String pmoPropertyName;

    private int position;
    private String labelText;

    private final List<LinkkiAspectDefinition> additionalAspects = new ArrayList<>();

    // position and labelText are null at initilization but
    // the first element is added immediatly after the creation of the
    // object
    // since this constructor is package-private it can not be called from
    // a user of the framework so it's ok...
    // to make eclipse happy we suppress the null warning :/
    @SuppressWarnings("null")
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

    public String getLabelText() {
        return labelText;
    }

    public ElementDescriptor getDescriptor(Object pmo) {
        ElementDescriptor descriptor;
        if (descriptors.size() == 1) {
            descriptor = descriptors.values()
                    .iterator()
                    .next();
        } else {
            Class<? extends Annotation> initialAnnotation = getInitialAnnotationClassFromPmo(pmo);
            descriptor = descriptors.get(initialAnnotation);
            if (descriptor == null) {
                throw new IllegalStateException(String.format("No descriptor found for annotation @%s for property %s",
                                                              initialAnnotation.getSimpleName(), pmoPropertyName));
            }
        }
        descriptor.addAspectDefinitions(additionalAspects);
        return descriptor;
    }

    void addDescriptor(Annotation annotation, ElementDescriptor descriptor, Class<?> pmoClass) {
        Validate.isTrue(pmoPropertyName.equals(descriptor.getPmoPropertyName()), String
                .format("Cannot add descriptor for property %s to PropertyElementDescriptors intended for property %s",
                        descriptor.getPmoPropertyName(), pmoPropertyName));

        if (descriptors.isEmpty()) {
            position = descriptor.getPosition();
            labelText = descriptor.getLabelText();
        } else {
            validateDynamicFieldDescriptor(descriptor, pmoClass);
        }

        descriptors.put(annotation.annotationType(), descriptor);
    }

    /**
     * A property can only have two different descriptors if the have the same position and a method
     * with {@link PropertyNamingConvention#getComponentTypeProperty(String)} exists for the
     * property.
     */
    private void validateDynamicFieldDescriptor(ElementDescriptor descriptor, Class<?> pmoClass) {
        Validate.validState(descriptor.getPosition() == position, String
                .format("UIElement annotations for property '%s' do not all have the same position",
                        pmoPropertyName));

        Validate.validState(labelText.equals(descriptor.getLabelText()),
                            "Labels for property %s in pmo class %s don't match. Values are: '%s' and '%s'",
                            pmoPropertyName, pmoClass.getName(), labelText, descriptor.getLabelText());

        PropertyAccessor propertyAccessor = PropertyAccessorCache
                .get(pmoClass, PROPERTY_NAMING_CONVENTION.getComponentTypeProperty(getPmoPropertyName()));
        if (!propertyAccessor.canRead()) {
            throw new IllegalStateException(String
                    .format("Method %s must be present in pmo class %s if more than one UIElement is defined for the same property %s",
                            "get" + StringUtils.capitalize(PROPERTY_NAMING_CONVENTION
                                    .getComponentTypeProperty(getPmoPropertyName())),
                            pmoClass, pmoPropertyName));
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Annotation> getInitialAnnotationClassFromPmo(Object pmo) {
        PropertyAccessor propertyAccessor = PropertyAccessorCache
                .get(pmo.getClass(), PROPERTY_NAMING_CONVENTION.getComponentTypeProperty(getPmoPropertyName()));
        return (Class<? extends Annotation>)propertyAccessor.getPropertyValue(pmo);
    }

    public void addAspect(List<LinkkiAspectDefinition> aspectDefs) {
        additionalAspects.addAll(aspectDefs);
    }

    public boolean isNotEmpty() {
        return !descriptors.isEmpty();
    }
}
