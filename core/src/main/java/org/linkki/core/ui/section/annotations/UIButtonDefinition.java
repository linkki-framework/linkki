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

    /** Defines how the value of caption should be retrieved, using values of {@link EnabledType} */
    public CaptionType captionType();

    /** Text displayed on the button */
    public String caption();

    @Override
    public Button newComponent();
}
