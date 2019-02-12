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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.behavior.PropertyBehavior;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.core.message.ObjectProperty;
import org.linkki.core.message.Severity;
import org.linkki.core.ui.section.annotations.aspect.VisibleAspectDefinition;

@SuppressWarnings("null")
public class BehaviorDependentDispatcherTest {

    private TestPropertyDispatcher wrappedDispatcher = new TestPropertyDispatcher();

    private BehaviorDependentDispatcher behaviorDispatcher;


    @Before
    public void setUp() {
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher,
                PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
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
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher, null);

        behaviorDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME));
    }

    @Test(expected = NullPointerException.class)
    public void testNullBoundObject_visible() {
        wrappedDispatcher.setBoundObject(null);
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher,
                PropertyBehaviorProvider.with(PropertyBehavior.visible(() -> false)));

        behaviorDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME));
    }

    @Test(expected = NullPointerException.class)
    public void testNullBoundObject_writable() {
        wrappedDispatcher.setBoundObject(null);
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher,
                PropertyBehaviorProvider.with(PropertyBehavior.writable(() -> false)));

        behaviorDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME));
    }

    @Test(expected = NullPointerException.class)
    public void testNullBoundObject_messages() {
        wrappedDispatcher.setBoundObject(null);
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher,
                PropertyBehaviorProvider.with(PropertyBehavior.showValidationMessages(() -> false)));

        behaviorDispatcher.getMessages(new MessageList());
    }

    @Test
    public void testNonConsensus_visible() {
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher,
                PropertyBehaviorProvider.with(PropertyBehavior.visible(() -> false)));

        assertThat(behaviorDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME)), is(false));
    }

    @Test
    public void testNonConsensus_writable() {
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher,
                PropertyBehaviorProvider.with(PropertyBehavior.writable(() -> false)));

        assertThat(behaviorDispatcher.isPushable(Aspect.of(StringUtils.EMPTY)), is(false));
    }

    @Test
    public void testNonConsensus_messages() {
        behaviorDispatcher = new BehaviorDependentDispatcher(wrappedDispatcher,
                PropertyBehaviorProvider.with(PropertyBehavior.showValidationMessages(() -> false)));

        MessageList messageList = new MessageList(Message.builder("Foo", Severity.ERROR)
                .invalidObject(new ObjectProperty(wrappedDispatcher.getBoundObject(), "prop")).code("4711").create());

        assertThat(behaviorDispatcher.getMessages(messageList), is(emptyMessageList()));
    }

    @Test
    public void testIsPushable_IgnoreOtherAspect() {
        behaviorDispatcher = new BehaviorDependentDispatcher(new TestPropertyDispatcher(),
                PropertyBehaviorProvider.with(PropertyBehavior.readOnly()));

        boolean pushable = behaviorDispatcher.isPushable(Aspect.of("any"));

        assertThat(pushable, is(true));
    }

    @Test
    public void testIsPushable_ReadOnly() {
        behaviorDispatcher = new BehaviorDependentDispatcher(new TestPropertyDispatcher(),
                PropertyBehaviorProvider.with(PropertyBehavior.readOnly()));

        boolean pushable = behaviorDispatcher.isPushable(Aspect.of(""));

        assertThat(pushable, is(false));
    }

    private static class TestPropertyDispatcher extends AbstractPropertyDispatcherDecorator {

        private Object boundObject = new Object();

        public TestPropertyDispatcher() {
            super(new ExceptionPropertyDispatcher("test"));
        }

        @Override
        public <T> boolean isPushable(Aspect<T> aspect) {
            return true;
        }

        @Override
        public @Nullable Object getBoundObject() {
            return boundObject;
        }

        public void setBoundObject(@Nullable Object boundObject) {
            this.boundObject = boundObject;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T pull(Aspect<T> aspect) {
            return (T)Boolean.TRUE;
        }

    }

}
