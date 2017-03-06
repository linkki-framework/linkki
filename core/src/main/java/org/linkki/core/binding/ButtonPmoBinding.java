/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

import javax.annotation.Nullable;

import org.faktorips.runtime.MessageList;
import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.util.ComponentFactory;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class ButtonPmoBinding implements ElementBinding, Serializable {

    private static final long serialVersionUID = 1L;

    private final Button button;
    private final PropertyDispatcher propertyDispatcher;

    private Handler updateUI;

    /**
     * Creates a new {@link ButtonPmoBinding}.
     * 
     * @param button the {@link Button} to be bound
     * @param propertyDispatcher the {@link PropertyDispatcher} handling the bound property in the
     *            model object
     * @param updateUI a {@link Handler} that is called when this {@link Binding} desires an update
     *            of the UI. Usually the {@link BindingContext#updateUI()} method.
     */
    public ButtonPmoBinding(Button button, PropertyDispatcher propertyDispatcher,
            Handler updateUI) {
        requireNonNull(button, "button must not be null");
        this.button = button;
        requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
        this.propertyDispatcher = propertyDispatcher;
        requireNonNull(updateUI, "updateUI must not be null");
        this.updateUI = updateUI;
        button.addClickListener(this::buttonClickCallback);
    }

    public static Button createBoundButton(BindingContext bindingContext, ButtonPmo pmo) {
        Button button = ComponentFactory.newButton(pmo.getButtonIcon(), pmo.getStyleNames());
        bindingContext.bind(pmo, button);
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
        return propertyDispatcher.isEnabled();
    }

    public boolean isVisible() {
        return propertyDispatcher.isVisible();
    }

    private void buttonClickCallback(@SuppressWarnings("unused") ClickEvent event) {
        propertyDispatcher.invoke();
        updateUI.apply();
    }

    @Override
    public Button getBoundComponent() {
        return button;
    }

    /**
     * We do not support messages on buttons at the moment.
     */
    @Override
    public MessageList displayMessages(@Nullable MessageList messages) {
        return new MessageList();
    }
}
