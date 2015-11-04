package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UICheckBox;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Component;

public class UICheckBoxAdapter implements UIFieldDefinition {

    private final UICheckBox uiCheckBox;

    public UICheckBoxAdapter(UICheckBox uiCheckBox) {
        this.uiCheckBox = uiCheckBox;
    }

    @Override
    public Component newComponent() {
        return ComponentFactory.newCheckbox();
    }

    @Override
    public int position() {
        return uiCheckBox.position();
    }

    @Override
    public String label() {
        return uiCheckBox.label();
    }

    @Override
    public EnabledType enabled() {
        return uiCheckBox.enabled();
    }

    @Override
    public RequiredType required() {
        return uiCheckBox.required();
    }

    @Override
    public VisibleType visible() {
        return uiCheckBox.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return null;
    }

    @Override
    public String modelAttribute() {
        return uiCheckBox.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiCheckBox.noLabel();
    }

}