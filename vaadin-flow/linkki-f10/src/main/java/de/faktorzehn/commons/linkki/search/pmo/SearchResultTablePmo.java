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
package de.faktorzehn.commons.linkki.search.pmo;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.table.pmo.SelectableTablePmo;

import de.faktorzehn.commons.linkki.search.annotation.UISearchTable;

/**
 * @deprecated moved to linkki-search-vaadin-flow. Use org.linkki.search.pmo.SearchResultPmo instead
 */
@Deprecated(since = "2.8.0")
@UISearchTable
public class SearchResultTablePmo<T, ROW> extends SimpleTablePmo<T, ROW> implements SelectableTablePmo<ROW> {

    private final Function<T, ROW> rowCreator;
    private final Consumer<ROW> primaryAction;

    private final Class<? extends ROW> rowClass;

    private ROW selectedRow;

    public SearchResultTablePmo(Supplier<List<? extends T>> modelObjectSupplier,
            Function<T, ROW> rowCreator,
            Class<? extends ROW> rowClass,
            Consumer<ROW> primaryAction) {
        super(modelObjectSupplier);
        this.rowCreator = rowCreator;
        this.primaryAction = primaryAction;
        this.rowClass = rowClass;
    }

    @Override
    protected ROW createRow(T modelObject) {
        return rowCreator.apply(modelObject);
    }

    @Override
    public Class<? extends ROW> getItemPmoClass() {
        return rowClass;
    }

    @Override
    public int getPageLength() {
        return 0;
    }

    @Override
    public ROW getSelection() {
        final List<ROW> items = getItems();
        if (!items.contains(selectedRow)) {
            if (!items.isEmpty()) {
                setSelection(getItems().get(0));
            }
        }
        return selectedRow;
    }

    @Override
    public void onDoubleClick() {
        if (selectedRow != null) {
            primaryAction.accept(selectedRow);
        }
    }

    @Override
    public void setSelection(ROW selectedRow) {
        if (selectedRow != null) {
            this.selectedRow = selectedRow;
        }
    }

}
