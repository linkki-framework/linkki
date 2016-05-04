/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import com.vaadin.ui.Component;

/**
 * Holds information about a bound UI element (such as the settings for visibility, enabled-state
 * etc.) and on how to create and display such an UI element.
 */
public interface ElementDescriptor extends BindingDescriptor {

    /** The position of the UI element in its parent/container. */
    int getPosition();

    /** The text for the UI element's label. */
    String getLabelText();

    /** Creates a new Vaadin UI component for this UI element. */
    Component newComponent();

}
