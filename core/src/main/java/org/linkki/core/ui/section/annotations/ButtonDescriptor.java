/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import org.linkki.core.binding.ButtonBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class ButtonDescriptor extends ElementDescriptor {

    private final String methodName;

    public ButtonDescriptor(UIButtonDefinition buttonAnnotation, UIToolTipDefinition toolTipDefinition,
            String methodName, Class<?> pmoClass) {
        super(buttonAnnotation, toolTipDefinition, pmoClass);
        this.methodName = requireNonNull(methodName, "methodName must not be null");
    }

    @Override
    protected UIButtonDefinition getBindingDefinition() {
        return (UIButtonDefinition)super.getBindingDefinition();
    }

    @Override
    public String getModelPropertyName() {
        return methodName;
    }

    @Override
    public String getModelObjectName() {
        return ModelObject.DEFAULT_NAME;
    }

    @Override
    public ButtonBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler updateUi,
            Component component,
            @Nullable Label label) {
        requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
        requireNonNull(updateUi, "updateUi must not be null");
        requireNonNull(component, "component must not be null");

        return new ButtonBinding(label, (Button)component, propertyDispatcher, updateUi, true);
    }

    @Override
    public String getPmoPropertyName() {
        return methodName;
    }

    public CaptionType captionType() {
        return getBindingDefinition().captionType();
    }

    public String caption() {
        return getBindingDefinition().caption();
    }
}
