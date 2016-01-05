package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UICustomField;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.VisibleType;

import com.vaadin.ui.Component;

public class UICustomFieldAdapter implements UIFieldDefinition {

    private final UICustomField uiCustomField;

    public UICustomFieldAdapter(UICustomField uiCustomField) {
        this.uiCustomField = uiCustomField;
    }

    @Override
    public Component newComponent() {
        try {
            return uiCustomField.uiControl().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int position() {
        return uiCustomField.position();
    }

    @Override
    public String label() {
        return uiCustomField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiCustomField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiCustomField.required();
    }

    @Override
    public VisibleType visible() {
        return uiCustomField.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return uiCustomField.content();
    }

    @Override
    public String modelAttribute() {
        return uiCustomField.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiCustomField.noLabel();
    }
}