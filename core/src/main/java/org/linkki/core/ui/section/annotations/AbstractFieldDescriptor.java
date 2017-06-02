/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.StringUtils;

/**
 * Holds all information about a field, which are the property name as well as the settings for
 * visibility, enabled-state etc. The given property name is only used as fallback if there is
 * {@link UIFieldDefinition#modelAttribute()} is not set.
 */
public abstract class AbstractFieldDescriptor extends ElementDescriptor {

    private final String pmoPropertyName;

    /**
     * Constructs a new field description.
     *
     * @param fieldDef field definition that holds given annotated properties
     * @param toolTipDefinition text and type of the tooltip
     * @param pmoPropertyName name of the corresponding method in the PMO
     * @param pmoClass presentation model object class type
     */
    public AbstractFieldDescriptor(UIFieldDefinition fieldDef, UIToolTipDefinition toolTipDefinition,
            String pmoPropertyName, Class<?> pmoClass) {
        super(fieldDef, toolTipDefinition, pmoClass);
        this.pmoPropertyName = requireNonNull(pmoPropertyName, "pmoPropertyName must not be null");
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
    public String getPmoPropertyName() {
        return pmoPropertyName;
    }

    @Override
    public String toString() {
        return "FieldDescriptor [getBindingDefinition()=" + getBindingDefinition() + ", fallbackPropertyName="
                + getPmoPropertyName()
                + "]";
    }
}
