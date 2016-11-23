package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.UITextArea;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;

public class TextAreaBindingDefinition implements UIFieldDefinition {

    private final UITextArea uiTextArea;

    public TextAreaBindingDefinition(UITextArea uiTextArea) {
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
    public String modelAttribute() {
        return uiTextArea.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiTextArea.noLabel();
    }

    @Override
    public String modelObject() {
        return uiTextArea.modelObject();
    }
}