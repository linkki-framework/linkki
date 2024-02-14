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

package org.linkki.core.defaults.columnbased.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.columnbased.ColumnBasedComponentWrapper;
import org.linkki.core.defaults.columnbased.TestColumnBasedComponent;
import org.linkki.core.defaults.columnbased.TestColumnBasedComponentWrapper;

public class ColumnBasedComponentItemsAspectDefinitionTest {

    @Test
    public void testCreateAspect() {
        Aspect<List<Object>> aspect = new ColumnBasedComponentItemsAspectDefinition<>().createAspect();
        assertThat(aspect.getName(), is(ColumnBasedComponentItemsAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(false));
    }

    @Test
    public void testCreateComponentValueSetter() {
        TestColumnBasedComponent<Object> component = new TestColumnBasedComponent<>();
        ComponentWrapper componentWrapper = new TestColumnBasedComponentWrapper<>(component);
        Consumer<List<Object>> componentValueSetter = new ColumnBasedComponentItemsAspectDefinition<>()
                .createComponentValueSetter(componentWrapper);

        componentValueSetter.accept(Arrays.asList("foo", "bar"));

        assertThat(component.getItems(), contains("foo", "bar"));
    }

    @Test
    public void testSupports() {
        assertThat(new ColumnBasedComponentItemsAspectDefinition<>().supports(WrapperType.COMPONENT), is(false));
        assertThat(new ColumnBasedComponentItemsAspectDefinition<>()
                .supports(ColumnBasedComponentWrapper.COLUMN_BASED_TYPE), is(true));
    }

}
