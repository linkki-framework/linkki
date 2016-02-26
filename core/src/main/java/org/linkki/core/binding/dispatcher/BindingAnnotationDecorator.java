/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.FieldDescriptor;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIAnnotationReader;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.section.annotations.VisibleType;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;

/**
 * Provides default values for enabled state, required state, visibility and available values
 * defined by annotations like {@link UITextField} and {@link UIComboBox}. If no annotation exists
 * for the given property, or if the type is dynamic, the wrapped dispatcher is accessed for a
 * value.
 *
 * Annotations are read from an arbitrary class passed to the constructor, mostly a subclass of
 * {@link PresentationModelObject}.
 *
 * @author widmaier
 */
public class BindingAnnotationDecorator extends AbstractPropertyDispatcherDecorator {

    private final UIAnnotationReader annotationReader;

    /**
     * Creating a new {@link BindingAnnotationDecorator} for an annotated class, passing the wrapped
     * dispatcher that should be decorated by this {@link BindingAnnotationDecorator}
     *
     * @param wrappedDispatcher The decorated dispatcher
     * @param annotatedClass the annotated class that should be handled
     */
    public BindingAnnotationDecorator(PropertyDispatcher wrappedDispatcher, Class<?> annotatedClass) {
        super(wrappedDispatcher);
        annotationReader = new UIAnnotationReader(annotatedClass);
    }

    @Override
    public boolean isEnabled(String property) {
        if (hasAnnotation(property)) {
            return calculateEnabled(property);
        } else {
            return super.isEnabled(property);
        }
    }

    private boolean hasAnnotation(String property) {
        return annotationReader.hasAnnotation(property);
    }

    private boolean calculateEnabled(String property) {
        EnabledType enabledType = annotationReader.get(property).enabled();
        if (enabledType == EnabledType.DYNAMIC) {
            return super.isEnabled(property);
        } else {
            return enabledType != EnabledType.DISABLED;
        }
    }

    @Override
    public boolean isVisible(String property) {
        if (hasAnnotation(property)) {
            return calculateVisible(property);
        } else {
            return super.isVisible(property);
        }
    }

    private boolean calculateVisible(String property) {
        VisibleType visibleType = annotationReader.get(property).visible();
        if (visibleType == VisibleType.DYNAMIC) {
            return super.isVisible(property);
        } else {
            return visibleType != VisibleType.INVISIBLE;
        }
    }

    @Override
    public boolean isRequired(String property) {
        if (hasAnnotation(property)) {
            return calculateRequired(property);
        } else {
            return super.isRequired(property);
        }
    }

    private boolean calculateRequired(String property) {
        RequiredType requiredType = fieldDescriptor(property).required();
        if (requiredType == RequiredType.DYNAMIC) {
            return super.isRequired(property);
        } else if (requiredType == RequiredType.NOT_REQUIRED) {
            return false;
        } else if (requiredType == RequiredType.REQUIRED_IF_ENABLED) {
            return isEnabled(property);
        } else {
            return true;
        }
    }

    @Override
    public Collection<?> getAvailableValues(String property) {
        if (hasAnnotation(property)) {
            return calculateAvailableValues(property);
        } else {
            return super.getAvailableValues(property);
        }
    }

    private Collection<?> calculateAvailableValues(String property) {
        AvailableValuesType type = fieldDescriptor(property).availableValues();
        if (type == AvailableValuesType.DYNAMIC) {
            return super.getAvailableValues(property);
        } else {
            return getAvailableValuesByValueClass(property, type == AvailableValuesType.ENUM_VALUES_INCL_NULL);
        }
    }

    private Collection<?> getAvailableValuesByValueClass(String property, boolean inclNull) {
        Class<?> type = getValueClass(property);
        Object[] values = type.getEnumConstants();
        if (values == null) {
            throw new IllegalStateException("Can't get list of values for Field " + property + ", ValueClass " + type);
        } else {
            return getCollection(values, inclNull);
        }
    }

    private Collection<?> getCollection(Object[] values, boolean inclNull) {
        if (inclNull) {
            ArrayList<Object> result = new ArrayList<>();
            result.add(null);
            result.addAll(Arrays.asList(values));
            return Collections.unmodifiableCollection(result);
        } else {
            return Arrays.asList(values);
        }
    }

    private FieldDescriptor fieldDescriptor(String property) {
        ElementDescriptor descriptor = annotationReader.get(property);
        Preconditions.checkState(descriptor instanceof FieldDescriptor,
                                 "Property %s is not annotated with an ui field annotation", property);
        return (FieldDescriptor)descriptor;
    }
}
