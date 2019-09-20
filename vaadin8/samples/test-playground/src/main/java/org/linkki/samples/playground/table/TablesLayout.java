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

package org.linkki.samples.playground.table;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.creation.table.PmoBasedTableFactory;
import org.linkki.samples.playground.table.MinimalSelectableTableSectionPmo.MinimalSelectableTableRowPmo;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.VerticalLayout;

public class TablesLayout extends VerticalLayout implements SidebarSheetDefinition {

    public static final String ID = "Table";

    private static final long serialVersionUID = 1L;

    public TablesLayout() {
        addSectionsForSelectableTable();
    }

    @SuppressWarnings("deprecation")
    private void addSectionsForSelectableTable() {
        BindingContext bindingContext = new BindingContext("selectableTable");
        MinimalSelectableTableSectionPmo selectableTableSectionPmo = new MinimalSelectableTableSectionPmo();

        com.vaadin.v7.ui.Table table = new PmoBasedTableFactory(selectableTableSectionPmo, bindingContext)
                .createTable();
        addComponent(table);

        BindingContext comparisonBindingContext = new BindingContext("selectableTableComparison");
        addComponent(VaadinUiCreator.createComponent(
                                                     new SelectableTableSelectionComparisonPmo(
                                                             () -> (MinimalSelectableTableRowPmo)table.getValue(),
                                                             () -> selectableTableSectionPmo.getSelection(),
                                                             comparisonBindingContext::modelChanged),
                                                     comparisonBindingContext));
    }

    @Override
    public String getSidebarSheetName() {
        return ID;
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.TABLE;
    }

    @Override
    public String getSidebarSheetId() {
        return ID;
    }
}
