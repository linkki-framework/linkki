/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.ButtonBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class ButtonDescriptor extends ElementDescriptor {

    private final String methodName;

    public ButtonDescriptor(UIButtonDefinition buttonAnnotation, UIToolTipDefinition toolTipDefinition,
            String methodName) {
        super(buttonAnnotation, toolTipDefinition);
        this.methodName = methodName;
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
        return getBindingDefinition().modelObject();
    }

    @Override
    public ButtonBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler updateUi,
            Component component,
            Label label) {
        requireNonNull(propertyDispatcher, "PropertyDispatcher must not be null");
        requireNonNull(updateUi, "UpdateUI-Handler must not be null");
        requireNonNull(component, "Component must not be null");

        return new ButtonBinding(label, (Button)component, propertyDispatcher, updateUi, true);
    }

    @Override
    public RequiredType required() {
        return RequiredType.NOT_REQUIRED;
    }

    @Override
    public AvailableValuesType availableValues() {
        return AvailableValuesType.NO_VALUES;
    }

    @Override
    public String getPmoPropertyName() {
        return getModelPropertyName();
    }

    @Override
    public int getPosition() {
        return getBindingDefinition().position();
    }

    @Override
    public String getLabelText() {
        if (getBindingDefinition().showLabel()) {
            return getBindingDefinition().label();
        } else {
            return "";
        }

    }

    public CaptionType captionType() {
        return getBindingDefinition().captionType();
    }

    public String caption() {
        return getBindingDefinition().caption();
    }
}
