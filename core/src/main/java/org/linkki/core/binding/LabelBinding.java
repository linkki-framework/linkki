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

    /**
     * Creates the binding for a label. If that label is used to display the non-editable value of a
     * property, there might be another label naming that property, which is passed as the first
     * parameter and will be set visible depending on the bound label's visibility.
     * 
     * @param labelTextForLabel the label's label (for example the property name if the label is a
     *            property value)
     * @param label the label to be bound
     * @param propertyDispatcher the dispatcher responsible to retrieve the displayed value and
     *            possibly visibility and tooltip information
     */
    public LabelBinding(@Nullable Label labelTextForLabel, Label label, PropertyDispatcher propertyDispatcher) {
        this.labelsLabel = Optional.ofNullable(labelTextForLabel);
        this.label = requireNonNull(label, "label must not be null");
        this.propertyDispatcher = requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
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

    /**
     * We do not support messages on buttons at the moment.
     */
    @Override
    public MessageList displayMessages(@Nullable MessageList messages) {
        return new MessageList();
    }

    @Override
    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

}
