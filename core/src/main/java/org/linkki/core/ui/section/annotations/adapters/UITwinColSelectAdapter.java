package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.UITwinColSelect;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.TwinColSelect;

public class UITwinColSelectAdapter implements UIFieldDefinition {

    private final UITwinColSelect uiTwinColSelect;

    public UITwinColSelectAdapter(UITwinColSelect uiTwinColSelect) {
        this.uiTwinColSelect = uiTwinColSelect;
    }

    @Override
    public TwinColSelect newComponent() {
        return ComponentFactory.newTwinColSelect();
    }

    @Override
    public int position() {
        return uiTwinColSelect.position();
    }

    @Override
    public String label() {
        return uiTwinColSelect.label();
    }

    @Override
    public EnabledType enabled() {
        return uiTwinColSelect.enabled();
    }

    @Override
    public RequiredType required() {
        return uiTwinColSelect.required();
    }

    @Override
    public VisibleType visible() {
        return uiTwinColSelect.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return uiTwinColSelect.content();
    }

    @Override
    public String modelAttribute() {
        return uiTwinColSelect.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiTwinColSelect.noLabel();
    }

}