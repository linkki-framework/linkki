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

package org.linkki.samples.playground.uitest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.table.uitablecolumn.UITableColumnTableSection.UITableColumnTablePmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationUI;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.grid.testbench.GridTHTDElement;

public class UITableColumnTest extends AbstractUiTest {

    @BeforeEach
    public void openTablesTab() {
        openTab(PlaygroundApplicationUI.TABLES_TAB_ID);
    }

    @Test
    public void testWidth() {
        GridElement table = $(GridElement.class).id(UITableColumnTablePmo.class.getSimpleName() + "_table");
        assertThat(table.getCell(0, 0).getSize().getWidth(), is(50));
    }

    @Test
    public void testNoAnnotation_ColumnsHaveSameWidth() {
        GridElement table = $(GridElement.class).id(UITableColumnTablePmo.class.getSimpleName() + "_table");
        GridTHTDElement cellWithoutAnnotation1 = table.getCell(0, 2);
        GridTHTDElement cellWithoutAnnotation2 = table.getCell(0, 3);

        // Selenium rounds to the next integer, this may lead to rounding error
        assertThat(cellWithoutAnnotation1.getSize().getWidth() - cellWithoutAnnotation2.getSize().getWidth(),
                   is(lessThanOrEqualTo(1)));
    }

    @Test
    public void testFlexGrow() {
        GridElement table = $(GridElement.class).id(UITableColumnTablePmo.class.getSimpleName() + "_table");
        // columns with undefined width gets 100px internally
        int excessWidthAssignedToColumnWithNoAnnotation = table.getCell(0, 3).getSize().getWidth() - 100;
        int excessWidthAssingedToColumnWithFlexGrow3 = table.getCell(0, 1).getSize().getWidth() - 100;

        assertThat(excessWidthAssingedToColumnWithFlexGrow3 / excessWidthAssignedToColumnWithNoAnnotation, is(3));
    }

    @Test
    public void testWidthAndFlexGrow() {
        GridElement table = $(GridElement.class).id(UITableColumnTablePmo.class.getSimpleName() + "_table");

        // columns with undefined width gets 100px internally
        int excessWidthAssignedToColumnWithNoAnnotation = table.getCell(0, 3).getSize().getWidth() - 100;
        int excessWidthAssingedToColumnWithFlexGrow1AndWidth200 = table.getCell(0, 4).getSize().getWidth() - 200;


        // Selenium rounds to the next integer, this may lead to rounding error
        assertThat(excessWidthAssignedToColumnWithNoAnnotation - excessWidthAssingedToColumnWithFlexGrow1AndWidth200,
                   is(lessThanOrEqualTo(1)));
    }
}
