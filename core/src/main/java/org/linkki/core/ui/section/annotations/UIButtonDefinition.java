/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import com.vaadin.ui.Button;

/**
 * The element definition for the {@link UIButton} annotation.
 */
public interface UIButtonDefinition extends UIElementDefinition {
    public CaptionType captionType();

    public String caption();

    @Override
    public Button newComponent();
}