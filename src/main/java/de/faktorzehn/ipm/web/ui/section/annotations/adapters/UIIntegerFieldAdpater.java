package de.faktorzehn.ipm.web.ui.section.annotations.adapters;

import com.vaadin.ui.Component;

import de.faktorzehn.ipm.web.ui.components.IntegerField;
import de.faktorzehn.ipm.web.ui.section.annotations.AvailableValuesType;
import de.faktorzehn.ipm.web.ui.section.annotations.EnabledType;
import de.faktorzehn.ipm.web.ui.section.annotations.RequiredType;
import de.faktorzehn.ipm.web.ui.section.annotations.UIFieldDefinition;
import de.faktorzehn.ipm.web.ui.section.annotations.UIIntegerField;
import de.faktorzehn.ipm.web.ui.section.annotations.VisibleType;

public class UIIntegerFieldAdpater implements UIFieldDefinition {

    private final UIIntegerField uiIntegerField;

    public UIIntegerFieldAdpater(UIIntegerField uiIntegerField) {
        this.uiIntegerField = uiIntegerField;
    }

    @Override
    public Component newComponent() {
        IntegerField field = new IntegerField(uiIntegerField.format());
        if (uiIntegerField.maxLength() > 0) {
            field.setMaxLength(uiIntegerField.maxLength());
            field.setColumns(uiIntegerField.maxLength() + 2);
        }
        return field;
    }

    @Override
    public int position() {
        return uiIntegerField.position();
    }

    @Override
    public String label() {
        return uiIntegerField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiIntegerField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiIntegerField.required();
    }

    @Override
    public VisibleType visible() {
        return uiIntegerField.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return null;
    }

    @Override
    public String modelAttribute() {
        return uiIntegerField.modelAttribute();
    }
}