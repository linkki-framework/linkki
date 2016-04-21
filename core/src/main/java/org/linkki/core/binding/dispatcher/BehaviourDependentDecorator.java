/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

/**
 * @deprecated Replaced by {@link BehaviourDependentDispatcher}
 */
@Deprecated
public class BehaviourDependentDecorator extends BehaviourDependentDispatcher {

    public BehaviourDependentDecorator(PropertyDispatcher wrappedDispatcher, PropertyBehaviorProvider provider) {
        super(wrappedDispatcher, provider);
    }
}
