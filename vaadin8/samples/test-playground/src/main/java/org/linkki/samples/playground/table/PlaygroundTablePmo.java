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

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.pmo.SelectableTablePmo;
import org.linkki.util.handler.Handler;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

@UISection(caption = "Selectable Table")
// tag::selectable-table[]
public class PlaygroundTablePmo extends SimpleTablePmo<TableModelObject, PlaygroundRowPmo>
        implements SelectableTablePmo<PlaygroundRowPmo> {

    public static final String NOTIFICATION_DOUBLE_CLICK = "Double clicked on ";

    private PlaygroundRowPmo selected;

    // ...

    // end::selectable-table[]

    public static final int INITAL_SELECTED_ROW = 2;


    private Handler addHandler;

    private Consumer<TableModelObject> deleteConsumer;

    public PlaygroundTablePmo(Supplier<List<? extends TableModelObject>> modelObjects,
            Handler addHandler,
            Consumer<TableModelObject> deleteConsumer) {
        super(modelObjects);
        this.addHandler = addHandler;
        this.deleteConsumer = deleteConsumer;
        selected = getItems().get(INITAL_SELECTED_ROW);
    }

    @Override
    protected PlaygroundRowPmo createRow(TableModelObject modelObject) {
        return new PlaygroundRowPmo(modelObject, () -> deleteConsumer.accept(modelObject));
    }

    @Override
    public Optional<TableFooterPmo> getFooterPmo() {
        return Optional.of(c -> c + "Footer");
    }

    @UIButton(position = 10, showIcon = true, icon = VaadinIcons.PLUS, caption = "", styleNames = ValoTheme.BUTTON_BORDERLESS)
    @SectionHeader
    public void add() {
        addHandler.apply();
    }

    // tag::selectable-table[]

    @Override
    public PlaygroundRowPmo getSelection() {
        return selected;
    }

    @Override
    public void setSelection(PlaygroundRowPmo selectedRow) {
        this.selected = selectedRow;
    }

    @Override
    public void onDoubleClick() {
        Notification.show(NOTIFICATION_DOUBLE_CLICK + selected.getModelObject().getName());
    }

}
// end::selectable-table[]
