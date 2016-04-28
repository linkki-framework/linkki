/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.dispatcher.BehaviourDependentDispatcher;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BehaviourDependentDispatcherTest {

    @Mock
    private PropertyBehaviorProvider behaviourProvider;
    @Mock
    private PropertyDispatcher wrappedDispatcher;
    private BehaviourDependentDispatcher decorator;

    @Before
    public void setUp() {
        when(behaviourProvider.getBehaviors()).thenReturn(Collections.emptyList());
        when(wrappedDispatcher.isVisible()).thenReturn(true);
        decorator = new BehaviourDependentDispatcher(wrappedDispatcher, behaviourProvider);
    }

    @Test
    public void returnTrueWithNoBehaviors() {
        assertTrue(decorator.isVisible());
    }

    @Test
    public void testNullProvider() {
        decorator = new BehaviourDependentDispatcher(wrappedDispatcher, null);
        assertTrue(decorator.isVisible());
    }

    @Test
    public void testNullList() {
        when(behaviourProvider.getBehaviors()).thenReturn(null);
        assertTrue(decorator.isVisible());
    }
}
