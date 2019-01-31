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

package org.linkki.core.binding.aspect.definition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import org.junit.Test;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.components.WrapperType;
import org.linkki.util.handler.Handler;

/**
 * Test for the default methods of {@link LinkkiAspectDefinition}
 */
public class LinkkiAspectDefinitionTest {

    @Test
    public void testSupports() {
        assertTrue(new TestLinkkiAspectDefinition().supports(WrapperType.COMPONENT));
        assertTrue(new TestLinkkiAspectDefinition().supports(WrapperType.FIELD));
        assertTrue(new TestLinkkiAspectDefinition().supports(WrapperType.LAYOUT));
        assertTrue(new TestLinkkiAspectDefinition().supports(WrapperType.of("customComponent", WrapperType.COMPONENT)));

        assertFalse(new TestLinkkiAspectDefinition().supports(WrapperType.ROOT));
        assertFalse(new TestLinkkiAspectDefinition().supports(WrapperType.of("custom")));
    }

    private static class TestLinkkiAspectDefinition implements LinkkiAspectDefinition {

        @Override
        public void initialize(Annotation annotation) {
            // nothing to do
        }

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return Handler.NOP_HANDLER;
        }
    }

}
