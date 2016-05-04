/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import org.linkki.core.binding.dispatcher.PropertyDispatcher;

/**
 * Binds an element to a control using the property dispatchers.
 *
 */
public interface ElementBinding extends Binding {

    PropertyDispatcher getPropertyDispatcher();

    @Override
    default Object getPmo() {
        return getPropertyDispatcher().getBoundObject();
    }

}
