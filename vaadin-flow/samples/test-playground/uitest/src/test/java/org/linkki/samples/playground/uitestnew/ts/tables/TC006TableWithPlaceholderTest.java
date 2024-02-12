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

package org.linkki.samples.playground.uitestnew.ts.tables;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.aspects.PlaceholderAspectDefinition;
import org.linkki.samples.playground.table.SimplePlaygroundTablePmo.TableWithEmptyPlaceholderPmo;
import org.linkki.samples.playground.table.SimplePlaygroundTablePmo.TableWithInheritedPlaceholderPmo;
import org.linkki.samples.playground.table.SimplePlaygroundTablePmo.TableWithPlaceholderPmo;
import org.linkki.samples.playground.table.SimplePlaygroundTablePmo.TableWithoutPlaceholderPmo;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.GridColumnElement;
import com.vaadin.flow.component.grid.testbench.GridElement;

class TC006TableWithPlaceholderTest extends PlaygroundUiTest {

    private static final String HAS_ITEMS_ATTRIBUTE = "has-items";

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS012, TestScenarioView.TC006);
    }

    @Test
    void testTable_WithPlaceholder() {
        GridElement table = getGrid(TableWithPlaceholderPmo.class);

        assertThat(table.getRowCount()).isEqualTo(1);
        assertThat(table.hasAttribute(HAS_ITEMS_ATTRIBUTE)).isEqualTo(true);
        assertThat(table.hasAttribute(PlaceholderAspectDefinition.HAS_PLACEHOLDER)).isEqualTo(true);
        int heightBeforeDeletion = table.getSize().getHeight();

        deleteRow(table);

        assertThat(table.getRowCount()).isEqualTo(0);
        assertThat(table.hasAttribute(HAS_ITEMS_ATTRIBUTE)).isEqualTo(false);
        assertThat(table.getSize().getHeight())
                .describedAs("The table itself should not be shown anymore.").isLessThan(heightBeforeDeletion);
        assertThat(table.getSize().getHeight())
                .describedAs("Height should not be 0 as the placeholder text should be shown.").isNotEqualTo(0);
    }

    @Test
    void testTable_WithInheritedPlaceholder() {
        GridElement table = getGrid(TableWithInheritedPlaceholderPmo.class);

        assertThat(table.getRowCount()).isEqualTo(1);
        assertThat(table.hasAttribute(HAS_ITEMS_ATTRIBUTE)).isEqualTo(true);
        assertThat(table.hasAttribute(PlaceholderAspectDefinition.HAS_PLACEHOLDER)).isEqualTo(true);
        int heightBeforeDeletion = table.getSize().getHeight();

        deleteRow(table);

        assertThat(table.getRowCount()).isEqualTo(0);
        assertThat(table.hasAttribute(HAS_ITEMS_ATTRIBUTE)).isEqualTo(false);
        assertThat(table.getSize().getHeight())
                .describedAs("The table itself should not be shown anymore.").isLessThan(heightBeforeDeletion);
        assertThat(table.getSize().getHeight())
                .describedAs("Height should not be 0 as the placeholder text should be shown.").isNotEqualTo(0);
    }

    @Test
    void testTable_WithEmptyPlaceholder() {
        GridElement table = getGrid(TableWithEmptyPlaceholderPmo.class);

        assertThat(table.getRowCount()).isEqualTo(1);
        assertThat(table.hasAttribute(HAS_ITEMS_ATTRIBUTE)).isEqualTo(true);
        assertThat(table.hasAttribute(PlaceholderAspectDefinition.HAS_PLACEHOLDER)).isEqualTo(true);

        deleteRow(table);

        assertThat(table.getRowCount()).isEqualTo(0);
        assertThat(table.hasAttribute(HAS_ITEMS_ATTRIBUTE)).isEqualTo(false);
        assertThat(table.getSize().getHeight())
                .describedAs("The table should not be shown anymore. Not placeholder text should be shown.")
                .isEqualTo(0);
    }

    @Test
    void testTable_WithoutPlaceholder() {
        GridElement table = getGrid(TableWithoutPlaceholderPmo.class);

        assertThat(table.getRowCount()).isEqualTo(1);
        assertThat(table.hasAttribute(HAS_ITEMS_ATTRIBUTE)).isEqualTo(true);
        assertThat(table.hasAttribute(PlaceholderAspectDefinition.HAS_PLACEHOLDER)).isEqualTo(false);
        int heightBeforeDeletion = table.getSize().getHeight();

        deleteRow(table);

        assertThat(table.getRowCount()).isEqualTo(0);
        assertThat(table.hasAttribute(HAS_ITEMS_ATTRIBUTE)).isEqualTo(false);
        assertThat(table.getSize().getHeight())
                .describedAs("Tables that are not annotated with @BindPlaceholder should behave as is if empty.")
                .isEqualTo(heightBeforeDeletion);
    }

    private void deleteRow(GridElement table) {
        GridColumnElement gridColumnElement = table.getVisibleColumns().stream()
                .filter(c -> c.getHeaderCell().getText().equals("Del")).findFirst().get();
        table.getRow(0).getCell(gridColumnElement).$(ButtonElement.class).id("delete").click();
    }
}
