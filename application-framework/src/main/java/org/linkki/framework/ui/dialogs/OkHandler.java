/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import javax.annotation.Nonnull;

import org.linkki.util.handler.Handler;

@FunctionalInterface
public interface OkHandler extends Handler {

    /** A handler that does nothing. */
    OkHandler NOP_HANDLER = () -> {
        // nothing to do
    };

    @Override
    default void apply() {
        onOk();
    }

    /**
     * Called when the user clicks OK.
     */
    void onOk();

    /**
     * Returns a composed handler that first executes this handler {@code onOk()} method and then
     * the {@code onOk()} method of the given handler.
     */
    @Override
    default OkHandler andThen(@Nonnull Handler after) {
        return () -> {
            apply();
            after.apply();
        };
    }

}