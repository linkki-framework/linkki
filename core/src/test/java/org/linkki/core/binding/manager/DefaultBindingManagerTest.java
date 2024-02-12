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
package org.linkki.core.binding.manager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TestPmo;
import org.linkki.core.binding.descriptor.BindingDescriptor;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultBindingManagerTest {

    @Mock
    PropertyBehaviorProvider behaviorProvider1;
    @Mock
    PropertyBehaviorProvider behaviorProvider2;
    @Spy
    PropertyDispatcherFactory dispatcherFactory;

    @Test
    public void testStartNewContext_BindingContextUsesManagersPropertyBehaviorProvider() {
        DefaultBindingManager defaultBindingManager = new DefaultBindingManager(() -> new MessageList(),
                behaviorProvider1);
        BindingContext bindingContext = defaultBindingManager.getContext("foo");

        assertThat(bindingContext.getBehaviorProvider(), is(behaviorProvider1));
    }

    @Test
    public void testStartNewContext_BindingContextUsesCustomPropertyBehaviorProvider() {
        DefaultBindingManager defaultBindingManager = new DefaultBindingManager(() -> new MessageList(),
                behaviorProvider1);
        BindingContext bindingContext = defaultBindingManager.createContext("foo", behaviorProvider2);

        assertThat(bindingContext.getBehaviorProvider(), is(behaviorProvider2));
    }

    @Test
    public void testStartNewContext_BindingContextUsesCustomPropertyDispatcherFactory() {
        DefaultBindingManager defaultBindingManager = new DefaultBindingManager(() -> new MessageList(),
                behaviorProvider1, dispatcherFactory);
        BindingContext bindingContext = defaultBindingManager.getContext("foo");
        bindingContext.bind(new TestPmo(), new BindingDescriptor(BoundProperty.empty(), Collections.emptyList()),
                            new TestComponentWrapper(new TestUiComponent()));

        verify(dispatcherFactory, times(1)).createDispatcherChain(any(), any(), any());
    }
}
