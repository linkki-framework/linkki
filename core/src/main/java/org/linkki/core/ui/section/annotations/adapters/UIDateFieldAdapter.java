package org.linkki.core.ui.section.annotations.adapters;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;
import org.linkki.core.ui.util.UiUtil;
import org.linkki.util.DateFormatRegistry;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;

public class UIDateFieldAdapter implements UIFieldDefinition {

    private final UIDateField uiDateField;
    private final DateFormatRegistry dateFormatRegistry = new DateFormatRegistry();

    public UIDateFieldAdapter(UIDateField uiDateField) {
        this.uiDateField = uiDateField;
    }

    @Override
    public Component newComponent() {
        DateField dateField = ComponentFactory.newDateField();
        if (StringUtils.isNotBlank(uiDateField.dateFormat())) {
            dateField.setDateFormat(uiDateField.dateFormat());
        } else {
            Locale locale = UiUtil.getUiLocale();
            dateField.setDateFormat(dateFormatRegistry.getPattern(locale));
        }
        return dateField;
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
    public String modelAttribute() {
        return uiDateField.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiDateField.noLabel();
    }

    @Override
    public String modelObject() {
        return uiDateField.modelObject();
    }
}