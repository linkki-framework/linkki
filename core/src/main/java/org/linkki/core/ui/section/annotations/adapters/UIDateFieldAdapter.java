package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Component;

public class UIDateFieldAdapter implements UIFieldDefinition {

    private final UIDateField uiDateField;

    public UIDateFieldAdapter(UIDateField uiDateField) {
        this.uiDateField = uiDateField;
    }

    @Override
    public Component newComponent() {
        return ComponentFactory.newDateField();
    }

    @Override
    public int position() {
        return uiDateField.position();
    }

    @Override
    public String label() {
        return uiDateField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiDateField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiDateField.required();
    }

    @Override
    public VisibleType visible() {
        return uiDateField.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return null;
    }

    @Override
    public String modelAttribute() {
        return uiDateField.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiDateField.noLabel();
    }
}