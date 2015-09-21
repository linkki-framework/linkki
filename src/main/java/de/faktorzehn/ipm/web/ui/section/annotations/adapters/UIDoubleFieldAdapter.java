package de.faktorzehn.ipm.web.ui.section.annotations.adapters;

import com.vaadin.ui.Component;

import de.faktorzehn.ipm.web.ui.components.DoubleField;
import de.faktorzehn.ipm.web.ui.section.annotations.AvailableValuesType;
import de.faktorzehn.ipm.web.ui.section.annotations.EnabledType;
import de.faktorzehn.ipm.web.ui.section.annotations.RequiredType;
import de.faktorzehn.ipm.web.ui.section.annotations.UIDoubleField;
import de.faktorzehn.ipm.web.ui.section.annotations.UIFieldDefinition;
import de.faktorzehn.ipm.web.ui.section.annotations.VisibleType;

public class UIDoubleFieldAdapter implements UIFieldDefinition {

    private final UIDoubleField uiDoubleField;

    public UIDoubleFieldAdapter(UIDoubleField uiDoubleField) {
        this.uiDoubleField = uiDoubleField;
    }

    @Override
    public Component newComponent() {

        DoubleField field = new DoubleField(uiDoubleField.format());
        if (uiDoubleField.maxLength() > 0) {
            field.setMaxLength(uiDoubleField.maxLength());
            field.setColumns(uiDoubleField.maxLength() + 2);
        }

        return field;
    }

    @Override
    public int position() {
        return uiDoubleField.position();
    }

    @Override
    public String label() {
        return uiDoubleField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiDoubleField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiDoubleField.required();
    }

    @Override
    public VisibleType visible() {
        return uiDoubleField.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return null;
    }

    @Override
    public String modelAttribute() {
        return uiDoubleField.modelAttribute();
    }

    @Override
    public boolean noLabel() {
        return uiDoubleField.noLabel();
    }
}