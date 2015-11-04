/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import java.util.Collection;

import org.linkki.core.binding.aspect.InjectablePropertyBehavior;

public interface PropertyBehaviorProvider {
    /**
     * Returns all behaviors relevant for the given context.
     */
    public Collection<InjectablePropertyBehavior> getBehaviors();
}
