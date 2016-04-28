/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;

import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.FieldDescriptor;
import org.linkki.core.ui.section.annotations.RequiredType;
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
 * @author widmaier
 */
public class BindingAnnotationDispatcher extends AbstractPropertyDispatcherDecorator {

    private final ElementDescriptor elementDescriptor;

    /**
     * Creating a new {@link BindingAnnotationDispatcher} for an {@link ElementDescriptor}, passing
     * the wrapped dispatcher that should be decorated by this {@link BindingAnnotationDispatcher}
     *
     * @param wrappedDispatcher The decorated dispatcher
     * @param elementDescriptor The descriptor for an element annotated with one of the UI
     *            annotations like {@link UITextField} or {@link UIComboBox}
     */
    public BindingAnnotationDispatcher(@Nonnull PropertyDispatcher wrappedDispatcher,
            @Nonnull ElementDescriptor elementDescriptor) {
        super(wrappedDispatcher);
        this.elementDescriptor = requireNonNull(elementDescriptor, "ElementDescriptor must not be null");
    }

    @Override
    public boolean isEnabled() {
        EnabledType enabledType = elementDescriptor.enabled();
        if (enabledType == null || enabledType == EnabledType.DYNAMIC) {
            return super.isEnabled();
        } else {
            return enabledType != EnabledType.DISABLED;
        }
    }

    @Override
    public boolean isVisible() {
        VisibleType visibleType = elementDescriptor.visible();
        if (visibleType == null || visibleType == VisibleType.DYNAMIC) {
            return super.isVisible();
        } else {
            return visibleType != VisibleType.INVISIBLE;
        }
    }

    @Override
    public boolean isRequired() {
        RequiredType requiredType = fieldDescriptor().required();
        if (requiredType == null || requiredType == RequiredType.DYNAMIC) {
            return super.isRequired();
        } else if (requiredType == RequiredType.NOT_REQUIRED) {
            return false;
        } else if (requiredType == RequiredType.REQUIRED_IF_ENABLED) {
            return isEnabled();
        } else {
            return true;
        }
    }

    @Override
    @Nonnull
    public Collection<?> getAvailableValues() {
        AvailableValuesType type = fieldDescriptor().availableValues();
        if (type == null || type == AvailableValuesType.DYNAMIC) {
            return super.getAvailableValues();
        } else {
            return getAvailableValuesByValueClass(type == AvailableValuesType.ENUM_VALUES_INCL_NULL);
        }
    }

    private Collection<?> getAvailableValuesByValueClass(boolean inclNull) {
        Class<?> type = getValueClass();
        Object[] values = type.getEnumConstants();
        if (values == null) {
            throw new IllegalStateException(
                    "Can't get list of values for Field " + getProperty() + ", ValueClass " + type);
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

    private FieldDescriptor fieldDescriptor() {
        Preconditions.checkState(elementDescriptor instanceof FieldDescriptor,
                                 "Property %s is not annotated with an ui field annotation", getProperty());
        return (FieldDescriptor)elementDescriptor;
    }

    @Override
    public String toString() {
        return "BindingAnnotationDispatcher [wrappedDispatcher=" + getWrappedDispatcher() + ", elementDescriptor="
                + elementDescriptor + "]";
    }

}
