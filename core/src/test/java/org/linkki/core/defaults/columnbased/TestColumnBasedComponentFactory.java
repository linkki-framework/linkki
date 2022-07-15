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

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Method;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ContainerBinding;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiLayoutComponent;
import org.linkki.core.uicreation.ComponentAnnotationReader;

public class TestColumnBasedComponentFactory {

    /**
     * Create a new table based on the container PMO.
     */
    public Object createContainerComponent(ContainerPmo<?> containerPmo, BindingContext bindingContext) {
        ComponentWrapper tableWrapper = createComponent();
        requireNonNull(bindingContext, "bindingContext must not be null");
        List<LinkkiAspectDefinition> tableAspects = AspectAnnotationReader
                .createAspectDefinitionsFor(containerPmo.getClass());
        ContainerBinding binding = bindingContext.bindContainer(containerPmo, BoundProperty.empty(), tableAspects,
                                                                tableWrapper);
        createColumns(containerPmo, tableWrapper, binding);
        // need to update binding after columns are created because the footer content cannot be updated
        // without columns
        binding.updateFromPmo();
        return tableWrapper.getComponent();
    }

    private void createColumns(ContainerPmo<?> containerPmo, ComponentWrapper tableWrapper, ContainerBinding binding) {
        Class<?> rowPmoClass = containerPmo.getItemPmoClass();
        ComponentAnnotationReader.getComponentDefinitionMethods(rowPmoClass)
                .forEach(m -> initColumn(containerPmo, tableWrapper, binding, m));
    }

    private void initColumn(ContainerPmo<?> containerPmo,
            ComponentWrapper parentWrapper,
            BindingContext bindingContext,
            Method m) {
        TestColumnBasedComponent<?> table = (TestColumnBasedComponent<?>)parentWrapper.getComponent();
        TestUiLayoutComponent column = new TestUiLayoutComponent();
        table.addChild(column);
        bindingContext.bind(containerPmo,
                            BoundPropertyAnnotationReader.getBoundProperty(m),
                            AspectAnnotationReader.createAspectDefinitionsFor(m),
                            new TestComponentWrapper(column));
    }

    private ComponentWrapper createComponent() {
        return new TestComponentWrapper(new TestColumnBasedComponent<>());
    }
}