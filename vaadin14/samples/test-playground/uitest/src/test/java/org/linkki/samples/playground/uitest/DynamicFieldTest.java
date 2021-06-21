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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitest.extensions.DriverExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

@DriverExtension.Configuration(locale = "en")
public class DynamicFieldTest extends AbstractUiTest {

    private static final String CAR_TABLE_PMO = "CarTablePmo" + "_table";
    private static final String OK = "ok";
    private static final String RETENTION = "retention";
    private static final String MODEL = "model";
    private static final String MAKE = "make";
    private static final String CAR_TYPE = "carType";
    private static final String BUTTON_PMO = "buttonPmo";

    @BeforeEach
    public void before() {
        openTab(PlaygroundApplicationView.TABLES_TAB_ID);
    }

    @Test
    public void testDynamicFields_newCarDialog() {
        // Select standard car type
        clickButton(BUTTON_PMO);

        waitUntil(ExpectedConditions.elementToBeClickable(By.id(CAR_TYPE)));
        selectCombobox(CAR_TYPE, "Standard");

        waitUntil(ExpectedConditions.elementToBeClickable(By.id(RETENTION)));
        // Must be a Textfield
        assertThat($(TextFieldElement.class).all().stream().filter(tf -> RETENTION.equals(tf.getAttribute("id")))
                .findFirst().isPresent(), is(true));

        // Select premium car type
        selectCombobox(CAR_TYPE, "Premium");
        // Must be a Combobox
        assertThat($(ComboBoxElement.class).all().stream().filter(tf -> RETENTION.equals(tf.getAttribute("id")))
                .findFirst().isPresent(), is(true));
    }

    @Test
    public void testDynamicFields_carTablePmo() {
        GridElement selectableTable = $(GridElement.class)
                .id(CAR_TABLE_PMO);

        // Test, if in first row, with cartype 'Standard', the retention is a Textfield
        assertThat(selectableTable.getCell(0, 2).$(ComboBoxElement.class).first().getSelectedText(), is("Standard"));
        assertThat(selectableTable.getCell(0, 3).$(TextFieldElement.class).first(), is(not(nullValue())));

        // Test, if in first row, with cartype 'Premium', the retention is a Combobox
        assertThat(selectableTable.getCell(1, 2).$(ComboBoxElement.class).first().getSelectedText(), is("Premium"));
        assertThat(selectableTable.getCell(1, 3).$(ComboBoxElement.class).first(), is(not(nullValue())));
    }

    @Test
    public void testDynamicTableFooter_carTablePmo() {
        GridElement selectableTable = $(GridElement.class).id(CAR_TABLE_PMO);
        assertThat(selectableTable.getFooterCell(3).getText(), is("15,500.00"));

        // Add premium car type
        clickButton(BUTTON_PMO);
        selectCombobox(CAR_TYPE, "Premium");
        typeInTextBox(MAKE, "Fiat");
        selectCombobox(MODEL, "Sonstige");
        selectCombobox(RETENTION, "5,000.00");
        clickButton(OK);

        // Check, if amount has changed
        assertThat(selectableTable.getFooterCell(2).getText(), is("Total Retention:"));
        assertThat(selectableTable.getFooterCell(3).getText(), is("20,500.00"));
    }
}
