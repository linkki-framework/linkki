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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.UITestConfiguration;
import org.openqa.selenium.By;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

@UITestConfiguration(locale = "en")
class TC004DynamicFieldTest extends PlaygroundUiTest {

    private static final String CAR_TABLE_PMO = "CarTablePmo" + "_table";
    private static final String OK = "ok";
    private static final String RETENTION = "retention";
    private static final String MODEL = "model";
    private static final String MAKE = "make";
    private static final String CAR_TYPE = "carType";
    private static final String BUTTON_PMO = "buttonPmo";

    @BeforeEach
    void goToTestCase() {
        goToTestCase(PlaygroundApplicationView.TS012, PlaygroundApplicationView.TC004);
    }

    @Test
    void testDynamicFields_InDialog() {
        // Select standard car type
        clickButton(BUTTON_PMO);

        $(DialogElement.class).waitForFirst().$(ComboBoxElement.class).id(CAR_TYPE).selectByText("Standard");
        // Must be a Textfield
        assertThat(findElement(By.cssSelector("#overlay #retention")).getTagName(), is("vaadin-text-field"));

        // Select premium car type
        $(DialogElement.class).first().$(ComboBoxElement.class).id(CAR_TYPE).selectByText("Premium");
        // Must be a Combobox
        assertThat(findElement(By.cssSelector("#overlay #retention")).getTagName(), is("vaadin-combo-box"));
    }

    @Test
    void testDynamicFields_InTableRow() {
        GridElement selectableTable = $(GridElement.class).id(CAR_TABLE_PMO);

        // Test, if in first row, with cartype 'Standard', the retention is a Textfield
        assertThat(selectableTable.getCell(0, 2).$(ComboBoxElement.class).first().getSelectedText(), is("Standard"));
        assertThat(selectableTable.getCell(0, 3).$(TextFieldElement.class).first(), is(not(nullValue())));

        // Test, if in first row, with cartype 'Premium', the retention is a Combobox
        assertThat(selectableTable.getCell(1, 2).$(ComboBoxElement.class).first().getSelectedText(), is("Premium"));
        assertThat(selectableTable.getCell(1, 3).$(ComboBoxElement.class).first(), is(not(nullValue())));
    }

    @Test
    void testDynamicTableFooter() throws ParseException {
        DecimalFormat format = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        GridElement selectableTable = $(GridElement.class).id(CAR_TABLE_PMO);
        double sumBeforeAdding = format.parse(selectableTable.getFooterCell(3).getText())
                .doubleValue();

        // Add premium car type
        clickButton(BUTTON_PMO);
        DialogElement dialogElement = $(DialogElement.class).waitForFirst();
        dialogElement.$(ComboBoxElement.class).id(CAR_TYPE).selectByText("Premium");
        dialogElement.$(TextFieldElement.class).id(MAKE).setValue("Fiat");
        dialogElement.$(ComboBoxElement.class).id(MODEL).selectByText("Sonstige");
        dialogElement.$(ComboBoxElement.class).id(RETENTION).selectByText(format.format(5000));
        clickButton(OK);

        // Check, if amount has changed
        assertThat(selectableTable.getFooterCell(2).getText(), is("Total Retention:"));
        assertThat(format.parse(selectableTable.getFooterCell(3).getText()).doubleValue(), is(sumBeforeAdding + 5000));
    }
}
