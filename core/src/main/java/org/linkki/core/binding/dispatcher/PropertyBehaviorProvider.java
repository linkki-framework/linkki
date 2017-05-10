/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import java.util.Collection;
import java.util.Collections;

import org.linkki.core.binding.aspect.PropertyBehavior;

@FunctionalInterface
public interface PropertyBehaviorProvider {

    /**
     * A provider that provides no additional / special behavior.
     */
    PropertyBehaviorProvider NO_BEHAVIOR_PROVIDER = Collections::emptyList;

    /**
     * Returns all behaviors relevant for the given context.
     */
    Collection<PropertyBehavior> getBehaviors();
}
