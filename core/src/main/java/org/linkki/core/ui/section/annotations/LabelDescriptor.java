/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.LabelBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class LabelDescriptor extends ElementDescriptor {

    private final String pmoPropertyName;

    public LabelDescriptor(UILabelDefinition labelDefinition, UIToolTipDefinition toolTipDefinition,
            String pmoPropertyName) {
        super(labelDefinition, toolTipDefinition);
        this.pmoPropertyName = pmoPropertyName;
    }

    @Override
    protected UILabelDefinition getBindingDefinition() {
        return (UILabelDefinition)super.getBindingDefinition();
    }

    @Override
    public AvailableValuesType availableValues() {
        return getBindingDefinition().availableValues();
    }

    @Override
    public String getModelPropertyName() {
        if (StringUtils.isEmpty(getBindingDefinition().modelAttribute())) {
            return getPmoPropertyName();
        }
        return getBindingDefinition().modelAttribute();
    }

    @Override
    public String getModelObjectName() {
        return getBindingDefinition().modelObject();
    }

    @Override
    public ElementBinding createBinding(PropertyDispatcher propertyDispatcher,
            Handler updateUi,
            Component component,
            Label label) {
        requireNonNull(propertyDispatcher, "PropertyDispatcher must not be null");
        requireNonNull(updateUi, "UpdateUI-Handler must not be null");
        requireNonNull(component, "Component must not be null");
        return new LabelBinding(label, (Label)component, propertyDispatcher);
    }

    @Override
    public String getPmoPropertyName() {
        return pmoPropertyName;
    }

    @Override
    public String getLabelText() {
        if (getBindingDefinition().showLabel()) {
            return getBindingDefinition().label();
        } else {
            return "";
        }
    }
}
