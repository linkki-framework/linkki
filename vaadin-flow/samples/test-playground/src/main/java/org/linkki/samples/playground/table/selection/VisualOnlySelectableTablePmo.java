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

package org.linkki.samples.playground.table.selection;

import com.vaadin.flow.component.icon.VaadinIcon;
import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.aspects.annotation.BindTableSelection;
import org.linkki.samples.playground.table.PlaygroundRowPmo;
import org.linkki.samples.playground.table.TableModelObject;
import org.linkki.util.handler.Handler;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@UISection(caption = "Visual Only Selectable Table")
// tag::selectable-table[]
@BindTableSelection(visualOnly = true)
public class VisualOnlySelectableTablePmo extends SimpleTablePmo<TableModelObject, PlaygroundRowPmo> {

    // ...

    // end::selectable-table[]
    private Handler addHandler;
    private Consumer<TableModelObject> deleteConsumer;

    protected VisualOnlySelectableTablePmo(Supplier<List<? extends TableModelObject>> modelObjects,
            Handler addHandler, Consumer<TableModelObject> deleteConsumer) {
        super(modelObjects);
        this.addHandler = addHandler;
        this.deleteConsumer = deleteConsumer;
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
    // tag::selectable-table[]
}
// end::selectable-table[]