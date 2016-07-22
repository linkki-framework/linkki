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

public class LabelDescriptor implements ElementDescriptor {

    private final UILabelDefinition labelDefinition;
    private final String pmoPropertyName;
    private final UIToolTipDefinition toolTipDefinition;

    public LabelDescriptor(UILabelDefinition labelDefinition, UIToolTipDefinition toolTipDefinition,
            String pmoPropertyName) {
        this.labelDefinition = labelDefinition;
        this.toolTipDefinition = toolTipDefinition;
        this.pmoPropertyName = pmoPropertyName;
    }

    @Override
    public String getModelPropertyName() {
        if (StringUtils.isEmpty(labelDefinition.modelAttribute())) {
            return getPmoPropertyName();
        }
        return labelDefinition.modelAttribute();
    }

    @Override
    public EnabledType enabled() {
        return labelDefinition.enabled();
    }

    @Override
    public VisibleType visible() {
        return labelDefinition.visible();
    }

    @Override
    public RequiredType required() {
        return labelDefinition.required();
    }

    @Override
    public AvailableValuesType availableValues() {
        return labelDefinition.availableValues();
    }

    @Override
    public String getModelObjectName() {
        return labelDefinition.modelObject();
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
    public int getPosition() {
        return labelDefinition.position();
    }

    @Override
    public String getLabelText() {
        if (labelDefinition.showLabel()) {
            return labelDefinition.label();
        } else {
            return "";
        }
    }

    @Override
    public Component newComponent() {
        return labelDefinition.newComponent();
    }

    @Override
    public String getPmoPropertyName() {
        return pmoPropertyName;
    }

    @Override
    public String getToolTip() {
        return toolTipDefinition.text();
    }

    @Override
    public ToolTipType getToolTipType() {
        return toolTipDefinition.toolTipType();
    }
}
