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

public class ButtonDescriptor implements ElementDescriptor {

    private final UIButtonDefinition uiButton;
    private final String methodName;
    private String modelObjectName;

    public ButtonDescriptor(UIButtonDefinition buttonAnnotation, String methodName, String modelObjectName) {
        this.uiButton = buttonAnnotation;
        this.methodName = methodName;
        this.modelObjectName = modelObjectName;
    }

    @Override
    public EnabledType enabled() {
        return uiButton.enabled();
    }

    @Override
    public VisibleType visible() {
        return uiButton.visible();
    }

    @Override
    public int getPosition() {
        return uiButton.position();
    }

    @Override
    public String getLabelText() {
        if (uiButton.showLabel()) {
            return uiButton.label();
        } else {
            return "";
        }

    }

    @Override
    public Component newComponent() {
        return uiButton.newComponent();
    }

    @Override
    public String getPropertyName() {
        return methodName;
    }

    @Override
    public String getModelObjectName() {
        return modelObjectName;
    }

    @Override
    public ButtonBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler updateUi,
            Component component,
            Label label) {
        requireNonNull(propertyDispatcher, "PropertyDispatcher must not be null");
        requireNonNull(updateUi, "UpdateUI-Handler must not be null");
        requireNonNull(component, "Component must not be null");
        return new ButtonBinding(label, (Button)component, propertyDispatcher, updateUi);
    }

    @Override
    public RequiredType required() {
        return RequiredType.NOT_REQUIRED;
    }

    @Override
    public AvailableValuesType availableValues() {
        return AvailableValuesType.NO_VALUES;
    }

}
