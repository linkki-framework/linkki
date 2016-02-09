/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Stack;

import org.junit.Test;

public class OkHandlerTest {

    final Stack<TestOkHandler> handlerStack = new Stack<>();

    class TestOkHandler implements OkHandler {

        @Override
        public void onOk() {
            handlerStack.push(this);
        }

    }

    @Test
    public void testAndThen() {
        TestOkHandler h1 = new TestOkHandler();
        TestOkHandler h2 = new TestOkHandler();

        OkHandler composed = h1.andThen(h2);
        composed.onOk();
        assertThat(handlerStack.size(), is(2));
        assertThat(handlerStack.pop(), is(h2));
        assertThat(handlerStack.pop(), is(h1));
    }

}
