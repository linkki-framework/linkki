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

import java.util.Optional;
import java.util.function.Supplier;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.samples.playground.table.PlaygroundRowPmo;
import org.linkki.util.handler.Handler;

@UIHorizontalLayout
public class SelectionComparisonSectionPmo {

    public static final String PROPERTY_TABLE_SELECTION = "tableSelection";
    public static final String PROPERTY_PMO_SELECTION = "pmoSelection";
    public static final String PROPERTY_UPDATE_COMPARISON_VALUES = "updateComparisonValues";

    private final Supplier<PlaygroundRowPmo> tableSelection;

    private final Supplier<PlaygroundRowPmo> pmoSelection;

    private final Handler updateComparisonValues;

    public SelectionComparisonSectionPmo(Supplier<PlaygroundRowPmo> tableSelection,
            Supplier<PlaygroundRowPmo> pmoSelection, Handler updateComparisonValues) {
        this.tableSelection = tableSelection;
        this.pmoSelection = pmoSelection;
        this.updateComparisonValues = updateComparisonValues;
    }

    @UILabel(position = 10, label = "Table selection:")
    public String getTableSelection() {
        return Optional.ofNullable(tableSelection.get()).map(o -> o.getModelObject().getName())
                .orElse("null");
    }

    @UILabel(position = 20, label = "Pmo selection:")
    public String getPmoSelection() {
        return Optional.ofNullable(pmoSelection.get()).map(o -> o.getModelObject().getName())
                .orElse("null");
    }

    @UIButton(position = 30, caption = "Update comparison values")
    public void updateComparisonValues() {
        updateComparisonValues.apply();
    }
}
