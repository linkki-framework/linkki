/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface OkHandler {

    /** A handler that does nothing. */
    OkHandler NOP_HANDLER = () -> {
        // nothing to do
    };

    void onOk();

    /**
     * Returns a composed handler that first executes this handler {@code onOk()} method and then
     * the {@code onOk()} method of the given handler.
     */
    default OkHandler andThen(@Nonnull OkHandler after) {
        requireNonNull(after);
        return () -> {
            onOk();
            after.onOk();
        };
    }

}