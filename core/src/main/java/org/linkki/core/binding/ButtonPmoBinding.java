/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class ButtonPmoBinding implements ElementBinding, Serializable {

    private static final long serialVersionUID = 1L;

    private final BindingContext bindingContext;
    private final Button button;
    private final ButtonPmo pmo;
    private final PropertyDispatcher propertyDispatcher;

    public ButtonPmoBinding(BindingContext bindingContext, ButtonPmo pmo, Button button,
            PropertyDispatcher propertyDispatcher) {
        this.bindingContext = bindingContext;
        this.button = button;
        this.propertyDispatcher = propertyDispatcher;
        this.pmo = pmo;
        button.addClickListener(this::buttonClickCallback);
    }

    public static ButtonPmoBinding create(BindingContext bindingContext,
            ButtonPmo pmo,
            Button button,
            PropertyDispatcher propertyDispatcher) {
        ButtonPmoBinding buttonPmoBinding = new ButtonPmoBinding(bindingContext, pmo, button, propertyDispatcher);
        bindingContext.add(buttonPmoBinding);
        return buttonPmoBinding;
    }

    public static Button createBoundButton(BindingContext bindingContext,
            ButtonPmo pmo,
            PropertyDispatcher propertyDispatcher) {
        Button button = ComponentFactory.newButton(pmo.getButtonIcon(), pmo.getStyleNames());
        create(bindingContext, pmo, button, propertyDispatcher);
        return button;
    }

    @Override
    public void updateFromPmo() {
        button.setEnabled(isEnabled());
        button.setVisible(isVisible());
    }

    @Override
    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

    public boolean isEnabled() {
        return propertyDispatcher.isEnabled(StringUtils.EMPTY);
    }

    public boolean isVisible() {
        return propertyDispatcher.isVisible(StringUtils.EMPTY);
    }

    private void buttonClickCallback(@SuppressWarnings("unused") ClickEvent event) {
        propertyDispatcher.invoke("onClick");
        bindingContext.updateUI();
    }

    @Override
    public Button getBoundComponent() {
        return button;
    }

    @Override
    public ButtonPmo getPmo() {
        return pmo;
    }

}
