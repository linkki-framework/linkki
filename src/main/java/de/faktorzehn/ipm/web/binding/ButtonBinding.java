/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding;

import java.io.Serializable;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;

import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;

public class ButtonBinding implements ElementBinding, Serializable {

    private static final long serialVersionUID = 1L;

    private final BindingContext bindingContext;
    private final String methodName;
    private final Button button;
    private final Label label;
    private final PropertyDispatcher propertyDispatcher;

    public ButtonBinding(BindingContext bindingContext, String methodName, Label label, Button button,
            PropertyDispatcher propertyDispatcher) {
        this.bindingContext = bindingContext;
        this.methodName = methodName;
        this.label = label;
        this.button = button;
        this.propertyDispatcher = propertyDispatcher;
        button.addClickListener(this::buttonClickCallback);
    }

    @Override
    public void updateFromPmo() {

        button.setEnabled(isEnabled());
        boolean visible = isVisible();
        button.setVisible(visible);
        if (label != null) {
            // label is null in case of a table
            label.setVisible(visible);
        }

    }

    @Override
    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

    public boolean isEnabled() {
        return propertyDispatcher.isEnabled(methodName);
    }

    public boolean isVisible() {
        return propertyDispatcher.isVisible(methodName);
    }

    private void buttonClickCallback(@SuppressWarnings("unused") ClickEvent event) {
        propertyDispatcher.invoke(methodName);
        bindingContext.updateUI();
    }

    public static ButtonBinding create(BindingContext bindingContext,
            String methodName,
            Label label,
            Button button,
            PropertyDispatcher propertyDispatcher) {
        return new ButtonBinding(bindingContext, methodName, label, button, propertyDispatcher);
    }
}
