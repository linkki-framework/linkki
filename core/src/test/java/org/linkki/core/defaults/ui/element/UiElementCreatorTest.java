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

package org.linkki.core.defaults.ui.element;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TestModelObject;
import org.linkki.core.binding.TestPmo;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.section.TestSectionPmo;
import org.linkki.core.defaults.ui.element.UiElementCreator;

public class UiElementCreatorTest {

    @Test
    public void testCreateUiElements() {
        TestSectionPmo testSectionPmo = new TestSectionPmo();
        BindingContext bindingContext = new BindingContext();
        List<TestComponentWrapper> wrappers = UiElementCreator
                .createUiElements(testSectionPmo, bindingContext, TestComponentWrapper::new)
                .collect(Collectors.toList());

        assertThat(wrappers, hasSize(2));
        List<String> componentIds = wrappers.stream().map(TestComponentWrapper::getComponent)
                .map(TestUiComponent::getId).collect(Collectors.toList());
        assertThat(componentIds, contains(TestPmo.PROPERTY_VALUE, TestModelObject.PROPERTY_MODEL_PROP));

        List<String> boundIds = bindingContext.getBindings().stream().map(Binding::getBoundComponent)
                .map(TestUiComponent.class::cast).map(TestUiComponent::getId).collect(Collectors.toList());
        assertThat(boundIds, containsInAnyOrder(TestPmo.PROPERTY_VALUE, TestModelObject.PROPERTY_MODEL_PROP));
    }

}
