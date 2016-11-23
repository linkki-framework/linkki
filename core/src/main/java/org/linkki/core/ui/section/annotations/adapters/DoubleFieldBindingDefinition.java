package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.ui.components.DoubleField;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIDoubleField;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.UiUtil;

import com.vaadin.ui.Component;

public class DoubleFieldBindingDefinition implements UIFieldDefinition {

    private final UIDoubleField uiDoubleField;

    public DoubleFieldBindingDefinition(UIDoubleField uiDoubleField) {
        this.uiDoubleField = uiDoubleField;
    }

    @Override
    public Component newComponent() {

        DoubleField field = new DoubleField(uiDoubleField.format(), UiUtil.getUiLocale());
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
    public String modelAttribute() {
        return uiDoubleField.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiDoubleField.noLabel();
    }

    @Override
    public String modelObject() {
        return uiDoubleField.modelObject();
    }
}