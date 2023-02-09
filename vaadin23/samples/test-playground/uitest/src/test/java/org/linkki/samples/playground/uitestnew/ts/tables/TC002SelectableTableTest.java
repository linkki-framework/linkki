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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.table.selection.PlaygroundSelectableTablePmo;
import org.linkki.samples.playground.table.selection.SelectionComparisonSectionPmo;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiTextElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.grid.testbench.GridTRElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;

class TC002SelectableTableTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS012, TestScenarioView.TC002);
    }

    @Test
    void testSelection() {
        GridElement selectableTable = getGrid(PlaygroundSelectableTablePmo.class);

        assertThat("First row should be initially selected",
                selectableTable.getRow(PlaygroundSelectableTablePmo.INITAL_SELECTED_ROW).getAttribute("selected"),
                is("true"));

        selectableTable.getRow(1).select();

        assertThat("After selecting a different row, the previously selected row should not be selected anymore",
                selectableTable.getRow(0).isSelected(),
                is(false));
        assertThat("After selecting a different row, the new row should be selected",
                selectableTable.getRow(1).isSelected(),
                is(true));

        clickButton(SelectionComparisonSectionPmo.PROPERTY_UPDATE_COMPARISON_VALUES);

        assertThat("The selected row in the PMO should be the same as the selected row in the table",
                $(LinkkiTextElement.class).id(SelectionComparisonSectionPmo.PROPERTY_PMO_SELECTION).getText(),
                allOf(is("Name 2"),
                        is($(LinkkiTextElement.class).id(SelectionComparisonSectionPmo.PROPERTY_TABLE_SELECTION).getText())));

        // change selection with key
        selectableTable.getCell(2, 1).click();
        selectableTable.getCell(2, 1).focus();
        assertThat(selectableTable.getRow(2).isSelected(), is(true));
        Actions action = new Actions(getDriver());
        action.sendKeys(Keys.ARROW_UP).perform();
        action.sendKeys(Keys.SPACE).perform();
        assertThat(selectableTable.getRow(1).isSelected(), is(true));

        clickButton(SelectionComparisonSectionPmo.PROPERTY_UPDATE_COMPARISON_VALUES);
        assertThat($(LinkkiTextElement.class).id(SelectionComparisonSectionPmo.PROPERTY_PMO_SELECTION).getText(),
                is("Name 2"));
        assertThat($(LinkkiTextElement.class).id(SelectionComparisonSectionPmo.PROPERTY_TABLE_SELECTION).getText(),
                is("Name 2"));
    }

    @Test
    void testDoubleClick() {
        GridElement selectableTable = getGrid(PlaygroundSelectableTablePmo.class);

        selectableTable.getRow(0).select();

        assertThat("Single click should not trigger the aspect", $(NotificationElement.class).all(), is(empty()));

        selectableTable.scrollIntoView();
        GridTRElement row = selectableTable.getRow(3);
        row.doubleClick();

        assertThat("Double click should also select the row", row.isSelected(), is(true));
        List<NotificationElement> notificationsAfterDoubleClick = $(NotificationElement.class).all();
        assertThat("Double click aspect should be triggered", notificationsAfterDoubleClick, hasSize(1));
        assertThat("Double click should be executed on the correct row",
                notificationsAfterDoubleClick.get(0).getText(),
                allOf(containsString(PlaygroundSelectableTablePmo.NOTIFICATION_DOUBLE_CLICK),
                        containsString("Name 4")));
    }

    @Test
    void testUnselectShouldNotBePossible() {
        GridElement selectableTable = getGrid(PlaygroundSelectableTablePmo.class);
        selectableTable.getRow(0).select();
        clickButton(SelectionComparisonSectionPmo.PROPERTY_UPDATE_COMPARISON_VALUES);
        assertThat($(LinkkiTextElement.class).id(SelectionComparisonSectionPmo.PROPERTY_PMO_SELECTION).getText(),
                is("Name 1"));
        assertThat($(LinkkiTextElement.class).id(SelectionComparisonSectionPmo.PROPERTY_TABLE_SELECTION).getText(),
                is("Name 1"));
        assertThat(selectableTable.getRow(0).isSelected(), is(true));

        selectableTable.getRow(0).select();

        clickButton(SelectionComparisonSectionPmo.PROPERTY_UPDATE_COMPARISON_VALUES);
        assertThat($(LinkkiTextElement.class).id(SelectionComparisonSectionPmo.PROPERTY_PMO_SELECTION).getText(),
                is("Name 1"));
        assertThat($(LinkkiTextElement.class).id(SelectionComparisonSectionPmo.PROPERTY_TABLE_SELECTION).getText(),
                is("Name 1"));
        assertThat(selectableTable.getRow(0).isSelected(), is(true));
    }
}
