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
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.message.MessageList;

import com.vaadin.ui.Button;

public class ExceptionPropertyDispatcherTest {

    private static final String OBJ1 = "obj1";
    private static final Object OBJ2 = new Button();

    private static final String PROPERTY_NAME = "testProperty";

    @SuppressWarnings("null")
    private ExceptionPropertyDispatcher dispatcher;

    @Before
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

    @SuppressWarnings("null")
    @Test
    public void testGetBoundObject() {
        assertThat(dispatcher.getBoundObject(), is(OBJ1));
    }

    @Test
    public void testToString_WithoutNull() {
        assertThat(dispatcher.toString(),
                   is("ExceptionPropertyDispatcher[String,Button#" + PROPERTY_NAME + "]"));
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
