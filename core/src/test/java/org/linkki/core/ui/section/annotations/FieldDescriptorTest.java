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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.ui.section.annotations.adapters.TextFieldBindingDefinition;
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
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, "xyz", Void.class, new ArrayList<>());

        assertEquals("blablub", fieldDescriptor.getLabelText());
    }

    @Test
    public void getLabelText_deriveFromPropertyName() {
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, "Test", Void.class, new ArrayList<>());

        assertEquals("Test", fieldDescriptor.getLabelText());
    }

    @Test
    public void getLabelText_addSuffixOnlyIfNecessary() {
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, "Test:", Void.class, new ArrayList<>());

        assertEquals("Test:", fieldDescriptor.getLabelText());
    }

    @Test
    public void getPropertyName_favorModelAttribute() {
        when(adapter.modelAttribute()).thenReturn("xyz");
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, "Test", Void.class, new ArrayList<>());

        assertEquals("xyz", fieldDescriptor.getModelPropertyName());
    }

    @Test
    public void getPropertyName_byMethodName() {
        FieldDescriptor fieldDescriptor = new FieldDescriptor(adapter, "Test", Void.class, new ArrayList<>());

        assertEquals("Test", fieldDescriptor.getModelPropertyName());
    }

}
