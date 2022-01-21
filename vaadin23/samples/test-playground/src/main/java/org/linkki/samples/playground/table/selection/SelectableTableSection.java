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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.vaadin.component.section.GridSection;
import org.linkki.samples.playground.table.PlaygroundRowPmo;
import org.linkki.samples.playground.table.TableModelObject;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.selection.SingleSelect;

public class SelectableTableSection {

    private SelectableTableSection() {
        // prevents instantiation
    }

    public static Component create() {
        List<TableModelObject> modelObjects = IntStream.range(1, 10)
                .mapToObj(TableModelObject::new)
                .collect(Collectors.toList());
        PlaygroundSelectableTablePmo selectableTableSectionPmo = new PlaygroundSelectableTablePmo(
                () -> modelObjects,
                () -> modelObjects.add(new TableModelObject(modelObjects.size() + 1)),
                o -> modelObjects.remove(o));

        GridSection section = (GridSection)new PmoBasedSectionFactory()
                .createSection(selectableTableSectionPmo, new BindingContext("selectableTable"));

        SingleSelect<?, ?> singleSelect = section.getGrid().asSingleSelect();
        BindingContext comparisonBindingContext = new BindingContext("selectableTableComparison");
        Component comparisonSection = VaadinUiCreator.createComponent(
                                                                      new SelectionComparisonSectionPmo(
                                                                              () -> (PlaygroundRowPmo)singleSelect
                                                                                      .getValue(),
                                                                              () -> selectableTableSectionPmo
                                                                                      .getSelection(),
                                                                              comparisonBindingContext::modelChanged),
                                                                      comparisonBindingContext);
        section.add(comparisonSection);
        return section;
    }
}
