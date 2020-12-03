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

package org.linkki.core.ui.wrapper;

import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;

/**
 * Wraps a vaadin component and uses the vaadin built-in caption instead of an extra label component
 * like the {@link LabelComponentWrapper}.
 * <p>
 * Although it is common to create this kind of {@link ComponentWrapper} for
 * {@link WrapperType#COMPONENT} the type is not fixed to {@link WrapperType#COMPONENT} and should be as
 * narrow as possible (for example {@link WrapperType#FIELD} or {@link WrapperType#LAYOUT}.
 */
public class CaptionComponentWrapper extends VaadinComponentWrapper {

    private static final long serialVersionUID = 1L;

    public CaptionComponentWrapper(Component component, WrapperType wrapperType) {
        super(component, wrapperType);
    }

    public CaptionComponentWrapper(String id, Component component, WrapperType wrapperType) {
        this(component, wrapperType);
        component.setId(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote Vaadin distinguishes between no caption (caption is {@code null}) and an empty caption.
     *           In second case, some space is reserved for a caption but no text is displayed. In
     *           annotations we cannot set the {@code null} value hence we cannot distinguish between
     *           {@code null} and empty string. That's why this implementation treats the empty string
     *           as no caption and will set {@code null} as the vaadin caption.
     */
    @Override
    public void setLabel(String labelText) {
        // TODO LIN-2057

        // attempted fix:
        // if (getComponent() instanceof HasText) {
        // ((HasText)getComponent()).setText(labelText);
        // }
    }

    @Override
    public String toString() {
        // TODO LIN-2057
        if (getComponent() instanceof HasText) {
            return ((HasText)getComponent()).getText() + "(" + getComponent().getClass().getSimpleName() + ")";
        } else {
            return getComponent().getClass().getSimpleName();
        }

    }

}
