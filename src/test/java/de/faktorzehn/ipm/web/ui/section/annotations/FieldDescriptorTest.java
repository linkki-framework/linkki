/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section.annotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UITextFieldAdpater;

@RunWith(MockitoJUnitRunner.class)
public class FieldDescriptorTest {

    @Mock
    private UITextFieldAdpater adapter;

    @Test
    public void getLabelText_deriveFromLabel() {
        when(adapter.label()).thenReturn("blablub");
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, "xyz");

        assertEquals("blablub:", fieldDescriptor.getLabelText());
    }

    @Test
    public void getLabelText_deriveFromPropertyName() {
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, "Test");

        assertEquals("Test:", fieldDescriptor.getLabelText());
    }

    @Test
    public void getLabelText_addSuffixOnlyIfNecessary() {
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, "Test:");

        assertEquals("Test:", fieldDescriptor.getLabelText());
    }

    @Test
    public void getPropertyName_favorModelAttribute() {
        when(adapter.modelAttribute()).thenReturn("xyz");
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, "Test");

        assertEquals("xyz", fieldDescriptor.getPropertyName());
    }

    @Test
    public void getPropertyName_byMethodName() {
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, "Test");

        assertEquals("Test", fieldDescriptor.getPropertyName());
    }

}
