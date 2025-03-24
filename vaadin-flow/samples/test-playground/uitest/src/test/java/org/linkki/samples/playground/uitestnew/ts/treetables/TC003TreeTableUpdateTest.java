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

package org.linkki.samples.playground.uitestnew.ts.treetables;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.treetable.TreeTableUpdateNodePmo;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.TreeGridElement;

class TC003TreeTableUpdateTest extends PlaygroundUiTest {
    @BeforeEach
    void setup() {
        goToTestCase(TestScenarioView.TS014, TestScenarioView.TC003);
    }

    @Test
    void testDeleteAllChildren_ChildrenMustBeVisibleWhenRecreated() {
        TreeGridElement table = $(TreeGridElement.class).id(TreeTableUpdateNodePmo.class.getSimpleName() + "_table");

        addChildRow(table);

        assertThat(table.getRowCount(), is(3));

        table.expandWithClick(0);

        assertThat(table.getRowCount(), is(4));

        removeChildRow(table);

        assertThat(table.getRowCount(), is(3));

        addChildRow(table);

        assertThat(table.getRowCount(), is(4));
        assertThat(table.isRowExpanded(0, 0), is(true));
        // recreate initial state
        removeChildRow(table);
    }

    private void addChildRow(TreeGridElement table) {
        table.getCell(0, 1).$(ButtonElement.class).id("add").click();
    }

    private void removeChildRow(TreeGridElement table) {
        table.getCell(1, 2).$(ButtonElement.class).id("remove").click();
    }
}
