/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.core.ui.table;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.binding.BindingContext;

import com.vaadin.ui.Table;

public class PmoBasedTableFactoryTest {

    private BindingContext ctx = new BindingContext();

    @Test
    public void testCreateTable_InitialPageLengthIsSetOnTable() {
        TestTablePmo containerPmo = new TestTablePmo();
        PmoBasedTableFactory<TestRowPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx);
        Table table = factory.createTable();
        assertThat(table.getPageLength(), is(ContainerPmo.DEFAULT_PAGE_LENGTH));
    }

    @Test
    public void testCreateTable_ItemsAreBound() {
        TestTablePmo containerPmo = new TestTablePmo();
        TestRowPmo rowPmo1 = containerPmo.addItem();
        TestRowPmo rowPmo2 = containerPmo.addItem();
        assertThat(containerPmo.getItems(), contains(rowPmo1, rowPmo2));

        PmoBasedTableFactory<TestRowPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx);
        Table table = factory.createTable();

        assertThat(table.getItemIds(), contains(rowPmo1, rowPmo2));
    }


}
