/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

/**
 * An observer that can be attached to a {@link BindingManager} and is notified if an update UI
 * event is triggered by a managed {@link BindingContext}. Updates its own UI upon notification.
 * 
 * @see BindingManager#addUiUpdateObserver(UiUpdateObserver)
 */
public interface UiUpdateObserver {

    /**
     * Called by {@link BindingManager} when UI updates are triggered (e.g. by managed
     * {@link BindingContext}s).
     */
    void uiUpdated();
}
