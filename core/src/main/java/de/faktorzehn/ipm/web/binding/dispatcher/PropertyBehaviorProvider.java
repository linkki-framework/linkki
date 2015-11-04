/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import java.util.Collection;

import de.faktorzehn.ipm.web.binding.aspect.InjectablePropertyBehavior;

public interface PropertyBehaviorProvider {
    /**
     * Returns all behaviors relevant for the given context.
     */
    public Collection<InjectablePropertyBehavior> getBehaviors();
}
