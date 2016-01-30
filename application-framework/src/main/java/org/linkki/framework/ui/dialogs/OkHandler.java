/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

@FunctionalInterface
public interface OkHandler {

    /** A handler that does nothing. */
    OkHandler NOP_HANDLER = () -> {
        // nothing to do
    };

    void onOk();

}