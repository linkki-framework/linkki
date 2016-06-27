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
import org.linkki.core.ui.section.annotations.adapters.UILabelAdapter;

public class LabelDescriptorTest {

    @Test
    public void testGetPropertyName_UsesPropertyFromAnnotation() {
        UILabel labelAnnotation = mock(UILabel.class);
        when(labelAnnotation.modelAttribute()).thenReturn("property");
        UILabelDefinition labelDefinition = new UILabelAdapter(labelAnnotation);
        LabelDescriptor descriptor = new LabelDescriptor(labelDefinition, "fallback", "modelObject");

        assertThat(descriptor.getPropertyName(), is("property"));
    }

    @Test
    public void testGetPropertyName_UsesFallbackPropertyName() {
        UILabel labelAnnotation = mock(UILabel.class);
        UILabelDefinition labelDefinition = new UILabelAdapter(labelAnnotation);
        LabelDescriptor descriptor = new LabelDescriptor(labelDefinition, "fallback", "modelObject");

        // precondition
        assertThat(labelDefinition.modelAttribute(), is(nullValue()));

        assertThat(descriptor.getPropertyName(), is("fallback"));
    }

}