package de.faktorzehn.ipm.web.ui.section.annotations.adapters;

import com.vaadin.ui.Component;

import de.faktorzehn.ipm.web.ui.section.annotations.AvailableValuesType;
import de.faktorzehn.ipm.web.ui.section.annotations.EnabledType;
import de.faktorzehn.ipm.web.ui.section.annotations.RequiredType;
import de.faktorzehn.ipm.web.ui.section.annotations.UICheckBox;
import de.faktorzehn.ipm.web.ui.section.annotations.UIFieldDefinition;
import de.faktorzehn.ipm.web.ui.section.annotations.VisibleType;
import de.faktorzehn.ipm.web.ui.util.ComponentFactory;

public class UICheckBoxAdpater implements UIFieldDefinition {

    private final UICheckBox uiCheckBox;

    public UICheckBoxAdpater(UICheckBox uiCheckBox) {
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

}