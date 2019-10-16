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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.SelectableTablePmo;
import org.linkki.samples.playground.table.MinimalSelectableTableSectionPmo.MinimalSelectableTableRowPmo;

import com.vaadin.ui.Notification;

@UISection(caption = "Selectable Table")
// tag::selectable-table[]
public class MinimalSelectableTableSectionPmo implements SelectableTablePmo<MinimalSelectableTableRowPmo> {

    public static final String NOTIFICATION_DOUBLE_CLICK = "Double clicked on ";
    // end::selectable-table[]
    public static final int INITAL_SELECTED_ROW = 2;
    public static final String ROW_1 = "row1";
    public static final String ROW_2 = "row2";
    public static final String ROW_3 = "row3";

    private final List<MinimalSelectableTableRowPmo> rows;
    private MinimalSelectableTableRowPmo selected;

    public MinimalSelectableTableSectionPmo() {
        this(createSampleRows(), createSampleRows().get(INITAL_SELECTED_ROW));
    }

    private static List<MinimalSelectableTableRowPmo> createSampleRows() {
        return Arrays.asList(ROW_1, ROW_2, ROW_3).stream().map(MinimalSelectableTableRowPmo::new)
                .collect(Collectors.toList());
    }
    // tag::selectable-table[]

    public MinimalSelectableTableSectionPmo(List<MinimalSelectableTableRowPmo> rows,
            MinimalSelectableTableRowPmo initiallySelectedRow) {
        this.rows = rows;
        this.selected = initiallySelectedRow;
    }

    @Override
    public MinimalSelectableTableRowPmo getSelection() {
        return selected;
    }

    @Override
    public void setSelection(MinimalSelectableTableRowPmo selectedRow) {
        this.selected = selectedRow;
    }

    @Override
    public void onDoubleClick() {
        Notification.show(NOTIFICATION_DOUBLE_CLICK + selected.getValue());
    }

    @Override
    public List<MinimalSelectableTableRowPmo> getItems() {
        return rows;
    }

    // end::selectable-table[]
    public static class MinimalSelectableTableRowPmo {

        private String value;

        public MinimalSelectableTableRowPmo(String value) {
            this.value = value;
        }

        @UILabel(position = 10)
        public String getValue() {
            return value;
        }
    }

    // tag::selectable-table[]
}
// end::selectable-table[]
