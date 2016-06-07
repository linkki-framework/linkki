/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UILabel;
import org.linkki.core.ui.section.annotations.UILabelDefinition;
import org.linkki.core.ui.section.annotations.VisibleType;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class UILabelAdapter implements UILabelDefinition {

    private UILabel labelAnnotation;

    public UILabelAdapter(UILabel labelAnnotation) {
        this.labelAnnotation = labelAnnotation;
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
        return new Label();
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
