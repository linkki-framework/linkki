package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.exception.LinkkiRuntimeException;
import org.linkki.core.ui.components.ItemCaptionProvider;
import org.linkki.core.ui.components.LinkkiComboBox;
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
        LinkkiComboBox comboBox = ComponentFactory.newComboBox();
        comboBox.setItemCaptionProvider(getItemCaptionProvider());
        comboBox.setNullSelectionAllowed(false);
        comboBox.setWidth(uiComboField.width());
        return comboBox;
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

    private ItemCaptionProvider<?> getItemCaptionProvider() {
        try {
            return uiComboField.itemCaptionProvider().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LinkkiRuntimeException(e);
        }
    }

    @Override
    public String modelObject() {
        return uiComboField.modelObject();
    }

}