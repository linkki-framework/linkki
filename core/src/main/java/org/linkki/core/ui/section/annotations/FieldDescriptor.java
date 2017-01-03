/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.FieldBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Holds all information about a field, which are the property name as well as the settings for
 * visibility, enabled-state etc. The given property name is only used as fallback if there is
 * {@link UIFieldDefinition#modelAttribute()} is not set.
 */
public class FieldDescriptor extends ElementDescriptor {

    private final String pmoPropertyName;

    /**
     * Constructs a new field description.
     *
     * @param fieldDef field definition that holds given annotated properties
     * @param toolTipDefinition text and type of the tooltip
     * @param pmoPropertyName name of the corresponding method in the PMO
     */
    public FieldDescriptor(UIFieldDefinition fieldDef, UIToolTipDefinition toolTipDefinition, String pmoPropertyName) {
        super(fieldDef, toolTipDefinition);
        this.pmoPropertyName = pmoPropertyName;
    }

    @Override
    protected UIFieldDefinition getBindingDefinition() {
        return (UIFieldDefinition)super.getBindingDefinition();
    }

    /**
     * Property derived from the "modelAttribute" property defined by the annotation. If no
     * "modelAttribute" exists, derives the property name from the name of the annotated method.
     */
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
    public FieldBinding<?> createBinding(PropertyDispatcher propertyDispatcher,
            Handler updateUi,
            Component component,
            Label label) {
        requireNonNull(propertyDispatcher, "PropertyDispatcher must not be null");
        requireNonNull(updateUi, "UpdateUI-Handler must not be null");
        requireNonNull(component, "Component must not be null");
        return new FieldBinding<>(label, (AbstractField<?>)component, propertyDispatcher, updateUi);
    }

    @Override
    public String getPmoPropertyName() {
        return pmoPropertyName;
    }

    /**
     * Derives the label from the label defined in the annotation. If no label is defined, derives
     * the label from the property name.
     */
    @Override
    public String getLabelText() {
        if (!getBindingDefinition().showLabel()) {
            return "";
        }

        String label = getBindingDefinition().label();
        if (StringUtils.isEmpty(label)) {
            label = StringUtils.capitalize(getModelPropertyName());
        }
        return label;
    }

    @Override
    public String toString() {
        return "FieldDescriptor [getBindingDefinition()=" + getBindingDefinition() + ", fallbackPropertyName="
                + getPmoPropertyName()
                + "]";
    }
}
