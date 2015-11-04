/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding;

import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;

/**
 * Binds an element to a control using the property dispatchers.
 *
 */
public interface ElementBinding extends Binding {

    PropertyDispatcher getPropertyDispatcher();

}
