/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import java.util.Collection;
import java.util.Collections;

import org.linkki.core.binding.aspect.InjectablePropertyBehavior;

@FunctionalInterface
public interface PropertyBehaviorProvider {

    public static final PropertyBehaviorProvider NO_BEHAVIOR_PROVIDER = () -> Collections.emptyList();

    /**
     * Returns all behaviors relevant for the given context.
     */
    public Collection<InjectablePropertyBehavior> getBehaviors();
}
