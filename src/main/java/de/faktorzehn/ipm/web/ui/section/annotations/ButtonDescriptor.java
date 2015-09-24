/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section.annotations;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.ButtonBinding;
import de.faktorzehn.ipm.web.binding.ElementBinding;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;

public class ButtonDescriptor implements ElementDescriptor {

    private final UIButtonDefinition uiButton;
    private final String methodName;

    public ButtonDescriptor(UIButtonDefinition buttonAnnotation, String methodName) {
        this.uiButton = buttonAnnotation;
        this.methodName = methodName;
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
    public ElementBinding createBinding(BindingContext bindingContext,
            Label label,
            Component component,
            PropertyDispatcher propertyDispatcher) {
        return new ButtonBinding(bindingContext, getPropertyName(), label, (Button)component, propertyDispatcher);
    }

    @Override
    public String getPropertyName() {
        return methodName;
    }

}
