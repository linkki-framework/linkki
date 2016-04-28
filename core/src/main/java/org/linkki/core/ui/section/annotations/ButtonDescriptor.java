/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ElementBinding;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

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
            Object pmo,
            Label label,
            Component component) {
        return bindingContext.bind(pmo, this, label, (Button)component);
    }

    @Override
    public String getPropertyName() {
        return methodName;
    }

}
