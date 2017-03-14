/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.linkki.core.binding.ButtonBinding;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.FieldBinding;
import org.linkki.core.binding.LabelBinding;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.annotations.adapters.UIToolTipAdapter;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class BindAnnotationDescriptorTest {

    @Test
    public void testCreateBinding_CreatesFieldBindingForField() {
        BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(mock(Bind.class),
                new UIToolTipAdapter(null));
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        ElementBinding binding = descriptor.createBinding(dispatcher, Handler.NOP_HANDLER, new TextField(), null);
        assertThat(binding, is(instanceOf(FieldBinding.class)));
    }

    @Test
    public void testCreateBinding_CreatesButtonBindingForButton() {
        BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(mock(Bind.class),
                new UIToolTipAdapter(null));
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        ElementBinding binding = descriptor.createBinding(dispatcher, Handler.NOP_HANDLER, new Button(), null);
        assertThat(binding, is(instanceOf(ButtonBinding.class)));
    }

    @Test
    public void testCreateBinding_CreatesLabelBindingForLabel() {
        BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(mock(Bind.class),
                new UIToolTipAdapter(null));
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        ElementBinding binding = descriptor.createBinding(dispatcher, Handler.NOP_HANDLER, new Label(), null);
        assertThat(binding, is(instanceOf(LabelBinding.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBinding_ThrowsExceptionForUnknownComponent() {
        BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(mock(Bind.class),
                new UIToolTipAdapter(null));
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        descriptor.createBinding(dispatcher, Handler.NOP_HANDLER, new VerticalLayout(), null);
    }

}
