package org.linkki.core.ui.section.annotations;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.linkki.core.binding.dispatcher.PropertyNamingConvention;
import org.linkki.core.binding.dispatcher.accessor.PropertyAccessor;
import org.linkki.core.binding.dispatcher.accessor.PropertyAccessorCache;

/**
 * This class stores all {@link ElementDescriptor}s of a PMO specified at the same position.
 * <p>
 * In case only one {@link ElementDescriptor} exists for a position, no additional method is needed
 * in the PMO. <br>
 * Otherwise to resolve which ElementDescriptor shall be used, the PMO must provide a method with
 * the signature: {@code public Class get[pmoPropertyName]ComponentType()} see
 * {@link PropertyNamingConvention#getComponentTypeProperty(String)}.
 * <p>
 * NOTE: If more UIElements are specified for a position, the annotations must be on the same method
 * and the {@code label}'s must be equal otherwise an {@link IllegalStateException} will be thrown.
 */
public class ElementDescriptors {

    private static final PropertyNamingConvention PROPERTY_NAMING_CONVENTION = new PropertyNamingConvention();

    private final Map<Class<? extends Annotation>, ElementDescriptor> descriptors;
    private final int position;

    private String pmoPropertyName;
    private String labelText;


    // pmoPropertyName and labelText are null at initilization but
    // the first element is added immediatly after the creation of the
    // object
    // since this constructor is package-private it can not be called from
    // a user of the framework so it's ok...
    // to make eclipse happy we suppress the null warning :/
    @SuppressWarnings("null")
    ElementDescriptors(int position) {
        this.position = position;
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
        if (descriptors.size() == 1) {
            return descriptors.values()
                    .iterator()
                    .next();
        }

        // call method to resolve class
        Class<? extends Annotation> clazz = getAnnotationClassFromPmo(pmo);
        ElementDescriptor descriptor = descriptors.get(clazz);
        if (descriptor == null) {
            throw new IllegalStateException("no descriptor found for annotation @" + clazz.getSimpleName());
        }

        return descriptor;
    }


    void addDescriptor(Annotation annotation, ElementDescriptor descriptor, Class<?> pmoClass) {
        if (descriptors.isEmpty()) {
            pmoPropertyName = descriptor.getPmoPropertyName();
            labelText = descriptor.getLabelText();
        } else {

            Validate.validState(pmoPropertyName.equals(descriptor.getPmoPropertyName()),
                                "Duplicate position in property %s and %s of pmo class %s",
                                pmoPropertyName, descriptor.getPmoPropertyName(), pmoClass.getName());

            Validate.validState(labelText.equals(descriptor.getLabelText()),
                                "Labels for property %s in pmo class %s doesn't match. Values are: '%s' and '%s'",
                                pmoPropertyName, pmoClass.getName(), labelText, descriptor.getLabelText());
        }

        descriptors.put(annotation.annotationType(), descriptor);
    }


    @SuppressWarnings("unchecked")
    private Class<? extends Annotation> getAnnotationClassFromPmo(Object pmo) {

        PropertyAccessor propertyAccessor = PropertyAccessorCache
                .get(pmo.getClass(), PROPERTY_NAMING_CONVENTION.getComponentTypeProperty(getPmoPropertyName()));
        if (!propertyAccessor.canRead()) {
            throw new IllegalStateException(String
                    .format("Method %s must be present in pmo class %s if more than one UIElement is defined for position %d",
                            "get" + StringUtils.capitalize(PROPERTY_NAMING_CONVENTION
                                    .getComponentTypeProperty(getPmoPropertyName())),
                            pmo.getClass(), position));

        } else {
            return (Class<? extends Annotation>)propertyAccessor.getPropertyValue(pmo);
        }
    }

}
