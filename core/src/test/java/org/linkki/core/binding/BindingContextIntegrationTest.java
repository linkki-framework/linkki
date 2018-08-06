/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.core.binding;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.ButtonPmo;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.page.AbstractPage;
import org.linkki.core.ui.section.annotations.UILabel;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITableColumn;
import org.linkki.core.ui.table.SimpleTablePmo;

import com.vaadin.ui.UI;

@SuppressWarnings("unchecked")
public class BindingContextIntegrationTest {

    @SuppressWarnings("null")
    private UI ui;
    @SuppressWarnings("null")
    private BindingManager bindingManager;
    @SuppressWarnings("null")
    private TestPage testPage;
    @SuppressWarnings("null")
    private TableBinding<TestTableSectionRowPmo> tableBinding;
    @SuppressWarnings("null")
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
        assertThat(tableBinding.getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForPmo_standardWithButton() {
        setUpBindings();

        bindingContext.removeBindingsForPmo(testPage.standardSectionPmo);

        assertThat(bindings(), contains(instanceOf(ButtonPmoBinding.class),
                                        is(tableBinding)));
        assertThat(tableBinding.getBindings().size(), is(2));
    }

    @Test
    public void testRemoveBindingsForPmo_standardOnlyButtonPmo() {
        setUpBindings();

        bindingContext.removeBindingsForPmo(testPage.standardSectionPmo.getEditButtonPmo().get());

        assertThat(bindings(), contains(instanceOf(ButtonPmoBinding.class),
                                        instanceOf(ComponentBinding.class),
                                        is(tableBinding)));
        assertThat(tableBinding.getBindings().size(), is(2));
    }

    @Test
    public void testRemoveBindingsForPmo_tableWithButton() {
        setUpBindings();

        bindingContext.removeBindingsForPmo(testPage.tableSectionPmo);

        assertThat(bindings(), contains(instanceOf(ButtonPmoBinding.class),
                                        instanceOf(ComponentBinding.class)));
        assertThat(tableBinding.getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForPmo_tableOnlyButtonPmo() {
        setUpBindings();

        bindingContext.removeBindingsForPmo(testPage.tableSectionPmo.getAddItemButtonPmo().get());

        assertThat(bindings(), contains(instanceOf(ButtonPmoBinding.class),
                                        instanceOf(ComponentBinding.class),
                                        is(tableBinding)));
        assertThat(tableBinding.getBindings().size(), is(2));
    }

    private void setUpBindings() {
        bindingManager = new TestBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
        testPage = new TestPage(bindingManager);
        testPage.init();
        testPage.setParent(ui);
        testPage.attach();

        bindingContext = testPage.getBindingContext();
        List<Binding> bindings = bindings();
        assertThat(bindings, contains(instanceOf(ButtonPmoBinding.class),
                                      instanceOf(ButtonPmoBinding.class),
                                      instanceOf(ComponentBinding.class),
                                      instanceOf(TableBinding.class)));
        tableBinding = (TableBinding<TestTableSectionRowPmo>)bindings.get(bindings.size() - 1);
        assertThat(tableBinding.getBindings().size(), is(2));
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
        public TestBindingContext startNewContext(String name) {
            return (TestBindingContext)super.startNewContext(name);
        }

        @Override
        protected TestBindingContext newBindingContext(String name) {
            return TestBindingContext.create();
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
        private static final ButtonPmo NOP_BUTTON_PMO = () -> {
            /**/
        };

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
        private static final ButtonPmo NOP_BUTTON_PMO = () -> {
            /**/
        };

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
