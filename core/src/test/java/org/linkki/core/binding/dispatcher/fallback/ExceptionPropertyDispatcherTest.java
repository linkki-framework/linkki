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

package org.linkki.core.binding.dispatcher.fallback;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.defaults.nls.TestUiComponent;

public class ExceptionPropertyDispatcherTest {

    private static final String OBJ1 = "obj1";
    private static final Object OBJ2 = new TestUiComponent();

    private static final String PROPERTY_NAME = "testProperty";

    private ExceptionPropertyDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        dispatcher = new ExceptionPropertyDispatcher(PROPERTY_NAME, OBJ1, OBJ2);
    }

    public void testPull() {
        try {
            dispatcher.pull(Aspect.of(PROPERTY_NAME));
        } catch (IllegalStateException e) {
            assertThat(e.getMessage().contains(PROPERTY_NAME), is(true));
        }

        fail();
    }

    public void testPush() {
        try {
            dispatcher.push(Aspect.of(PROPERTY_NAME));
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage().contains(PROPERTY_NAME), is(true));
        }
        fail();
    }

    @Test
    public void testGetBoundObject() {
        assertThat(dispatcher.getBoundObject(), is(OBJ1));
    }

    @Test
    public void testToString_WithoutNull() {
        assertThat(dispatcher.toString(),
                   is("ExceptionPropertyDispatcher[String,TestUiComponent#" + PROPERTY_NAME + "]"));
    }

    @Test
    public void testToString_WithNull() {
        dispatcher = new ExceptionPropertyDispatcher(PROPERTY_NAME, (String)null);
        assertThat(dispatcher.toString(),
                   is("ExceptionPropertyDispatcher[null" + "#" + PROPERTY_NAME + "]"));
    }

    @Test
    public void testGetMessages() {
        assertThat(dispatcher.getMessages(new MessageList()).isEmpty(), is(true));
    }

    @Test
    public void testIsPushable() {
        assertThat(dispatcher.isPushable(Aspect.of(PROPERTY_NAME)), is(false));
    }

}
