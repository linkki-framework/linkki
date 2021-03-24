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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.table.PlaygroundTablePmo;
import org.linkki.samples.playground.table.SelectionComparisonSectionPmo;
import org.linkki.samples.playground.table.TablePage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.NotificationElement;

@SuppressWarnings("deprecation")
public class SelectableTableTest extends AbstractUiTest {

    @BeforeEach
    public void selectPage() {
        clickButton(TablePage.ID);
    }

    @Test
    public void testSelection() {
        com.vaadin.testbench.elements.TableElement selectableTable = $(com.vaadin.testbench.elements.TableElement.class)
                .id(PlaygroundTablePmo.class.getSimpleName() + "_table");

        // initial selection
        assertThat(selectableTable.getRow(PlaygroundTablePmo.INITAL_SELECTED_ROW).getClassNames(),
                   hasItem("v-selected"));

        // set selection
        selectableTable.getRow(0).click(1, 1);
        assertThat(selectableTable.getRow(0).getClassNames(), hasItem("v-selected"));

        clickButton(SelectionComparisonSectionPmo.PROPERTY_UPDATE_COMPARISON_VALUES);

        assertThat($(LabelElement.class).id(SelectionComparisonSectionPmo.PROPERTY_PMO_SELECTION).getText(),
                   is("Name 1"));
        assertThat($(LabelElement.class).id(SelectionComparisonSectionPmo.PROPERTY_TABLE_SELECTION)
                .getText(),
                   is("Name 1"));

        // change selection with key
        selectableTable.getRow(2).click(1, 1);
        Actions action = new Actions(getDriver());
        action.sendKeys(Keys.ARROW_UP).perform();
        assertThat(selectableTable.getRow(1).getClassNames(), hasItem("v-selected"));

        clickButton(SelectionComparisonSectionPmo.PROPERTY_UPDATE_COMPARISON_VALUES);
        assertThat($(LabelElement.class).id(SelectionComparisonSectionPmo.PROPERTY_PMO_SELECTION).getText(),
                   is("Name 2"));
        assertThat($(LabelElement.class).id(SelectionComparisonSectionPmo.PROPERTY_TABLE_SELECTION)
                .getText(),
                   is("Name 2"));
    }

    @Test
    public void testDoubleClick() {
        com.vaadin.testbench.elements.TableElement selectableTable = $(com.vaadin.testbench.elements.TableElement.class)
                .id(PlaygroundTablePmo.class.getSimpleName() + "_table");

        // single click should not trigger the aspect
        selectableTable.getRow(0).click(1, 1);
        List<NotificationElement> notificationsAfterSingleClick = $(NotificationElement.class).all();
        assertThat(notificationsAfterSingleClick, hasSize(0));

        // double click should first set selection then trigger the aspect
        selectableTable.getRow(1).getCell(1).doubleClick();
        assertThat(selectableTable.getRow(1).getClassNames(), hasItem("v-selected"));
        List<NotificationElement> notificationsAfterDoubleClick = $(NotificationElement.class).all();
        assertThat(notificationsAfterDoubleClick, hasSize(1));
        assertThat(notificationsAfterDoubleClick.get(0).getCaption(),
                   containsString(PlaygroundTablePmo.NOTIFICATION_DOUBLE_CLICK));
        assertThat(notificationsAfterDoubleClick.get(0).getCaption(),
                   containsString("Name 2"));
    }
}
