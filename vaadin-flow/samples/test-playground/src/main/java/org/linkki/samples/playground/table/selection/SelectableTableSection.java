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

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vaadin.flow.theme.lumo.LumoUtility;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.section.GridSection;
import org.linkki.samples.playground.table.PlaygroundRowPmo;
import org.linkki.samples.playground.table.TableModelObject;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

public class SelectableTableSection {

    private SelectableTableSection() {
        // prevents instantiation
    }

    public static Component create() {
        var wrapper = new Div();

        var modelObjects = IntStream.range(1, 10)
                .mapToObj(TableModelObject::new)
                .collect(Collectors.toList());
        var selectableTableSectionPmo = new PlaygroundSelectableTablePmo(
                () -> modelObjects,
                () -> modelObjects.add(new TableModelObject(modelObjects.size() + 1)),
                modelObjects::remove);
        var gridSection = VaadinUiCreator.createComponent(selectableTableSectionPmo, new BindingContext());
        wrapper.add(gridSection);

        var singleSelect = ((GridSection)gridSection).getGrid().asSingleSelect();
        var comparisonSectionPmo = new SelectionComparisonSectionPmo(
                () -> (PlaygroundRowPmo)singleSelect.getValue(), selectableTableSectionPmo::getSelection);
        wrapper.add(VaadinUiCreator.createComponent(comparisonSectionPmo, new BindingContext()));

        var visualOnlySelectableTablePmo = new VisualOnlySelectableTablePmo(
                () -> modelObjects,
                () -> modelObjects.add(new TableModelObject(modelObjects.size() + 1)),
                modelObjects::remove);
        wrapper.add(VaadinUiCreator.createComponent(visualOnlySelectableTablePmo, new BindingContext()));

        var multiSelectableTablePmo = new PlaygroundMultiSelectableTablePmo(
                () -> modelObjects,
                () -> modelObjects.add(new TableModelObject(modelObjects.size() + 1)),
                modelObjects::remove);
        wrapper.add(VaadinUiCreator.createComponent(multiSelectableTablePmo, new BindingContext()));

        wrapper.setWidthFull();
        wrapper.getClassNames().add(LumoUtility.Display.FLEX);
        wrapper.getClassNames().add(LumoUtility.FlexDirection.COLUMN);
        wrapper.getClassNames().add(LumoUtility.Gap.LARGE);
        return wrapper;
    }
}
