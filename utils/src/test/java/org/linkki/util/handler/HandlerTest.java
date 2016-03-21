/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util.handler;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Stack;

import org.junit.Test;
import org.linkki.util.handler.Handler;

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
