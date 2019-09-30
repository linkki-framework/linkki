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

package org.linkki.core.defaults.columnbased;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.linkki.test.matcher.Matchers.hasValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.PropertyElementDescriptors;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.defaults.columnbased.pmo.TestContainerPmo;
import org.linkki.core.defaults.columnbased.pmo.TestRowPmo;
import org.linkki.core.defaults.nls.TestUiLayoutComponent;

public class ColumnBasedComponentFactoryTest {

    @Test
    public void testCreateContainerComponent() {
        ColumnBasedComponentCreator containerComponentCreator = spy(new TestColumnBasedComponentCreator());
        ColumnBasedComponentFactory columnBasedComponentFactory = new ColumnBasedComponentFactory(
                containerComponentCreator);
        BindingContext bindingContext = new BindingContext();
        TestContainerPmo containerPmo = new TestContainerPmo();
        TableFooterPmo footerPmo = $ -> "foot";
        containerPmo.setFooterPmo(footerPmo);
        TestColumnBasedComponent<?> containerComponent = new TestColumnBasedComponent<>();
        TestColumnBasedComponentWrapper<?> containerComponentWrapper = new TestColumnBasedComponentWrapper<>(
                containerComponent);
        when(containerComponentCreator.createComponent(containerPmo)).thenReturn(containerComponentWrapper);

        Object createdContainerComponent = columnBasedComponentFactory.createContainerComponent(containerPmo,
                                                                                                bindingContext);

        assertThat(createdContainerComponent, is(sameInstance(containerComponent)));

        Collection<Binding> bindings = bindingContext.getBindings();
        assertThat(getNumberOfBindingsFor(containerComponent, bindings), is(1));
        assertThat(getBindingFor(containerComponent, bindings).getPmo(), is(containerPmo));

        verify(containerComponentCreator, times(2)).initColumn(eq(containerPmo), eq(containerComponentWrapper),
                                                               any(BindingContext.class),
                                                               any(PropertyElementDescriptors.class));
        assertThat(containerComponent.getFooterPmo(), hasValue(footerPmo));
        assertThat(containerComponent.getColumnsOnLastFooterUpdate(), is(2));

        assertThat(containerComponent.getPageLength(), is(0));

        containerPmo.addItem(new TestRowPmo());
        bindingContext.modelChanged();

        assertThat(containerComponent.getPageLength(), is(1));


    }


    private Binding getBindingFor(TestUiLayoutComponent containerComponent, Collection<Binding> bindings) {
        return bindings.stream().filter(b -> b.getBoundComponent().equals(containerComponent))
                .findFirst().get();
    }


    private int getNumberOfBindingsFor(TestUiLayoutComponent containerComponent, Collection<Binding> bindings) {
        return (int)bindings.stream().filter(b -> b.getBoundComponent().equals(containerComponent)).count();
    }

}
