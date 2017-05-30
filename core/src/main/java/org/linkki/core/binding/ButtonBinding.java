/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.message.MessageList;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;

public class ButtonBinding implements ElementBinding, Serializable {

    private static final long serialVersionUID = 1L;

    private final Button button;
    private final Optional<Label> label;
    private final PropertyDispatcher propertyDispatcher;
    private final Handler updateUi;

    private boolean bindCaption;

    /**
     * Creates a new {@link ButtonBinding}.
     * 
     * @param label the button's label (optional)
     * @param button the {@link Button} to be bound
     * @param propertyDispatcher the {@link PropertyDispatcher} handling the bound property in the
     *            model object
     * @param updateUi a {@link Handler} that is called when this {@link Binding} desires an update
     *            of the UI. Usually the {@link BindingContext#updateUI()} method.
     * @param bindCaption indicates whether the button's caption should be bound. <code>true</code>
     *            updates the caption. <code>false</code> prevents caption updates and thus also
     *            prevents a caption to be requested from the property dispatcher.
     */
    public ButtonBinding(@Nullable Label label, Button button, PropertyDispatcher propertyDispatcher,
            Handler updateUi, boolean bindCaption) {
        this.label = Optional.ofNullable(label);
        this.button = requireNonNull(button, "button must not be null");
        this.propertyDispatcher = requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
        this.updateUi = requireNonNull(updateUi, "updateUi must not be null");
        this.bindCaption = bindCaption;
        button.addClickListener(this::buttonClickCallback);
    }

    @Override
    public void updateFromPmo() {
        button.setEnabled(isEnabled());
        boolean visible = isVisible();
        button.setVisible(visible);
        String toolTip = propertyDispatcher.getToolTip();
        button.setDescription(toolTip);
        label.ifPresent(l -> l.setVisible(visible));
        label.ifPresent(l -> l.setDescription(toolTip));
        if (bindCaption) {
            button.setCaption(getCaption());
        }
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

    @CheckForNull
    public String getCaption() {
        return propertyDispatcher.getCaption();
    }

    private void buttonClickCallback(@SuppressWarnings("unused") ClickEvent event) {
        propertyDispatcher.invoke();
        updateUi.apply();
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
