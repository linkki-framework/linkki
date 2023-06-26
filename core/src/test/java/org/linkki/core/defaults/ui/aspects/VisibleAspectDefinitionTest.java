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

package org.linkki.core.defaults.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VisibleAspectDefinitionTest {

    private final TestComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());

    @Test
    void testCreateAspect_visible() {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(VisibleType.VISIBLE);

        Aspect<Boolean> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(VisibleAspectDefinition.NAME));
        assertThat(createdAspect.getValue(), is(true));
    }

    @Test
    void testCreateAspect_invisible() {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(VisibleType.INVISIBLE);

        Aspect<Boolean> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(VisibleAspectDefinition.NAME));
        assertThat(createdAspect.isValuePresent(), is(true));
        assertThat(createdAspect.getValue(), is(false));
    }

    @Test
    void testCreateAspect_dynamic() {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(VisibleType.DYNAMIC);

        Aspect<Boolean> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(VisibleAspectDefinition.NAME));
        assertThat(createdAspect.isValuePresent(), is(false));
    }

    @Test
    void testCreateComponentValueSetterComponentWrapper() {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(
                VisibleType.VISIBLE);
        Consumer<Boolean> setter = aspectDefinition.createComponentValueSetter(componentWrapper);
        assertThat(componentWrapper.getComponent().isVisible(), is(true));

        setter.accept(false);

        assertThat(componentWrapper.getComponent().isVisible(), is(false));
    }
}
