/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.UILabel;
import org.linkki.core.ui.section.annotations.VisibleType;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class LabelBindingDefinition implements UIFieldDefinition {

    private UILabel labelAnnotation;

    public LabelBindingDefinition(UILabel labelAnnotation) {
        this.labelAnnotation = requireNonNull(labelAnnotation, "labelAnnotation must not be null");
    }

    @Override
    public EnabledType enabled() {
        return EnabledType.ENABLED;
    }

    @Override
    public VisibleType visible() {
        return labelAnnotation.visible();
    }

    @Override
    public RequiredType required() {
        return RequiredType.NOT_REQUIRED;
    }

    @Override
    public Component newComponent() {
        Label label = new Label();
        label.setContentMode(labelAnnotation.htmlContent() ? ContentMode.HTML : ContentMode.TEXT);
        for (String styleName : labelAnnotation.styleNames()) {
            label.addStyleName(styleName);
        }
        return label;
    }

    @Override
    public int position() {
        return labelAnnotation.position();
    }

    @Override
    public String label() {
        return labelAnnotation.label();
    }

    @Override
    public boolean showLabel() {
        return StringUtils.isNotBlank(labelAnnotation.label());
    }

    @Override
    public String modelObject() {
        return labelAnnotation.modelObject();
    }

    @Override
    public String modelAttribute() {
        return labelAnnotation.modelAttribute();
    }

}
