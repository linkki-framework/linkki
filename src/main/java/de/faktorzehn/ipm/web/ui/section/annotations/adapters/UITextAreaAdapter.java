package de.faktorzehn.ipm.web.ui.section.annotations.adapters;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;

import de.faktorzehn.ipm.web.ui.section.annotations.AvailableValuesType;
import de.faktorzehn.ipm.web.ui.section.annotations.EnabledType;
import de.faktorzehn.ipm.web.ui.section.annotations.RequiredType;
import de.faktorzehn.ipm.web.ui.section.annotations.UIFieldDefinition;
import de.faktorzehn.ipm.web.ui.section.annotations.UITextArea;
import de.faktorzehn.ipm.web.ui.section.annotations.VisibleType;
import de.faktorzehn.ipm.web.ui.util.ComponentFactory;

public class UITextAreaAdapter implements UIFieldDefinition {

    private final UITextArea uiTextArea;

    public UITextAreaAdapter(UITextArea uiTextArea) {
        this.uiTextArea = uiTextArea;
    }

    @Override
    public Component newComponent() {
        TextArea area = ComponentFactory.newTextArea();
        if (uiTextArea.columns() > 0) {
            area.setColumns(uiTextArea.columns());
        } else {
            area.setWidth("100%");
        }
        if (uiTextArea.maxLength() > 0) {
            area.setMaxLength(uiTextArea.maxLength());
        }
        if (uiTextArea.rows() > 0) {
            area.setRows(uiTextArea.rows());
        }
        return area;
    }

    @Override
    public int position() {
        return uiTextArea.position();
    }

    @Override
    public String label() {
        return uiTextArea.label();
    }

    @Override
    public EnabledType enabled() {
        return uiTextArea.enabled();
    }

    @Override
    public RequiredType required() {
        return uiTextArea.required();
    }

    @Override
    public VisibleType visible() {
        return uiTextArea.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return null;
    }

    @Override
    public String modelAttribute() {
        return uiTextArea.modelAttribute();
    }

    @Override
    public boolean noLabel() {
        return uiTextArea.noLabel();
    }
}