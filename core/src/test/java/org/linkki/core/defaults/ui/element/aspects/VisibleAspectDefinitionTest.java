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

package org.linkki.core.defaults.ui.element.aspects;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.function.Consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.ui.element.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.element.aspects.types.VisibleType;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VisibleAspectDefinitionTest {

    private TestComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());

    @Test
    public void testCreateAspect_visible() {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(VisibleType.VISIBLE);

        Aspect<Boolean> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(VisibleAspectDefinition.NAME));
        assertThat(createdAspect.getValue(), is(true));
    }

    @Test
    public void testCreateAspect_dynamic() {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(VisibleType.DYNAMIC);

        Aspect<Boolean> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(VisibleAspectDefinition.NAME));
        assertThat(createdAspect.isValuePresent(), is(false));
    }

    @Test
    public void testCreateComponentValueSetterComponentWrapper() {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(
                VisibleType.VISIBLE);
        Consumer<Boolean> setter = aspectDefinition.createComponentValueSetter(componentWrapper);
        assertThat(componentWrapper.getComponent().isVisible(), is(false));

        setter.accept(true);

        assertThat(componentWrapper.getComponent().isVisible(), is(true));
    }
}
