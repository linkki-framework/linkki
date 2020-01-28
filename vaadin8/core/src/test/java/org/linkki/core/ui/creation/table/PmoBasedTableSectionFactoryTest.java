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
package org.linkki.core.ui.creation.table;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ContainerBinding;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.ui.creation.table.container.LinkkiInMemoryContainer;
import org.linkki.core.vaadin.component.section.AbstractSection;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

public class PmoBasedTableSectionFactoryTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateSection_TableIsAddedAndBound() {
        TestTablePmo containerPmo = new TestTablePmo();
        BindingContext bindingContext = new BindingContext();
        PmoBasedSectionFactory factory = new PmoBasedSectionFactory();

        AbstractSection tableSection = factory.createSection(containerPmo, bindingContext);

        assertThat(tableSection, is(notNullValue()));
        assertThat(tableSection.getComponentCount(), is(2)); // header and table
        assertThat(tableSection.getComponent(1), is(instanceOf(com.vaadin.v7.ui.Table.class)));
        com.vaadin.v7.ui.Table table = (com.vaadin.v7.ui.Table)tableSection.getComponent(1);
        assertThat(table.getContainerDataSource(), is(instanceOf(LinkkiInMemoryContainer.class)));
        LinkkiInMemoryContainer<?> container = (LinkkiInMemoryContainer<?>)table.getContainerDataSource();
        assertThat(bindingContext, hasBindingWith(container));
    }

    private Matcher<BindingContext> hasBindingWith(LinkkiInMemoryContainer<?> container) {
        return new TypeSafeMatcher<BindingContext>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("a BindingContext containing a Binding using the table container ");
                description.appendValue(container);
            }

            @SuppressWarnings("deprecation")
            @Override
            protected boolean matchesSafely(BindingContext bindingContext) {
                return bindingContext.getBindings().stream()
                        // TableSection
                        .filter(ContainerBinding.class::isInstance)
                        .map(ContainerBinding.class::cast)
                        .flatMap(bc -> bc.getBindings().stream())
                        // Table
                        .filter(ContainerBinding.class::isInstance)
                        .map(ContainerBinding.class::cast)
                        .map(binding -> binding.getBoundComponent())
                        .filter(com.vaadin.v7.ui.Table.class::isInstance)
                        .map(com.vaadin.v7.ui.Table.class::cast)
                        .map(com.vaadin.v7.ui.Table::getContainerDataSource)
                        .anyMatch(Predicate.isEqual(container));
            }
        };
    }

    @Test
    public void testCreateSection_SectionHasAddButtonInHeader() {
        TestTablePmo containerPmo = new TestTablePmo();
        BindingContext bindingContext = new BindingContext();
        PmoBasedSectionFactory factory = new PmoBasedSectionFactory();

        AbstractSection tableSection = factory.createSection(containerPmo, bindingContext);

        assertThat(tableSection, is(notNullValue()));
        assertThat(tableSection.getComponentCount(), is(2)); // header and table
        assertThat(tableSection.getComponent(0), is(instanceOf(HorizontalLayout.class)));
        HorizontalLayout header = (HorizontalLayout)tableSection.getComponent(0);
        // caption, add button, close button, hidden show/expand button and @SectionHeader button
        assertThat(header.getComponentCount(), is(5));
        assertThat(header.getComponent(2), is(instanceOf(Button.class)));
        assertThat(header.getComponent(2).isVisible(), is(false));
        assertThat(header.getComponent(1), is(instanceOf(Button.class)));
        Button addButton = (Button)header.getComponent(1);
        assertThat(addButton.getIcon(), is(VaadinIcons.PLUS));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateSection_NoAnnotation() {
        NoAnnotationTablePmo containerPmo = new NoAnnotationTablePmo();
        BindingContext bindingContext = new BindingContext();
        PmoBasedSectionFactory factory = new PmoBasedSectionFactory();

        AbstractSection tableSection = factory.createSection(containerPmo, bindingContext);

        assertThat(tableSection, is(notNullValue()));
        assertThat(tableSection.getComponentCount(), is(2)); // invisible header, table
        assertThat(tableSection.getComponent(0).isVisible(), is(false));
        assertThat(tableSection.getComponent(1), is(instanceOf(com.vaadin.v7.ui.Table.class)));
        com.vaadin.v7.ui.Table table = (com.vaadin.v7.ui.Table)tableSection.getComponent(1);
        assertThat(table.getContainerDataSource(), is(instanceOf(LinkkiInMemoryContainer.class)));
        LinkkiInMemoryContainer<?> container = (LinkkiInMemoryContainer<?>)table.getContainerDataSource();
        assertThat(bindingContext, hasBindingWith(container));
    }

    public static class NoAnnotationTablePmo implements ContainerPmo<TestRowPmo> {

        @Override
        public List<TestRowPmo> getItems() {
            return new ArrayList<>();
        }

    }

    @Test
    public void testCreateSectionHeader() {
        TestTablePmo containerPmo = new TestTablePmo();
        BindingContext bindingContext = new BindingContext();
        PmoBasedSectionFactory factory = new PmoBasedSectionFactory();

        AbstractSection tableSection = factory.createSection(containerPmo, bindingContext);
        HorizontalLayout header = (HorizontalLayout)tableSection.getComponent(0);

        assertThat(header.getComponent(3), instanceOf(Button.class));
        assertThat(header.getComponent(3).getCaption(), is("header button"));
    }
}
