package org.linkki.core.ui.section.annotations.adapters;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.ui.components.ItemCaptionProvider;
import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIYesNoComboBox;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

/**
 * {@link BindingDefinition} for {@link UIYesNoComboBox}.
 */
public class YesNoComboBoxBindingDefinition implements BindingDefinition {

    private final UIYesNoComboBox uiYesNoComboBox;

    public YesNoComboBoxBindingDefinition(UIYesNoComboBox uiYesNoComboBox) {
        this.uiYesNoComboBox = requireNonNull(uiYesNoComboBox, "uiYesNoComboBox must not be null"); //$NON-NLS-1$
    }

    @Override
    public Component newComponent() {
        ComboBox<Object> comboBox = ComponentFactory.newComboBox();
        comboBox.setItemCaptionGenerator(getItemCaptionProvider()::getUnsafeCaption);
        comboBox.setEmptySelectionAllowed(false);
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
    public String modelAttribute() {
        return uiYesNoComboBox.modelAttribute();
    }

    @SuppressWarnings("unchecked")
    private ItemCaptionProvider<Object> getItemCaptionProvider() {
        try {
            return (ItemCaptionProvider<Object>)uiYesNoComboBox.itemCaptionProvider().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LinkkiBindingException(
                    "Cannot instantiate item caption provider " + uiYesNoComboBox.itemCaptionProvider().getName()
                            + " using default constructor.",
                    e);
        }
    }

    @Override
    public String modelObject() {
        return uiYesNoComboBox.modelObject();
    }

}