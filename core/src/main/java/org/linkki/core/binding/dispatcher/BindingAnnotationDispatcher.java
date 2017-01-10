/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.BindingDescriptor;
import org.linkki.core.ui.section.annotations.ButtonDescriptor;
import org.linkki.core.ui.section.annotations.CaptionType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.ToolTipType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.section.annotations.adapters.AvailableValuesProvider;

/**
 * Provides default values for enabled state, required state, visibility and available values
 * defined by annotations like {@link UITextField} and {@link UIComboBox}. If no annotation exists
 * for the given property, or if the type is dynamic, the wrapped dispatcher is accessed for a
 * value.
 */
public class BindingAnnotationDispatcher extends AbstractPropertyDispatcherDecorator {

    private final BindingDescriptor bindingDescriptor;

    /**
     * Creating a new {@link BindingAnnotationDispatcher} for an {@link BindingDescriptor}, passing
     * the wrapped dispatcher that should be decorated by this {@link BindingAnnotationDispatcher}
     *
     * @param wrappedDispatcher The decorated dispatcher
     * @param bindingDescriptor The descriptor for an element annotated with one of the UI
     *            annotations like {@link UITextField} or {@link UIComboBox} or the {@link Bind}
     *            annotation
     */
    public BindingAnnotationDispatcher(@Nonnull PropertyDispatcher wrappedDispatcher,
            @Nonnull BindingDescriptor bindingDescriptor) {
        super(wrappedDispatcher);
        this.bindingDescriptor = requireNonNull(bindingDescriptor, "BindingDescriptor must not be null");
    }

    @Override
    public boolean isEnabled() {
        EnabledType enabledType = bindingDescriptor.enabled();
        if (enabledType == null || enabledType == EnabledType.DYNAMIC) {
            return super.isEnabled();
        } else {
            return enabledType != EnabledType.DISABLED;
        }
    }

    @Override
    public boolean isVisible() {
        VisibleType visibleType = bindingDescriptor.visible();
        if (visibleType == null || visibleType == VisibleType.DYNAMIC) {
            return super.isVisible();
        } else {
            return visibleType != VisibleType.INVISIBLE;
        }
    }

    @Override
    public boolean isRequired() {
        RequiredType requiredType = bindingDescriptor.required();
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
        AvailableValuesType type = Objects
                .requireNonNull(bindingDescriptor.availableValues(),
                                "AvailableValuesType null is not allowed, use AvailableValuesType.NO_VALUES to indicate that available values cannot be obtained");
        if (type == AvailableValuesType.NO_VALUES) {
            return Collections.emptySet();
        } else if (type == AvailableValuesType.DYNAMIC) {
            return super.getAvailableValues();
        } else {
            if (getValueClass().isEnum()) {
                return AvailableValuesProvider.enumToValues(getValueClass().asSubclass(Enum.class),
                                                            type == AvailableValuesType.ENUM_VALUES_INCL_NULL);
            }
            if (getValueClass() == Boolean.TYPE) {
                return AvailableValuesProvider.booleanPrimitiveToValues();
            }
            if (getValueClass() == Boolean.class) {
                return AvailableValuesProvider.booleanWrapperToValues();
            } else {
                throw new IllegalStateException(
                        "Cannot retrieve list of available values for field " + getProperty() + ", valueClass " + type);
            }

        }
    }

    @Override
    public String toString() {
        return "BindingAnnotationDispatcher [wrappedDispatcher=" + getWrappedDispatcher() + ", bindingDescriptor="
                + bindingDescriptor + "]";
    }

    /**
     * If the {@linkplain UIButton} annotation caption type is CaptionType.DYNAMIC a method
     * get[AnnotatedMethodName]Caption() is called to retrieve the caption name dynamically
     */
    @Override
    public String getCaption() {
        if (bindingDescriptor instanceof ButtonDescriptor) {
            if (((ButtonDescriptor)bindingDescriptor).captionType() == CaptionType.STATIC) {
                return ((ButtonDescriptor)bindingDescriptor).caption();
            } else if (((ButtonDescriptor)bindingDescriptor).captionType() == CaptionType.NONE) {
                return StringUtils.EMPTY;
            } else if (((ButtonDescriptor)bindingDescriptor).captionType() == CaptionType.DYNAMIC) {
                return super.getCaption();
            }
        }
        throw new IllegalArgumentException("Caption only supported for buttons");
    }

    @Override
    public String getToolTip() {
        if (bindingDescriptor.getToolTipType() == ToolTipType.DYNAMIC) {
            return super.getToolTip();
        }
        return bindingDescriptor.getToolTip();
    }
}