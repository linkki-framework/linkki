/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import com.vaadin.ui.Component;

/**
 * A common interface for annotations that are used to create and bind UI elements in a view
 * generated from an annotated PMO.
 * <p>
 * As annotations can't implement an interface, the {@link UIAnnotationReader} is used to get
 * definition instances for the annotated methods of a (PMO) class. The
 * {@link UIElementDefinitionRegistry} is used to link the annotations to their definition
 * implementations.
 * 
 * @see UIAnnotationReader
 * @see UIElementDefinitionRegistry
 */
public interface UIElementDefinition extends BindingDefinition {

    Component newComponent();

    int position();

    String label();

    boolean showLabel();

}
