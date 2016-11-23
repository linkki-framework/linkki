/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.ui.components.DecimalField;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIDecimalField;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.UiUtil;

import com.vaadin.ui.Component;

public class DecimalFieldBindingDefinition implements UIFieldDefinition {

    private final UIDecimalField uiDecimalField;

    public DecimalFieldBindingDefinition(UIDecimalField uiDecimalField) {
        this.uiDecimalField = uiDecimalField;
    }

    @Override
    public Component newComponent() {
        DecimalField field = new DecimalField(uiDecimalField.format(), UiUtil.getUiLocale());
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
    public String modelAttribute() {
        return uiDecimalField.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiDecimalField.noLabel();
    }

    @Override
    public String modelObject() {
        return uiDecimalField.modelObject();
    }
}
