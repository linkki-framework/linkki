/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.VisibleType;

public class BindAnnotationAdapter implements BindingDefinition {

    private final Bind bindAnnotation;

    public BindAnnotationAdapter(Bind annotation) {
        super();
        this.bindAnnotation = annotation;
    }

    @Override
    public EnabledType enabled() {
        return bindAnnotation.enabled();
    }

    @Override
    public VisibleType visible() {
        return bindAnnotation.visible();
    }

    @Override
    public RequiredType required() {
        return bindAnnotation.required();
    }

    @Override
    public AvailableValuesType availableValues() {
        return bindAnnotation.availableValues();
    }

    public String getPropertyName() {
        return bindAnnotation.pmoProperty();
    }

}
