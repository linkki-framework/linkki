/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.core.cdi;

/**
 * Provider for objects that cannot be injected directly, e.g. because they are obtained using
 * static utility methods and the like.
 */
public interface IpmProvider<T> {

    /**
     * Returns the value this provider provides.
     */
    public T get();

}
