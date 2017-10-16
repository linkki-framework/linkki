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
