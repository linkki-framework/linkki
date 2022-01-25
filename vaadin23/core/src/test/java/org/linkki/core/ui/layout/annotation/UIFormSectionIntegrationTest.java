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

package org.linkki.core.ui.layout.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.section.BaseSection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

class UIFormSectionIntegrationTest {

    @Test
    void testCreation() {
        BindingContext bindingContext = new BindingContext();
        FormSectionPmo pmo = new FormSectionPmo();

        Component component = VaadinUiCreator.createComponent(pmo, bindingContext);
        assertThat("UiCreator should be able to create a FormSection", component,
                   is(instanceOf(BaseSection.class)));

        BaseSection layout = (BaseSection)component;

        assertThat("Child component should be correctly created", layout.getChildren().count(), is(2L));
        List<Component> childComponents = layout.getChildren().collect(Collectors.toList());
        assertThat("First child component should be a HorizontalLayout", childComponents.get(0),
                   is(instanceOf(HorizontalLayout.class)));
        assertThat("Second child component should be a FlexLayout", childComponents.get(1),
                   is(instanceOf(FlexLayout.class)));
    }


    @UIFormSection
    public static class FormSectionPmo {
        // no content needed for creation test
    }
}
