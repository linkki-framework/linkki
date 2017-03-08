package org.linkki.core.ui.section.annotations.adapters;

import static java.util.Objects.requireNonNull;

import org.linkki.core.ui.components.IntegerField;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.UIIntegerField;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.UiUtil;

import com.vaadin.ui.Component;

public class IntegerFieldBindingDefinition implements UIFieldDefinition {

    private final UIIntegerField uiIntegerField;

    public IntegerFieldBindingDefinition(UIIntegerField uiIntegerField) {
        this.uiIntegerField = requireNonNull(uiIntegerField, "uiIntegerField must not be null");
    }

    @Override
    public Component newComponent() {
        IntegerField field = new IntegerField(uiIntegerField.format(), UiUtil.getUiLocale());
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
    public String modelAttribute() {
        return uiIntegerField.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiIntegerField.noLabel();
    }

    @Override
    public String modelObject() {
        return uiIntegerField.modelObject();
    }
}