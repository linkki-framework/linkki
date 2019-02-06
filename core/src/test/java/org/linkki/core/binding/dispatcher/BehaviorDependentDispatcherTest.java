/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.binding.dispatcher;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.linkki.core.matcher.MessageMatchers.emptyMessageList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.behavior.PropertyBehavior;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.core.message.ObjectProperty;
import org.linkki.core.message.Severity;
import org.linkki.core.ui.section.annotations.aspect.VisibleAspectDefinition;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@SuppressWarnings("null")
@RunWith(MockitoJUnitRunner.class)
public class BehaviorDependentDispatcherTest {

    @Mock
    private PropertyBehaviorProvider behaviorProvider;
    @Mock
    private PropertyDispatcher wrappedDispatcher;
    private BehaviorDependentDispatcher behaviorDispatcher;

    @Before
    public void setUp() {
        when(behaviorProvider.getBehaviors()).thenReturn(Collections.emptyList());
        when(wrappedDispatcher.pull(Mockito.any())).thenReturn(true);
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher, behaviorProvider);
    }

    @Test
    public void testReturnTrueWithNoBehaviors() {
        assertThat(behaviorDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME)), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void testNullProvider() {
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullList() {
        when(behaviorProvider.getBehaviors()).thenReturn(null);
        behaviorDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME));
    }

    @Test(expected = NullPointerException.class)
    public void testNullBoundObject_visible() {
        when(behaviorProvider.getBehaviors()).thenReturn(Arrays.asList(PropertyBehavior.visible(() -> false)));
        behaviorDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME));
    }

    @Test(expected = NullPointerException.class)
    public void testNullBoundObject_writable() {
        when(behaviorProvider.getBehaviors()).thenReturn(Arrays.asList(PropertyBehavior.writable(() -> false)));
        behaviorDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME));
    }

    @Test(expected = NullPointerException.class)
    public void testNullBoundObject_messages() {
        when(behaviorProvider.getBehaviors())
                .thenReturn(Arrays.asList(PropertyBehavior.showValidationMessages(() -> false)));
        behaviorDispatcher.getMessages(new MessageList());
    }

    @Test
    public void testNonConsensus_visible() {
        when(behaviorProvider.getBehaviors()).thenReturn(Arrays.asList(PropertyBehavior.visible(() -> false)));
        when(behaviorDispatcher.getBoundObject()).thenReturn(mock(Object.class));
        assertThat(behaviorDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME)), is(false));
    }

    @Test
    public void testNonConsensus_writable() {
        when(behaviorProvider.getBehaviors()).thenReturn(Arrays.asList(PropertyBehavior.writable(() -> false)));
        when(behaviorDispatcher.getBoundObject()).thenReturn(mock(Object.class));
        assertThat(behaviorDispatcher.isPushable(Aspect.of(LinkkiAspectDefinition.VALUE_ASPECT_NAME)), is(false));
    }

    @Test
    public void testNonConsensus_messages() {
        when(behaviorProvider.getBehaviors())
                .thenReturn(Arrays.asList(PropertyBehavior.showValidationMessages(() -> false)));
        Object boundObject = mock(Object.class);
        when(behaviorDispatcher.getBoundObject()).thenReturn(boundObject);
        MessageList messageList = new MessageList(Message.builder("Foo", Severity.ERROR)
                .invalidObject(new ObjectProperty(boundObject, "prop")).code("4711").create());
        assertThat(behaviorDispatcher.getMessages(messageList), is(emptyMessageList()));
    }
}
