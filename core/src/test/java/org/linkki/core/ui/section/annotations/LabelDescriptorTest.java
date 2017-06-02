/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.adapters.LabelBindingDefinition;
import org.linkki.core.ui.section.annotations.adapters.UIToolTipAdapter;

public class LabelDescriptorTest {

    @Test
    public void testGetModelPropertyName_UsesPropertyFromAnnotation() {
        UILabel labelAnnotation = mock(UILabel.class);
        when(labelAnnotation.modelAttribute()).thenReturn("property");
        UIFieldDefinition labelDefinition = new LabelBindingDefinition(labelAnnotation);
        LabelDescriptor descriptor = new LabelDescriptor(labelDefinition, new UIToolTipAdapter(null), "fallback",
                Void.class);

        assertThat(descriptor.getModelPropertyName(), is("property"));
    }

    @Test
    public void testGetModelPropertyName_UsesFallbackPropertyName() {
        UILabel labelAnnotation = mock(UILabel.class);
        UIFieldDefinition labelDefinition = new LabelBindingDefinition(labelAnnotation);
        LabelDescriptor descriptor = new LabelDescriptor(labelDefinition, new UIToolTipAdapter(null), "fallback",
                Void.class);

        // precondition
        assertThat(labelDefinition.modelAttribute(), is(nullValue()));

        assertThat(descriptor.getModelPropertyName(), is("fallback"));
    }

}
