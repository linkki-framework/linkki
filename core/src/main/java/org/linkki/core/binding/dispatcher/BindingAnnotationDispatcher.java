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
package org.linkki.core.binding.dispatcher;

import static java.util.Objects.requireNonNull;

import javax.annotation.CheckForNull;

import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.nls.pmo.PmoNlsService;
import org.linkki.core.ui.section.annotations.BindingDescriptor;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.section.annotations.VisibleType;

/**
 * Provides static values for aspects such as enabled state, required state, visibility and
 * available values defined by annotations like {@link UITextField} and {@link UIComboBox}. If no
 * annotation exists for the given property, or if the type is dynamic, the wrapped dispatcher is
 * accessed for a value.
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
    public BindingAnnotationDispatcher(PropertyDispatcher wrappedDispatcher,
            BindingDescriptor bindingDescriptor) {
        super(wrappedDispatcher);
        this.bindingDescriptor = requireNonNull(bindingDescriptor, "bindingDescriptor must not be null");
    }

    @Override
    public boolean isEnabled() {
        switch (bindingDescriptor.enabled()) {
            case DYNAMIC:
                return super.isEnabled();
            default:
                return bindingDescriptor.enabled() != EnabledType.DISABLED;
        }
    }

    @Override
    public boolean isVisible() {
        switch (bindingDescriptor.visible()) {
            case DYNAMIC:
                return super.isVisible();
            default:
                return bindingDescriptor.visible() != VisibleType.INVISIBLE;
        }
    }

    @Override
    public boolean isRequired() {
        switch (bindingDescriptor.required()) {
            case DYNAMIC:
                return super.isRequired();
            case NOT_REQUIRED:
                return false;
            case REQUIRED_IF_ENABLED:
                return isEnabled();
            default:
                return true;
        }
    }

    @Override
    public String toString() {
        return "BindingAnnotationDispatcher [wrappedDispatcher=" + getWrappedDispatcher() + ", bindingDescriptor="
                + bindingDescriptor + "]";
    }

    /**
     * Returns the value of the {@link Aspect} if the value is static.
     */
    @SuppressWarnings("unchecked")
    @CheckForNull
    @Override
    public <T> T getAspectValue(Aspect<T> aspect) {
        if (aspect.isStatic()) {
            T staticValue = aspect.getStaticValue();
            if (staticValue instanceof String && getBoundObject() != null) {
                Class<? extends Object> pmoClass = requireNonNull(getBoundObject()).getClass();
                return (T)PmoNlsService.get()
                        .getLabel(pmoClass, getProperty(), aspect.getName(), (String)staticValue);
            } else {
                return staticValue;
            }
        } else {
            return super.getAspectValue(aspect);
        }
    }


}