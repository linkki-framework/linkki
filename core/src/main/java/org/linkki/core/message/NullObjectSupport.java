/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.message;

/**
 * This interface marks a class as supporting the null object pattern. Instances of this class are
 * either a "normal" object or the null object.
 * 
 * @see NullObject
 * 
 * @author Jan Ortmann
 */
public interface NullObjectSupport {

    /**
     * Returns <code>true</code> if this is the object representing <code>null</code>, otherwise
     * <code>false</code>.
     */
    public boolean isNull();

    /**
     * Returns <code>false</code> if this is the object representing <code>null</code>, otherwise
     * <code>true</code>.
     */
    public boolean isNotNull();
}
