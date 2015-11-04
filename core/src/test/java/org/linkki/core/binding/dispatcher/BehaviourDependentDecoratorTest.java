/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.dispatcher.BehaviourDependentDecorator;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BehaviourDependentDecoratorTest {

    @Mock
    private PropertyBehaviorProvider behaviourProvider;
    @Mock
    private PropertyDispatcher wrappedDispatcher;
    private BehaviourDependentDecorator decorator;

    @Before
    public void setUp() {
        when(behaviourProvider.getBehaviors()).thenReturn(Collections.emptyList());
        when(wrappedDispatcher.isVisible(anyString())).thenReturn(true);
        decorator = new BehaviourDependentDecorator(wrappedDispatcher, behaviourProvider);
    }

    @Test
    public void returnTrueWithNoBehaviors() {
        assertTrue(decorator.isVisible("arbitraryProperty"));
    }

    @Test
    public void testNullProvider() {
        decorator = new BehaviourDependentDecorator(wrappedDispatcher, null);
        assertTrue(decorator.isVisible("arbitraryProperty"));
    }

    @Test
    public void testNullList() {
        when(behaviourProvider.getBehaviors()).thenReturn(null);
        assertTrue(decorator.isVisible("arbitraryProperty"));
    }
}
