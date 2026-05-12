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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.OkCancelDialogElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class BrowserConfirmationTest extends PlaygroundUiTest {

    private String originalTab;

    @BeforeEach
    void goToAndClearConfirmView() {
        originalTab = getDriver().getWindowHandle();
        getDriver().switchTo().newWindow(WindowType.TAB);
        goToTestCase();
        waitForEditButton().click();
        editValue(Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE);
        waitForSaveButton().click();
        assertThat(textField().getValue()).isEmpty();
    }

    @AfterEach
    void closeTab() {
        getDriver().close();
        getDriver().switchTo().window(originalTab);
    }

    @Test
    void testNoBrowserConfirmationOnReload() {
        reload();
        var targetLocator = getDriver().switchTo();
        assertThrows(NoAlertPresentException.class, targetLocator::alert);
        waitForElementVisible(By.ById.id("EditSaveButtonPmo"));
    }

    @Test
    void testBrowserConfirmationOnReload_Dismiss() {
        waitForEditButton().click();
        editValue("2");

        reload();
        waitUntil(ExpectedConditions.alertIsPresent());
        getDriver().switchTo().alert().dismiss();

        assertThat(textField().getValue()).isEqualTo("2");
    }

    @Test
    void testBrowserConfirmationOnReload_Accept() {
        waitForEditButton().click();
        editValue("2");

        reload();
        waitUntil(ExpectedConditions.alertIsPresent());
        getDriver().switchTo().alert().accept();

        assertThat(textField().getValue()).isEmpty();
    }

    @Test
    void testNavigationConfirmation_Cancel() {
        waitForEditButton().click();
        editValue("3");

        clickMenuItemById("playground");

        var dialog = $(OkCancelDialogElement.class).first();
        dialog.clickOnCancel();
        assertThat(textField().getValue()).isEqualTo("3");
    }

    @Test
    void testNavigationConfirmation_Confirm() {
        waitForEditButton().click();
        editValue("3");

        clickMenuItemById("playground");

        var dialog = $(OkCancelDialogElement.class).first();
        dialog.clickOnOk();

        assertThat(getDriver().getCurrentUrl()).endsWith("/playground");

        goToTestCase();
        assertThat(textField().getValue()).isEmpty();
    }

    @Test
    void testNoConfirmationAfterSave() {
        waitForEditButton().click();
        editValue("2");
        waitForSaveButton().click();
        waitForEditButton();

        reload();
        waitUntilViewIsLoaded();

        clickMenuItemById("playground");
        assertThat(getDriver().getCurrentUrl()).endsWith("/playground");
    }

    private void reload() {
        getDriver().navigate().refresh();
    }

    private void goToTestCase() {
        goToTestCaseByUrl(TestScenarioView.TS019, TestScenarioView.TC001);
        $(ButtonElement.class).id("goToView").click();
    }

    private void editValue(String value) {
        waitUntilViewIsLoaded();
        waitUntil(ExpectedConditions
                .elementToBeClickable($(ButtonElement.class).id("save")));
        $(TextFieldElement.class).id("value").sendKeys(value);
    }

    private TextFieldElement textField() {
        waitUntilViewIsLoaded();
        return $(TextFieldElement.class).id("value");
    }

    private ButtonElement waitForEditButton() {
        return (ButtonElement)waitUntil(ExpectedConditions
                .elementToBeClickable($(ButtonElement.class).id("edit")));
    }

    private ButtonElement waitForSaveButton() {
        return (ButtonElement)waitUntil(ExpectedConditions
                .elementToBeClickable($(ButtonElement.class).id("save")));
    }

    private void waitUntilViewIsLoaded() {
        waitForElementVisible(By.ById.id("EditSaveButtonPmo"));
    }
}
