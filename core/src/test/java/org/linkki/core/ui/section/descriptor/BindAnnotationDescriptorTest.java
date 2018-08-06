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
package org.linkki.core.ui.section.descriptor;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.Test;
import org.linkki.core.binding.ComponentBinding;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class BindAnnotationDescriptorTest {

    @Test
    public void testCreateBinding_CreatesFieldBindingForField() {
        BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(mock(Bind.class), new ArrayList<>());
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        ElementBinding binding = descriptor.createBinding(dispatcher, Handler.NOP_HANDLER,
                                                          new LabelComponentWrapper(new TextField()));
        assertThat(binding, is(instanceOf(ComponentBinding.class)));
    }

    @Test
    public void testCreateBinding_CreatesButtonBindingForButton() {
        BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(mock(Bind.class), new ArrayList<>());
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        ElementBinding binding = descriptor.createBinding(dispatcher, Handler.NOP_HANDLER,
                                                          new LabelComponentWrapper(new Button()));
        assertThat(binding, is(instanceOf(ElementBinding.class)));
    }

    @Test
    public void testCreateBinding_CreatesLabelBindingForLabel() {
        BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(mock(Bind.class), new ArrayList<>());
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        ElementBinding binding = descriptor.createBinding(dispatcher, Handler.NOP_HANDLER,
                                                          new LabelComponentWrapper(new Label()));
        assertThat(binding, is(instanceOf(ElementBinding.class)));
    }

}
