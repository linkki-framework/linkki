/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.nls.pmo;

/**
 * Derives a bundle name for use with a {@link PmoNlsService} from a presentation model object
 * class.
 */
@FunctionalInterface
public interface PmoBundleNameGenerator {

    /**
     * @return the bundle name to be used for lookup of texts used in the given class
     */
    public String getBundleName(Class<?> pmoClass);
}
