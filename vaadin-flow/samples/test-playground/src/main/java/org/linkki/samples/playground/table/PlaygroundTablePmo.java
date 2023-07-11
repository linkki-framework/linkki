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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.icon.VaadinIcon;

@UISection(caption = "Table With Validation")
public class PlaygroundTablePmo extends SimpleTablePmo<TableModelObject, PlaygroundRowPmo> {

    private Handler addHandler;
    private Consumer<TableModelObject> deleteConsumer;

    public PlaygroundTablePmo(Supplier<List<? extends TableModelObject>> modelObjects,
            Handler addHandler,
            Consumer<TableModelObject> deleteConsumer) {
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
    public Optional<TableFooterPmo> getFooterPmo() {
        return Optional
                .of(c -> "index".contentEquals(c)
                        ? String.valueOf((Integer)getItems().stream()
                        .map(PlaygroundRowPmo::getModelObject)
                        .filter(Objects::nonNull)
                        .map(TableModelObject::getIndex).mapToInt(i -> i).sum())
                        : c + "Footer");
    }

    @Override
    public int getPageLength() {
        return 5;
    }

}
