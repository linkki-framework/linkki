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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.bind.MockUi;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.test.TestButtonPmo;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.UI;

import edu.umd.cs.findbugs.annotations.NonNull;

@SuppressWarnings("unchecked")
public class BindingContextIntegrationTest {

    private UI ui;

    private BindingManager bindingManager;

    private TestPage testPage;

    private ContainerBinding tableSectionBinding;

    private ContainerBinding standardSectionBinding;

    private BindingContext bindingContext;

    @Before
    public void mockUi() {
        ui = MockUi.mockUi();
    }

    @After
    public void cleanUpUi() {
        UI.setCurrent(null);
    }

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
        assertThat(bindings, contains(tableSectionBinding));
        assertThat(tableSectionBinding.getBindings().size(), is(2));
    }

    @Test
    public void testRemoveBindingsForPmo_standardOnlyButtonPmo() {
        setUpBindings();

        bindingContext.removeBindingsForPmo(testPage.standardSectionPmo.getEditButtonPmo().get());

        List<Binding> bindings = bindings();
        assertThat(bindings, containsInAnyOrder(tableSectionBinding, standardSectionBinding));
        assertThat(tableSectionBinding.getBindings().size(), is(2));
        assertThat(standardSectionBinding.getBindings().size(), is(1));
    }

    @Test
    public void testRemoveBindingsForPmo_tableWithButton() {
        setUpBindings();

        bindingContext.removeBindingsForPmo(testPage.tableSectionPmo);

        List<Binding> bindings = bindings();
        assertThat(bindings, contains(standardSectionBinding));
        assertThat(bindings, not(hasItem(tableSectionBinding)));
    }

    @Test
    public void testRemoveBindingsForPmo_tableOnlyButtonPmo() {
        setUpBindings();

        bindingContext.removeBindingsForPmo(testPage.tableSectionPmo.getAddItemButtonPmo().get());

        List<Binding> bindings = bindings();
        assertThat(bindings, containsInAnyOrder(tableSectionBinding, standardSectionBinding));
        assertThat(tableSectionBinding.getBindings().size(), is(1));
    }

    private void setUpBindings() {
        bindingManager = new TestBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
        testPage = new TestPage(bindingManager);
        testPage.init();
        testPage.setParent(ui);
        testPage.attach();

        bindingContext = testPage.getBindingContext();
        List<Binding> bindings = bindings();
        // Container for StandardSection, TableSection
        assertThat(bindings, contains(instanceOf(ContainerBinding.class),
                                      instanceOf(ContainerBinding.class)));
        tableSectionBinding = (ContainerBinding)bindings.stream()
                .filter(b -> b.getPmo().equals(testPage.tableSectionPmo))
                .findFirst().get();
        // Table + Button
        assertThat(tableSectionBinding.getBindings().size(), is(2));
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
            return new BindingContext("", behaviorProvider, Handler.NOP_HANDLER);
        }

    }

    public static class TestPage extends AbstractPage {

        private static final long serialVersionUID = 1L;
        private BindingManager bindingManager;
        TestStandardSectionPmo standardSectionPmo = new TestStandardSectionPmo();
        TestTableSectionPmo tableSectionPmo = new TestTableSectionPmo("Foo", "Bar");

        public TestPage(BindingManager bindingManager) {
            this.bindingManager = bindingManager;
        }

        @Override
        public void createContent() {
            addSection(standardSectionPmo);
            addSection(tableSectionPmo);
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
            iterator().forEachRemaining(getBindingContext()::removeBindingsForComponent);
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

    public static class TestTableSectionPmo extends SimpleTablePmo<String, TestTableSectionRowPmo> {
        private static final ButtonPmo NOP_BUTTON_PMO = new TestButtonPmo();

        protected TestTableSectionPmo(@NonNull String... modelObjects) {
            super(Arrays.asList(modelObjects));
        }

        @Override
        protected TestTableSectionRowPmo createRow(String modelObject) {
            return new TestTableSectionRowPmo(modelObject);
        }

        @Override
        public Optional<ButtonPmo> getAddItemButtonPmo() {
            return Optional.of(NOP_BUTTON_PMO);
        }

    }

    public static class TestTableSectionRowPmo {

        private String modelObject;

        public TestTableSectionRowPmo(String modelObject) {
            this.modelObject = modelObject;
        }

        @UITableColumn
        @UILabel(position = 1)
        public String getCol1() {
            return modelObject;
        }
    }

}
