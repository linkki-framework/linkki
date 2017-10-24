package org.linkki.core.ui.section.annotations.adapters;

import static java.util.Objects.requireNonNull;

import org.linkki.core.exception.LinkkiRuntimeException;
import org.linkki.core.ui.components.ItemCaptionProvider;
import org.linkki.core.ui.components.LinkkiComboBox;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.UIYesNoComboBox;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Component;

public class YesNoComboBoxBindingDefinition implements UIFieldDefinition {

    private final UIYesNoComboBox uiYesNoComboBox;

    public YesNoComboBoxBindingDefinition(UIYesNoComboBox uiYesNoComboBox) {
        this.uiYesNoComboBox = requireNonNull(uiYesNoComboBox, "uiYesNoComboBox must not be null"); //$NON-NLS-1$
    }

    @Override
    public Component newComponent() {
        LinkkiComboBox comboBox = ComponentFactory.newComboBox();
        comboBox.setItemCaptionProvider(getItemCaptionProvider());
        comboBox.setNullSelectionAllowed(false);
        comboBox.setWidth(uiYesNoComboBox.width());
        return comboBox;
    }

    @Override
    public int position() {
        return uiYesNoComboBox.position();
    }

    @Override
    public String label() {
        return uiYesNoComboBox.label();
    }

    @Override
    public EnabledType enabled() {
        return uiYesNoComboBox.enabled();
    }

    @Override
    public RequiredType required() {
        return uiYesNoComboBox.required();
    }

    @Override
    public VisibleType visible() {
        return uiYesNoComboBox.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return AvailableValuesType.ENUM_VALUES_INCL_NULL;
    }

    @Override
    public String modelAttribute() {
        return uiYesNoComboBox.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiYesNoComboBox.noLabel();
    }

    private ItemCaptionProvider<?> getItemCaptionProvider() {
        try {
            return uiYesNoComboBox.itemCaptionProvider().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LinkkiRuntimeException(e);
        }
    }

    @Override
    public String modelObject() {
        return uiYesNoComboBox.modelObject();
    }

}