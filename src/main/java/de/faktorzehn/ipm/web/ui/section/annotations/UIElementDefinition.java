/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section.annotations;

import com.vaadin.ui.Component;

/**
 * A common interface for all annotations describing UI elements. As annotations can't implement an
 * interface, the {@link UIAnnotationReader} is used to get definition instances for the annotated
 * methods of a class. The {@link UIElementDefinitionRegistry} is used to link the annotations to
 * their definition implementations.
 * 
 * @see UIAnnotationReader
 * @see UIElementDefinitionRegistry
 */
public interface UIElementDefinition {

    Component newComponent();

    int position();

    String label();

    boolean showLabel();

    EnabledType enabled();

    VisibleType visible();
}
