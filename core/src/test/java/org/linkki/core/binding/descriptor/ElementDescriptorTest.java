/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.binding.descriptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.util.handler.Handler;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ElementDescriptorTest {

    @Test
    public void testGetPropertyName_favorModelAttribute() {
        ElementDescriptor elementDescriptor = new ElementDescriptor(0, mock(LinkkiComponentDefinition.class),
                BoundProperty.of("test").withModelAttribute("xyz"), new ArrayList<>());

        assertEquals("xyz", elementDescriptor.getModelAttributeName());
    }

    @Test
    public void testGetPropertyName() {
        ElementDescriptor elementDescriptor = new ElementDescriptor(0, mock(LinkkiComponentDefinition.class),
                BoundProperty.of("test"), new ArrayList<>());

        assertEquals("test", elementDescriptor.getModelAttributeName());
    }

    @Test
    public void testAddAspectDefinitions() {
        LinkkiAspectDefinition aspectDefinition1 = (p, w) -> Handler.NOP_HANDLER;
        LinkkiAspectDefinition aspectDefinition2 = (p, w) -> Handler.NOP_HANDLER;
        LinkkiAspectDefinition aspectDefinition3 = (p, w) -> Handler.NOP_HANDLER;
        ElementDescriptor elementDescriptor = new ElementDescriptor(0, mock(LinkkiComponentDefinition.class),
                BoundProperty.of("test"), Arrays.asList(aspectDefinition1));

        elementDescriptor.addAspectDefinitions(Arrays.asList(aspectDefinition3, aspectDefinition2));

        assertThat(elementDescriptor.getAspectDefinitions(),
                   contains(aspectDefinition1, aspectDefinition3, aspectDefinition2));
    }

    @Test
    public void testAddAspectDefinitions_IgnoresAlreadyContainedAspects() {
        LinkkiAspectDefinition aspectDefinition1 = (p, w) -> Handler.NOP_HANDLER;
        LinkkiAspectDefinition aspectDefinition2 = (p, w) -> Handler.NOP_HANDLER;
        LinkkiAspectDefinition aspectDefinition3 = (p, w) -> Handler.NOP_HANDLER;
        ElementDescriptor elementDescriptor = new ElementDescriptor(0, mock(LinkkiComponentDefinition.class),
                BoundProperty.of("test"), Arrays.asList(aspectDefinition1, aspectDefinition2));

        elementDescriptor.addAspectDefinitions(Arrays.asList(aspectDefinition3, aspectDefinition2));

        assertThat(elementDescriptor.getAspectDefinitions(),
                   contains(aspectDefinition1, aspectDefinition2, aspectDefinition3));
    }

}
