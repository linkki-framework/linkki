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

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.grid.testbench.GridTRElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.table.selection.PlaygroundSelectableTablePmo;
import org.linkki.samples.playground.table.selection.SelectionComparisonSectionPmo;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiTextElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

class TC002SelectableTableTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS012, TestScenarioView.TC002);
    }

    @Test
    void testSelection() {
        GridElement selectableTable = getGrid(PlaygroundSelectableTablePmo.class);

        // initial selection
        assertThat(selectableTable.getRow(PlaygroundSelectableTablePmo.INITAL_SELECTED_ROW).getAttribute("selected"),
                is("true"));

        // set selection
        selectableTable.getRow(1).select();
        // first row deselected
        assertThat(selectableTable.getRow(0).isSelected(), is(false));
        // second row selected
        assertThat(selectableTable.getRow(1).isSelected(), is(true));

        clickButton(SelectionComparisonSectionPmo.PROPERTY_UPDATE_COMPARISON_VALUES);

        assertThat($(LinkkiTextElement.class).id(SelectionComparisonSectionPmo.PROPERTY_PMO_SELECTION).getText(),
                is("Name 2"));
        assertThat($(LinkkiTextElement.class).id(SelectionComparisonSectionPmo.PROPERTY_TABLE_SELECTION).getText(),
                is("Name 2"));

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

        // single click should not trigger the aspect
        selectableTable.getRow(0).select();
        List<NotificationElement> notificationsAfterSingleClick = $(NotificationElement.class).all();
        assertThat(notificationsAfterSingleClick.isEmpty(), is(true));

        selectableTable.scrollIntoView();
        GridTRElement row = selectableTable.getRow(3);
        row.select();
        // double click does not select the row?
        row.doubleClick();
        assertThat(row.isSelected(), is(true));
        List<NotificationElement> notificationsAfterDoubleClick = $(NotificationElement.class).all();
        assertThat(notificationsAfterDoubleClick.size(), is(1));
        assertThat(notificationsAfterDoubleClick.get(0).getText(),
                containsString(PlaygroundSelectableTablePmo.NOTIFICATION_DOUBLE_CLICK));
        assertThat(notificationsAfterDoubleClick.get(0).getText(),
                containsString("Name 4"));
    }
}
