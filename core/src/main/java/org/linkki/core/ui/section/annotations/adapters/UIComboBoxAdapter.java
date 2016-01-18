package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Component;

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
        return uiComboField.content();
    }

    @Override
    public String modelAttribute() {
        return uiComboField.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiComboField.noLabel();
    }

}