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
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@SuppressWarnings("null")
@RunWith(MockitoJUnitRunner.class)
public class BehaviorDependentDispatcherTest {

    @Mock
    private PropertyBehaviorProvider behaviourProvider;
    @Mock
    private PropertyDispatcher wrappedDispatcher;
    private BehaviorDependentDispatcher decorator;

    @Before
    public void setUp() {
        when(behaviourProvider.getBehaviors()).thenReturn(Collections.emptyList());
        when(wrappedDispatcher.isVisible()).thenReturn(true);
        decorator = new BehaviorDependentDispatcher(wrappedDispatcher, behaviourProvider);
    }

    @Test
    public void returnTrueWithNoBehaviors() {
        assertTrue(decorator.isVisible());
    }

    @Test(expected = NullPointerException.class)
    public void testNullProvider() {
        decorator = new BehaviorDependentDispatcher(wrappedDispatcher, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullList() {
        when(behaviourProvider.getBehaviors()).thenReturn(null);
        decorator.isVisible();
    }

    @Test
    public void testEmptyList() {
        when(behaviourProvider.getBehaviors()).thenReturn(Collections.emptyList());
        assertTrue(decorator.isVisible());
    }
}
