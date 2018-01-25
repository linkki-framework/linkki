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
package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.Test;
import org.linkki.core.binding.ButtonBinding;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.FieldBinding;
import org.linkki.core.binding.LabelBinding;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class BindAnnotationDescriptorTest {

    @Test
    public void testCreateBinding_CreatesFieldBindingForField() {
        BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(mock(Bind.class), new ArrayList<>());
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        ElementBinding binding = descriptor.createBinding(dispatcher, Handler.NOP_HANDLER, new TextField(), null);
        assertThat(binding, is(instanceOf(FieldBinding.class)));
    }

    @Test
    public void testCreateBinding_CreatesButtonBindingForButton() {
        BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(mock(Bind.class), new ArrayList<>());
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        ElementBinding binding = descriptor.createBinding(dispatcher, Handler.NOP_HANDLER, new Button(), null);
        assertThat(binding, is(instanceOf(ButtonBinding.class)));
    }

    @Test
    public void testCreateBinding_CreatesLabelBindingForLabel() {
        BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(mock(Bind.class), new ArrayList<>());
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        ElementBinding binding = descriptor.createBinding(dispatcher, Handler.NOP_HANDLER, new Label(), null);
        assertThat(binding, is(instanceOf(LabelBinding.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBinding_ThrowsExceptionForUnknownComponent() {
        BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(mock(Bind.class), new ArrayList<>());
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        descriptor.createBinding(dispatcher, Handler.NOP_HANDLER, new VerticalLayout(), null);
    }

}
