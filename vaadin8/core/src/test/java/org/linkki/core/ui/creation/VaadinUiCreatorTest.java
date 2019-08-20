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

package org.linkki.core.ui.creation;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Optional;

import org.junit.Test;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.section.FormSection;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class VaadinUiCreatorTest {

    @Test
    public void testCreateComponent() {
        TestPmo pmo = new TestPmo();
        BindingContext bindingContext = new BindingContext();

        Component component = VaadinUiCreator.createComponent(pmo, bindingContext);

        assertThat(component, instanceOf(FormSection.class));
        assertThat(bindingContext.getBindings(), hasSize(1));

        Binding binding = bindingContext.getBindings().iterator().next();
        assertThat(binding.getBoundComponent(), is(component));
        assertThat(binding, is(instanceOf(BindingContext.class)));

        Collection<Binding> elementBindings = ((BindingContext)binding).getBindings();
        assertThat(elementBindings.size(), is(1));
        assertThat(elementBindings.iterator().next().getBoundComponent(), is(instanceOf(TextField.class)));
    }

    @Test
    public void testCreateComponent_WithFinders() {
        TestPmo pmo = new TestPmo();
        BindingContext bindingContext = new BindingContext();

        Component component = VaadinUiCreator.createComponent(pmo, bindingContext,
                                                              c -> Optional.of(new TestComponentDefinition()),
                                                              c -> Optional.empty());

        assertThat(component, instanceOf(Label.class));
        assertThat(bindingContext.getBindings(), hasSize(1));
        Binding binding = bindingContext.getBindings().iterator().next();
        assertThat(binding.getBoundComponent(), is(component));
        assertThat(binding, is(not(instanceOf(BindingContext.class))));
    }

    private static class TestComponentDefinition implements LinkkiComponentDefinition {

        @Override
        public Object createComponent(Object pmo) {
            return new Label("test");
        }

    }

    @UISection
    private static class TestPmo {

        @UITextField(position = 0, label = "test")
        public String getText() {
            return "text";
        }
    }
}
