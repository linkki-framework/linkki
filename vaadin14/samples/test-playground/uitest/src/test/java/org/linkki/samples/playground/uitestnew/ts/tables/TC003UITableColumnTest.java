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

package org.linkki.samples.playground.uitestnew.ts.tables;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.table.uitablecolumn.UITableColumnTablePmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.grid.testbench.GridTHTDElement;

class TC003UITableColumnTest extends PlaygroundUiTest {

    @BeforeEach
    void setup() {
        super.setUp();
        goToTestCase("TS012", "TC003");
    }

    @Test
    void testWidth() {
        GridElement table = getTable();
        assertThat(table.getCell(0, 0).getSize().getWidth(), is(50));
    }

    @Test
    void testNoAnnotation_ColumnsHaveSameWidth() {
        GridElement table = getTable();
        GridTHTDElement cellWithoutAnnotation1 = table.getCell(0, 2);
        GridTHTDElement cellWithoutAnnotation2 = table.getCell(0, 3);

        // Selenium rounds to the next integer, this may lead to rounding error
        assertThat(cellWithoutAnnotation1.getSize().getWidth() - cellWithoutAnnotation2.getSize().getWidth(),
                   is(lessThanOrEqualTo(1)));
    }

    @Test
    void testFlexGrow() {
        GridElement table = getTable();
        // columns with undefined width gets 100px internally
        double excessWidthAssignedToColumnWithNoAnnotation = table.getCell(0, 3).getSize().getWidth() - 100;
        double excessWidthAssingedToColumnWithFlexGrow3 = table.getCell(0, 1).getSize().getWidth() - 100;

        assertThat(Math.round(excessWidthAssingedToColumnWithFlexGrow3 / excessWidthAssignedToColumnWithNoAnnotation),
                   is(3L));
    }

    @Test
    void testWidthAndFlexGrow() {
        GridElement table = getTable();

        // columns with undefined width gets 100px internally
        int excessWidthAssignedToColumnWithNoAnnotation = table.getCell(0, 3).getSize().getWidth() - 100;
        int excessWidthAssingedToColumnWithFlexGrow1AndWidth200 = table.getCell(0, 4).getSize().getWidth() - 200;


        // Selenium rounds to the next integer, this may lead to rounding error
        assertThat(excessWidthAssignedToColumnWithNoAnnotation - excessWidthAssingedToColumnWithFlexGrow1AndWidth200,
                   is(lessThanOrEqualTo(1)));
    }

    private GridElement getTable() {
        GridElement table = $(GridElement.class).id(UITableColumnTablePmo.class.getSimpleName() + "_table");
        table.scrollIntoView();
        return table;
    }
}
