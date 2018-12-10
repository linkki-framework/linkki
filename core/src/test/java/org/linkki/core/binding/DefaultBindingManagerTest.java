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
package org.linkki.core.binding;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.message.MessageList;
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
