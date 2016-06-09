/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.table;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TableBinding;
import org.linkki.core.binding.TestBindingContext;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;

public class PmoBasedTableSectionFactoryTest {

    @Test
    public void testCreateSection_TableIsAddedAndBound() {
        TestTablePmo containerPmo = new TestTablePmo();
        BindingContext bindingContext = TestBindingContext.create();
        PmoBasedTableSectionFactory<TestRowPmo> factory = new PmoBasedTableSectionFactory<TestRowPmo>(
                containerPmo, bindingContext);

        TableSection<TestRowPmo> tableSection = factory.createSection();

        assertThat(tableSection, is(notNullValue()));
        assertThat(tableSection.getComponentCount(), is(2)); // header and table
        assertThat(tableSection.getComponent(1), is(instanceOf(Table.class)));
        Table table = (Table)tableSection.getComponent(1);
        assertThat(table.getContainerDataSource(), is(instanceOf(TableBinding.class)));
        TableBinding<?> tableBinding = (TableBinding<?>)table.getContainerDataSource();
        assertThat(bindingContext.getTableBindings(), hasItem(tableBinding));
    }

    @Test
    public void testCreateSection_SectionHasAddButtonInHeader() {
        TestTablePmo containerPmo = new TestTablePmo();
        BindingContext bindingContext = TestBindingContext.create();
        PmoBasedTableSectionFactory<TestRowPmo> factory = new PmoBasedTableSectionFactory<TestRowPmo>(
                containerPmo, bindingContext);

        TableSection<TestRowPmo> tableSection = factory.createSection();

        assertThat(tableSection, is(notNullValue()));
        assertThat(tableSection.getComponentCount(), is(2)); // header and table
        assertThat(tableSection.getComponent(0), is(instanceOf(HorizontalLayout.class)));
        HorizontalLayout header = (HorizontalLayout)tableSection.getComponent(0);
        assertThat(header.getComponentCount(), is(3)); // caption, add button and close button
        assertThat(header.getComponent(1), is(instanceOf(Button.class)));
        Button addButton = (Button)header.getComponent(1);
        assertThat(addButton.getIcon(), is(FontAwesome.PLUS));

    }

}
