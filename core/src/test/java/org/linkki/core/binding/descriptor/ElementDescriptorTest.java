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
package org.linkki.core.binding.descriptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.ui.section.annotations.adapters.TextFieldBindingDefinition;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ElementDescriptorTest {

    @Mock
    private TextFieldBindingDefinition adapter = mock(TextFieldBindingDefinition.class);

    @Test
    public void testGetPropertyName_favorModelAttribute() {
        when(adapter.modelAttribute()).thenReturn("xyz");
        ElementDescriptor elementDescriptor = new ElementDescriptor(adapter, "Test", new ArrayList<>());

        assertEquals("xyz", elementDescriptor.getModelPropertyName());
    }

    @Test
    public void testGetPropertyName_byMethodName() {
        ElementDescriptor elementDescriptor = new ElementDescriptor(adapter, "Test", new ArrayList<>());

        assertEquals("Test", elementDescriptor.getModelPropertyName());
    }

}
