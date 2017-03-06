/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class LabelBinding implements ElementBinding {

    private final Label label;
    private final Optional<Label> labelsLabel;
    private final PropertyDispatcher propertyDispatcher;

    public LabelBinding(@Nullable Label labelsLabel, Label label, PropertyDispatcher propertyDispatcher) {
        this.labelsLabel = Optional.ofNullable(labelsLabel);
        requireNonNull(label, "label must not be null");
        this.label = label;
        requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
        this.propertyDispatcher = propertyDispatcher;
    }

    @Override
    public Component getBoundComponent() {
        return label;
    }

    @Override
    public void updateFromPmo() {
        boolean visible = propertyDispatcher.isVisible();
        labelsLabel.ifPresent(l -> l.setVisible(visible));
        String toolTip = propertyDispatcher.getToolTip();
        labelsLabel.ifPresent(l -> l.setDescription(toolTip));
        label.setDescription(toolTip);
        label.setValue(Objects.toString(propertyDispatcher.getValue(), ""));
        label.setVisible(visible);
    }

    @Override
    public MessageList displayMessages(@Nullable MessageList messages) {
        return new MessageList();
    }

    @Override
    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

}
