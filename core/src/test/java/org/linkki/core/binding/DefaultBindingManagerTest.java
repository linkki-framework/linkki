/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.faktorips.runtime.MessageList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBindingManagerTest {

    @Mock
    @SuppressWarnings("null")
    PropertyBehaviorProvider behaviorProvider;

    @Test
    public void testStartNewContext_BindingContextUsesManagersPropertyBehaviorProvider() {
        DefaultBindingManager defaultBindingManager = new DefaultBindingManager(() -> new MessageList(),
                behaviorProvider);
        BindingContext bindingContext = defaultBindingManager.startNewContext("foo");

        assertThat(bindingContext.getBehaviorProvider(), is(behaviorProvider));
    }

}
