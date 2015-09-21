package de.faktorzehn.ipm.web.ui.section.annotations.adapters;

import com.vaadin.ui.Component;

import de.faktorzehn.ipm.web.ui.section.annotations.AvailableValuesType;
import de.faktorzehn.ipm.web.ui.section.annotations.EnabledType;
import de.faktorzehn.ipm.web.ui.section.annotations.RequiredType;
import de.faktorzehn.ipm.web.ui.section.annotations.UIComboBox;
import de.faktorzehn.ipm.web.ui.section.annotations.UIFieldDefinition;
import de.faktorzehn.ipm.web.ui.section.annotations.VisibleType;
import de.faktorzehn.ipm.web.ui.util.ComponentFactory;

public class UIComboBoxAdapter implements UIFieldDefinition {

    private final UIComboBox uiComboField;

    public UIComboBoxAdapter(UIComboBox uiComboField) {
        this.uiComboField = uiComboField;
    }

    @Override
    public Component newComponent() {
        return ComponentFactory.newCombobox();
    }

    @Override
    public int position() {
        return uiComboField.position();
    }

    @Override
    public String label() {
        return uiComboField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiComboField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiComboField.required();
    }

    @Override
    public VisibleType visible() {
        return uiComboField.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return uiComboField.lov();
    }

    @Override
    public String modelAttribute() {
        return uiComboField.modelAttribute();
    }

    @Override
    public boolean noLabel() {
        return uiComboField.noLabel();
    }

}