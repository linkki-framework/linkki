/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.util.handler;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Stack;

import org.junit.Test;

public class HandlerTest {

    final Stack<TestOkHandler> handlerStack = new Stack<>();

    class TestOkHandler implements Handler {

        @Override
        public void apply() {
            handlerStack.push(this);
        }

    }

    @Test
    public void testAndThen() {
        TestOkHandler h1 = new TestOkHandler();
        TestOkHandler h2 = new TestOkHandler();

        Handler composed = h1.andThen(h2);
        composed.apply();
        assertThat(handlerStack.size(), is(2));
        assertThat(handlerStack.pop(), is(h2));
        assertThat(handlerStack.pop(), is(h1));
    }

}
