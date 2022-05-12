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

package org.linkki.core.binding;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext.BindingContextBuilder;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.TestButtonPmo;
import org.linkki.core.vaadin.component.page.AbstractPage;


public class BindingContextIntegrationTest {

    private BindingManager bindingManager;

    private TestPage testPage;

    private ContainerBinding standardSectionBinding;

    private BindingContext bindingContext;

    @Test
    public void testRemoveBindingsForComponent() {
        setUpBindings();

        testPage.removeBindings();

        assertThat(testPage.getBindingContext().getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForPmo_standardWithButton() {
        setUpBindings();

        bindingContext.removeBindingsForPmo(testPage.standardSectionPmo);

        List<Binding> bindings = bindings();
        assertThat(bindings.size(), is(0));
    }

    @Test
    public void testRemoveBindingsForPmo_standardOnlyButtonPmo() {
        setUpBindings();

        bindingContext.removeBindingsForPmo(testPage.standardSectionPmo.getEditButtonPmo().get());

        List<Binding> bindings = bindings();
        assertThat(bindings, contains(standardSectionBinding));
        assertThat(standardSectionBinding.getBindings().size(), is(1));
    }

    private void setUpBindings() {
        bindingManager = new TestBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
        testPage = new TestPage(bindingManager);
        testPage.init();

        bindingContext = testPage.getBindingContext();
        List<Binding> bindings = bindings();
        // Container for StandardSection
        assertThat(bindings, contains(instanceOf(ContainerBinding.class)));

        standardSectionBinding = (ContainerBinding)bindings.stream()
                .filter(b -> b.getPmo().equals(testPage.standardSectionPmo))
                .findFirst().get();
        // value + button
        assertThat(standardSectionBinding.getBindings().size(), is(2));
    }

    private List<Binding> bindings() {
        return bindingContext.getBindings().stream()
                .sorted(Comparator.comparing(b -> b.getClass().getSimpleName())).collect(Collectors.toList());
    }

    private static class TestBindingManager extends BindingManager {

        public TestBindingManager(ValidationService validationService) {
            super(validationService);
        }

        @Override
        protected BindingContext newBindingContext(String name) {
            return new BindingContext();
        }

        @Override
        protected BindingContext newBindingContext(String name, PropertyBehaviorProvider behaviorProvider) {
            return new BindingContextBuilder().propertyBehaviorProvider(behaviorProvider).build();
        }

    }

    public static class TestPage extends AbstractPage {

        private static final long serialVersionUID = 1L;
        private final BindingManager bindingManager;
        TestStandardSectionPmo standardSectionPmo = new TestStandardSectionPmo();

        public TestPage(BindingManager bindingManager) {
            this.bindingManager = bindingManager;
        }

        @Override
        public void createContent() {
            addSection(standardSectionPmo);
        }

        @Override
        protected BindingManager getBindingManager() {
            return bindingManager;
        }

        @Override
        protected BindingContext getBindingContext() {
            return super.getBindingContext();
        }

        protected void removeBindings() {
            getChildren().forEach(getBindingContext()::removeBindingsForComponent);
        }

    }

    @UISection
    public static class TestStandardSectionPmo implements PresentationModelObject {
        private static final ButtonPmo NOP_BUTTON_PMO = new TestButtonPmo();

        @UILabel(position = 10)
        public String getValue() {
            return "v";
        }

        @Override
        public Optional<ButtonPmo> getEditButtonPmo() {
            return Optional.of(NOP_BUTTON_PMO);
        }
    }


}
