/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section.annotations.adapters;

import com.vaadin.ui.Component;

import de.faktorzehn.ipm.web.ui.components.DecimalField;
import de.faktorzehn.ipm.web.ui.section.annotations.AvailableValuesType;
import de.faktorzehn.ipm.web.ui.section.annotations.EnabledType;
import de.faktorzehn.ipm.web.ui.section.annotations.RequiredType;
import de.faktorzehn.ipm.web.ui.section.annotations.UIDecimalField;
import de.faktorzehn.ipm.web.ui.section.annotations.UIFieldDefinition;
import de.faktorzehn.ipm.web.ui.section.annotations.VisibleType;

public class UIDecimalFieldAdapter implements UIFieldDefinition {

    private final UIDecimalField uiDecimalField;

    public UIDecimalFieldAdapter(UIDecimalField uiDecimalField) {
        this.uiDecimalField = uiDecimalField;
    }

    @Override
    public Component newComponent() {
        DecimalField field = new DecimalField(uiDecimalField.format());
        if (uiDecimalField.maxLength() > 0) {
            field.setMaxLength(uiDecimalField.maxLength());
            field.setColumns(uiDecimalField.maxLength() + 2);
        }

        return field;
    }

    @Override
    public int position() {
        return uiDecimalField.position();
    }

    @Override
    public String label() {
        return uiDecimalField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiDecimalField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiDecimalField.required();
    }

    @Override
    public VisibleType visible() {
        return uiDecimalField.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return null;
    }

    @Override
    public String modelAttribute() {
        return uiDecimalField.modelAttribute();
    }

    @Override
    public boolean noLabel() {
        return uiDecimalField.noLabel();
    }
}
