package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

public class TextFieldBindingDefinition implements UIFieldDefinition {

    private final UITextField uiTextField;

    public TextFieldBindingDefinition(UITextField uiTextField) {
        this.uiTextField = uiTextField;
    }

    @Override
    public Component newComponent() {
        TextField field = ComponentFactory.newTextfield();
        if (uiTextField.columns() > 0) {
            field.setColumns(uiTextField.columns());
        } else {
            field.setWidth("100%");
        }
        if (uiTextField.maxLength() > 0) {
            field.setMaxLength(uiTextField.maxLength());
        }
        return field;
    }

    @Override
    public int position() {
        return uiTextField.position();
    }

    @Override
    public String label() {
        return uiTextField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiTextField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiTextField.required();
    }

    @Override
    public VisibleType visible() {
        return uiTextField.visible();
    }

    @Override
    public String modelAttribute() {
        return uiTextField.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiTextField.noLabel();
    }

    @Override
    public String modelObject() {
        return uiTextField.modelObject();
    }
}