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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.nls.pmo.PmoLabelType;
import org.linkki.core.nls.pmo.PmoNlsService;
import org.linkki.core.ui.section.annotations.BindingDescriptor;
import org.linkki.core.ui.section.annotations.ButtonDescriptor;
import org.linkki.core.ui.section.annotations.CaptionType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.UIButton;
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

    private PmoNlsService pmoNlsService;

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
        pmoNlsService = PmoNlsService.get();
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
     * If the {@linkplain UIButton} annotation caption type is CaptionType.DYNAMIC a method
     * get[AnnotatedMethodName]Caption() is called to retrieve the caption name dynamically
     */
    @Override
    @CheckForNull
    public String getCaption() {
        Object boundObject = getBoundObject();
        if (boundObject == null) {
            return "";
        }
        if (bindingDescriptor instanceof ButtonDescriptor) {
            if (((ButtonDescriptor)bindingDescriptor).captionType() == CaptionType.STATIC) {
                String caption = ((ButtonDescriptor)bindingDescriptor).caption();
                String nlsCaption = pmoNlsService.getLabel(PmoLabelType.BUTTON_CAPTION, boundObject.getClass(),
                                                           getProperty(), caption);

                return nlsCaption;
            } else if (((ButtonDescriptor)bindingDescriptor).captionType() == CaptionType.NONE) {
                return StringUtils.EMPTY;
            } else if (((ButtonDescriptor)bindingDescriptor).captionType() == CaptionType.DYNAMIC) {
                return super.getCaption();
            }
        }
        throw new IllegalArgumentException("Caption only supported for buttons");
    }

    /**
     * Returns the value of the {@link Aspect} if the value is static.
     */
    @CheckForNull
    @Override
    public <T> T getAspectValue(Aspect<T> aspect) {
        return aspect.getStaticValueOr(() -> super.getAspectValue(aspect));
    }
}