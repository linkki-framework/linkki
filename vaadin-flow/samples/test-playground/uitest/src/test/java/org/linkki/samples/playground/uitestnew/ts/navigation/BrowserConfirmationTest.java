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

package org.linkki.samples.playground.uitestnew.ts.navigation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class BrowserConfirmationTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS019, TestScenarioView.TC001);
    }

    @Disabled(
            value = "ExpectedConditions.alertIsPresent closes alerts automatically since migration from chromedriver 177*.")
    @Test
    void testBrowserConfirmationDialog() throws InterruptedException {

        $(ButtonElement.class).id("goToView").click();

        // wait for new view to be shown
        waitForElementVisible(By.ById.id("EditSaveButtonPmo"));

        // reload page without confirmation
        reload();

        waitForElementVisible(By.ById.id("EditSaveButtonPmo"));

        // start edit mode and enter '2' as value
        editButton().click();
        valueField().sendKeys("2");

        // reload page and check that browser confirmation dialog is shown
        reload();

        waitUntil(ExpectedConditions.alertIsPresent());

        // abort dialog
        getDriver().switchTo().alert().dismiss();

        // still on the same page and value is '2'
        assertThat(valueField().getValue()).isEqualTo("2");

        // reload page and confirm dialog
        reload();
        waitUntil(ExpectedConditions.alertIsPresent());
        getDriver().switchTo().alert().accept();

        // page was reloaded and value is empty
        waitForElementVisible(By.ById.id("EditSaveButtonPmo"));
        assertThat(valueField().getValue()).isEmpty();

        // enter edit mode and enter value '3'
        valueField().sendKeys("3");

        // click menu item and check that custom confirmation dialog is shown
        clickMenuItemById("playground");

        // abort dialog and check that value is still '3'
        var dialog = $(DialogElement.class).first();
        dialog.$(ButtonElement.class).id("cancelButton").click();
        assertThat(valueField().getValue()).isEqualTo("3");

        // go back and confirm dialog
        clickMenuItemById("playground");
        dialog = $(DialogElement.class).first();
        dialog.$(ButtonElement.class).id("okButton").click();

        // check that we are on the main page again
        assertThat(getDriver().getCurrentUrl()).endsWith("/playground");

    }

    private void reload() {
        getDriver().navigate().refresh();
    }

    private TextFieldElement valueField() {
        return $(TextFieldElement.class).id("value");
    }

    private ButtonElement editButton() {
        return $(ButtonElement.class).id("edit");
    }

    private ButtonElement saveButton() {
        return $(ButtonElement.class).id("save");
    }

}
