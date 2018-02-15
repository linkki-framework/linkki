/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.binding.dispatcher;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.behavior.PropertyBehavior;
import org.linkki.core.ui.section.annotations.aspect.FieldValueAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.VisibleAspectDefinition;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@SuppressWarnings("null")
@RunWith(MockitoJUnitRunner.class)
public class BehaviorDependentDispatcherTest {

    @Mock
    private PropertyBehaviorProvider behaviourProvider;
    @Mock
    private PropertyDispatcher wrappedDispatcher;
    private BehaviorDependentDispatcher behaviorDispatcher;

    @Before
    public void setUp() {
        when(behaviourProvider.getBehaviors()).thenReturn(Collections.emptyList());
        when(wrappedDispatcher.getAspectValue(Mockito.any())).thenReturn(true);
        when(wrappedDispatcher.isWritable(Mockito.any())).thenReturn(true);
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher, behaviourProvider);
    }

    @Test
    public void testReturnTrueWithNoBehaviors() {
        assertThat(behaviorDispatcher.getAspectValue(Aspect.newDynamic(VisibleAspectDefinition.NAME)), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void testNullProvider() {
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullList() {
        when(behaviourProvider.getBehaviors()).thenReturn(null);
        behaviorDispatcher.getAspectValue(Aspect.newDynamic(VisibleAspectDefinition.NAME));
    }

    @Test
    public void testNonConsensus_visible() {
        PropertyBehavior nonVisibleBehavior = new PropertyBehavior() {
            @Override
            public boolean isVisible(Object boundObject, String property) {
                return false;
            }
        };

        when(behaviourProvider.getBehaviors()).thenReturn(Arrays.asList(nonVisibleBehavior));
        when(behaviorDispatcher.getBoundObject()).thenReturn(mock(Object.class));
        assertThat(behaviorDispatcher.getAspectValue(Aspect.newDynamic(VisibleAspectDefinition.NAME)), is(false));
    }

    @Test
    public void testNonConsensus_writable() {
        PropertyBehavior nonWritableBehavior = new PropertyBehavior() {
            @Override
            public boolean isWritable(Object boundObject, String property) {
                return false;
            }
        };

        when(behaviourProvider.getBehaviors()).thenReturn(Arrays.asList(nonWritableBehavior));
        when(behaviorDispatcher.getBoundObject()).thenReturn(mock(Object.class));
        assertThat(behaviorDispatcher.isWritable(Aspect.newDynamic(FieldValueAspectDefinition.NAME)), is(false));
    }
}
