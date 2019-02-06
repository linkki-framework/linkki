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

package org.linkki.core.ui.section.annotations.aspect;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.function.Consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.ui.TestComponentWrapper;
import org.linkki.core.ui.TestUiComponent;
import org.linkki.core.ui.section.annotations.BindTooltip.TooltipType;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BindTooltipAspectDefinitionTest {

    private TestComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());

    @Test
    public void testCreateAspect_static() {
        BindTooltipAspectDefinition aspectDefinition = new BindTooltipAspectDefinition(TooltipType.STATIC, "Foo");

        Aspect<String> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(BindTooltipAspectDefinition.NAME));
        assertThat(createdAspect.getValue(), is("Foo"));
    }

    @Test
    public void testCreateAspect_dynamic() {
        BindTooltipAspectDefinition aspectDefinition = new BindTooltipAspectDefinition(TooltipType.DYNAMIC, "Bar");

        Aspect<String> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(BindTooltipAspectDefinition.NAME));
        assertThat(createdAspect.isValuePresent(), is(false));
    }

    @Test
    public void testCreateComponentValueSetterComponentWrapper() {
        BindTooltipAspectDefinition aspectDefinition = new BindTooltipAspectDefinition(
                TooltipType.STATIC, "test");
        Consumer<String> setter = aspectDefinition.createComponentValueSetter(componentWrapper);

        assertThat(componentWrapper.getComponent().getTooltipText(), is(nullValue()));

        setter.accept("test");

        assertThat(componentWrapper.getComponent().getTooltipText(), is("test"));
    }
}
