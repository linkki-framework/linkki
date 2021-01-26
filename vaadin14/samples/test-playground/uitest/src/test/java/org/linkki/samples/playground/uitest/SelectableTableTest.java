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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.table.PlaygroundTablePmo;
import org.linkki.samples.playground.table.SelectionComparisonSectionPmo;
import org.openqa.selenium.By;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.tabs.testbench.TabElement;


public class SelectableTableTest extends AbstractUiTest {

    private TabElement tablesTab;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        tablesTab = $(TabElement.class).all().stream().filter(t -> "Tables".equals(t.getText())).findFirst().get();
    }

    @AfterEach
    public void tearDown() {
        tablesTab = null;
    }

    @Test
    public void testSelection() {
        tablesTab.click();
        GridElement selectableTable = $(GridElement.class)
                .id(PlaygroundTablePmo.class.getSimpleName() + "_table");

        // initial selection
        assertThat(selectableTable.getRow(PlaygroundTablePmo.INITAL_SELECTED_ROW).getAttribute("selected"),
                   is("true"));

        // set selection
        // TODO LIN-2088 click() not working but doubleClick() does!?
        // selectableTable.getRow(1).click();
        selectableTable.getRow(1).select();
        // first row deselected
        assertThat(selectableTable.getRow(0).getAttribute("selected"), is(nullValue()));
        // second row selected
        assertThat(selectableTable.getRow(1).getAttribute("selected"), is("true"));

        clickButton(SelectionComparisonSectionPmo.PROPERTY_UPDATE_COMPARISON_VALUES);

        assertThat(findElement(By.id(SelectionComparisonSectionPmo.PROPERTY_PMO_SELECTION)).getText(),
                   is("Name 2"));
        assertThat(findElement(By.id(SelectionComparisonSectionPmo.PROPERTY_TABLE_SELECTION)).getText(),
                   is("Name 2"));

        // TODO LIN-2088 Selection by up-/down arrow keys not working yet?
        // change selection with key
        // selectableTable.getRow(2).select();
        // Actions action = new Actions(getDriver());
        // action.sendKeys(Keys.ARROW_UP).perform();
        // assertThat(selectableTable.getRow(1).getAttribute("selected"), is("true"));
        //
        // clickButton(SelectionComparisonSectionPmo.PROPERTY_UPDATE_COMPARISON_VALUES);
        // assertThat(findElement(By.id(SelectionComparisonSectionPmo.PROPERTY_PMO_SELECTION)).getText(),
        // is("Name 2"));
        // assertThat(findElement(By.id(SelectionComparisonSectionPmo.PROPERTY_TABLE_SELECTION)).getText(),
        // is("Name 2"));
    }

    // TODO LIN-2088 doubleClick doesn't work as intended: while a textfield has focus a doubleClick on
    // another
    // @Test
    // public void testDoubleClick() {
    // tablesTab.click();
    // GridElement selectableTable = $(GridElement.class)
    // .id(PlaygroundTablePmo.class.getSimpleName() + "_table");
    //
    // // single click should not trigger the aspect
    // selectableTable.getRow(0).click();
    // selectableTable.getRow(0).select();
    // List<NotificationElement> notificationsAfterSingleClick = $(NotificationElement.class).all();
    // assertThat(notificationsAfterSingleClick.isEmpty(), is(true));
    //
    // // double click should first set selection then trigger the aspect
    // selectableTable.getRow(1).doubleClick();
    // assertThat(selectableTable.getRow(1).getAttribute("selected"), is("true"));
    // List<NotificationElement> notificationsAfterDoubleClick = $(NotificationElement.class).all();
    // assertThat(notificationsAfterDoubleClick.size(), is(1));
    // assertThat(notificationsAfterDoubleClick.get(0).getText(),
    // containsString(PlaygroundTablePmo.NOTIFICATION_DOUBLE_CLICK));
    // assertThat(notificationsAfterDoubleClick.get(0).getText(),
    // containsString("Name 2"));
    // }
}
