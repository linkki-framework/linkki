/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import java.io.Serializable;

import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;

public class ButtonBinding implements ElementBinding, Serializable {

    private static final long serialVersionUID = 1L;

    private final BindingContext bindingContext;
    private final String methodName;
    private final Button button;
    private final Object pmo;
    private final Label label;
    private final PropertyDispatcher propertyDispatcher;

    public ButtonBinding(BindingContext bindingContext, Object pmo, String methodName, Label label, Button button,
            PropertyDispatcher propertyDispatcher) {
        this.bindingContext = bindingContext;
        this.methodName = methodName;
        this.label = label;
        this.button = button;
        this.propertyDispatcher = propertyDispatcher;
        this.pmo = pmo;
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

    /**
     * Creates a new {@link ButtonBinding} and adds the new binding to the {@link BindingContext}
     * 
     * @param bindingContext The {@link BindingContext} used to bind the button to the PMO
     * @param methodName the method name that should be called when button was clicked
     * @param label the label of the button
     * @param button The button that should be bound
     * @param propertyDispatcher The property dispatcher that is used to call the methods on update.
     * 
     * @return The newly created binding
     */
    public static ButtonBinding create(BindingContext bindingContext,
            String methodName,
            Label label,
            Button button,
            Object pmo,
            PropertyDispatcher propertyDispatcher) {
        ButtonBinding buttonBinding = new ButtonBinding(bindingContext, pmo, methodName, label, button,
                propertyDispatcher);
        bindingContext.add(buttonBinding);
        return buttonBinding;
    }

    @Override
    public Button getBoundComponent() {
        return button;
    }

    @Override
    public Object getPmo() {
        return pmo;
    }

    /**
     * We do not support messages on buttons at the moment.
     */
    @Override
    public MessageList displayMessages(MessageList messages) {
        return new MessageList();
    }
}
