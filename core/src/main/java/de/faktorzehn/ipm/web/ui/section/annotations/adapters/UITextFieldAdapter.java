package de.faktorzehn.ipm.web.ui.section.annotations.adapters;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

import de.faktorzehn.ipm.web.ui.section.annotations.AvailableValuesType;
import de.faktorzehn.ipm.web.ui.section.annotations.EnabledType;
import de.faktorzehn.ipm.web.ui.section.annotations.RequiredType;
import de.faktorzehn.ipm.web.ui.section.annotations.UIFieldDefinition;
import de.faktorzehn.ipm.web.ui.section.annotations.UITextField;
import de.faktorzehn.ipm.web.ui.section.annotations.VisibleType;
import de.faktorzehn.ipm.web.ui.util.ComponentFactory;

public class UITextFieldAdapter implements UIFieldDefinition {

    private final UITextField uiTextField;

    public UITextFieldAdapter(UITextField uiTextField) {
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
    public AvailableValuesType availableValues() {
        return null;
    }

    @Override
    public String modelAttribute() {
        return uiTextField.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiTextField.noLabel();
    }
}