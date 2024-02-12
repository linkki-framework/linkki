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

package org.linkki.samples.playground.table.selection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.pmo.MultiSelectableTablePmo;
import org.linkki.samples.playground.table.PlaygroundRowPmo;
import org.linkki.samples.playground.table.TableModelObject;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.icon.VaadinIcon;

@UISection(caption = "Multi Selectable Table")
// tag::multi-selectable-table[]
public class PlaygroundMultiSelectableTablePmo extends SimpleTablePmo<TableModelObject, PlaygroundRowPmo>
        implements MultiSelectableTablePmo<PlaygroundRowPmo> {

    // end::multi-selectable-table[]
    private final Handler addHandler;
    private final Consumer<TableModelObject> deleteConsumer;
    // tag::multi-selectable-table[]
    private Set<PlaygroundRowPmo> selectedRows;

    // ...

    // end::multi-selectable-table[]
    protected PlaygroundMultiSelectableTablePmo(Supplier<List<? extends TableModelObject>> modelObjectsSupplier,
            Handler addHandler, Consumer<TableModelObject> deleteConsumer) {
        super(modelObjectsSupplier);
        this.addHandler = addHandler;
        this.deleteConsumer = deleteConsumer;
        this.selectedRows = new HashSet<>();
    }

    @Override
    protected PlaygroundRowPmo createRow(TableModelObject modelObject) {
        return new PlaygroundRowPmo(modelObject, () -> deleteConsumer.accept(modelObject));
    }

    @UIButton(position = 10, showIcon = true, icon = VaadinIcon.PLUS, caption = "")
    @SectionHeader
    public void add() {
        addHandler.apply();
    }

    @Override
    public int getPageLength() {
        return 7;
    }

    @SectionHeader
    @UILabel(position = -5)
    public int getNumberOfSelectedRows() {
        return selectedRows.size();
    }

    // tag::multi-selectable-table[]
    @Override
    public Set<PlaygroundRowPmo> getSelection() {
        return selectedRows;
    }

    @Override
    public void setSelection(Set<PlaygroundRowPmo> selectedRows) {
        this.selectedRows = selectedRows;
    }

}
// end::multi-selectable-table[]