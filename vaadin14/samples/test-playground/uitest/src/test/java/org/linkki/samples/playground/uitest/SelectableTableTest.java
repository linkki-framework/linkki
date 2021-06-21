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
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.table.selection.PlaygroundSelectableTablePmo;
import org.linkki.samples.playground.table.selection.SelectionComparisonSectionPmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.openqa.selenium.By;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.grid.testbench.GridTRElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;


public class SelectableTableTest extends AbstractUiTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        openTab(PlaygroundApplicationView.TABLES_TAB_ID);
    }

    @Test
    public void testSelection() {
        GridElement selectableTable = $(GridElement.class)
                .id(PlaygroundSelectableTablePmo.class.getSimpleName() + "_table");

        // initial selection
        assertThat(selectableTable.getRow(PlaygroundSelectableTablePmo.INITAL_SELECTED_ROW).getAttribute("selected"),
                   is("true"));

        // set selection
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

    // TODO LIN-2343
    @Disabled("Double click on row does not select it")
    @Test
    public void testDoubleClick() {
        GridElement selectableTable = $(GridElement.class)
                .id(PlaygroundSelectableTablePmo.class.getSimpleName() + "_table");

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
