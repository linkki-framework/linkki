/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

/**
 * An observer that can be registered at a {@link BindingManager} and is notified if an update UI
 * event is triggered by a managed {@link BindingContext}. Updates own UI upon notification.
 * 
 * @see BindingManager#registerUiUpdateObserver(UiUpdateObserver)
 */
public interface UiUpdateObserver {

    /**
     * Called by {@link BindingManager} when UI updates are triggered by {@link BindingContext}s.
     */
    void updateUIBindings();
}
