/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.ui.section.annotations.adapters.TextFieldBindingDefinition;
import org.linkki.core.ui.section.annotations.adapters.UIToolTipAdapter;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FieldDescriptorTest {

    @SuppressWarnings("null")
    @Mock
    private TextFieldBindingDefinition adapter;

    @Before
    public void setUp() {
        when(adapter.showLabel()).thenReturn(true);
    }

    @Test
    public void getLabelText_deriveFromLabel() {
        when(adapter.label()).thenReturn("blablub");
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, new UIToolTipAdapter(null), "xyz", Void.class);

        assertEquals("blablub", fieldDescriptor.getLabelText());
    }

    @Test
    public void getLabelText_deriveFromPropertyName() {
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, new UIToolTipAdapter(null), "Test", Void.class);

        assertEquals("Test", fieldDescriptor.getLabelText());
    }

    @Test
    public void getLabelText_addSuffixOnlyIfNecessary() {
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, new UIToolTipAdapter(null), "Test:", Void.class);

        assertEquals("Test:", fieldDescriptor.getLabelText());
    }

    @Test
    public void getPropertyName_favorModelAttribute() {
        when(adapter.modelAttribute()).thenReturn("xyz");
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, new UIToolTipAdapter(null), "Test", Void.class);

        assertEquals("xyz", fieldDescriptor.getModelPropertyName());
    }

    @Test
    public void getPropertyName_byMethodName() {
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, new UIToolTipAdapter(null), "Test", Void.class);

        assertEquals("Test", fieldDescriptor.getModelPropertyName());
    }

}
