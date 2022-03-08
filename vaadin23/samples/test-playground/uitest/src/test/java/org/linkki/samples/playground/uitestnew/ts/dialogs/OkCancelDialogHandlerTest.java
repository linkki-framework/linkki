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

package org.linkki.samples.playground.uitestnew.ts.dialogs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.dialogs.OkCancelDialogHandlerPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;

class OkCancelDialogHandlerTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS011, TestScenarioView.TC001);
        $(ButtonElement.class).id(OkCancelDialogHandlerPmo.RESET_BUTTON_ID).click();
    }

    @ParameterizedTest
    @MethodSource("dialogButtons")
    void testOkHandler_ClickButton(String dialogButton) {
        DialogElement dialog = openDialog(dialogButton);

        dialog.$(ButtonElement.class).id(OkCancelDialog.OK_BUTTON_ID).click();

        assertThat(getOkCount(), is(1));
        assertThat(getCancelCount(), is(0));
    }

    @ParameterizedTest
    @MethodSource("dialogButtons")
    void testOkHandler_EnterKeyOnButton(String dialogButton) {
        DialogElement dialog = openDialog(dialogButton);

        ButtonElement okButton = dialog.$(ButtonElement.class).id(OkCancelDialog.OK_BUTTON_ID);
        okButton.focus();
        okButton.sendKeys(Keys.ENTER);

        assertThat(getOkCount(), is(1));
        assertThat(getCancelCount(), is(0));
    }

    @ParameterizedTest
    @MethodSource("dialogButtons")
    void testCancelHandler_ClickButton(String dialogButton) {
        DialogElement dialog = openDialog(dialogButton);

        dialog.$(ButtonElement.class).id(OkCancelDialog.CANCEL_BUTTON_ID).click();

        assertThat(getOkCount(), is(0));
        assertThat(getCancelCount(), is(1));
    }

    @ParameterizedTest
    @MethodSource("dialogButtons")
    void testCancelHandler_EnterKeyOnButton(String dialogButton) {
        DialogElement dialog = openDialog(dialogButton);

        ButtonElement cancelButton = dialog.$(ButtonElement.class).id(OkCancelDialog.CANCEL_BUTTON_ID);
        cancelButton.focus();
        cancelButton.sendKeys(Keys.ENTER);

        assertThat(getOkCount(), is(0));
        assertThat(getCancelCount(), is(1));
    }

    @ParameterizedTest
    @MethodSource("dialogButtons")
    void testCancelHandler_EscapeKey(String dialogButton) {
        openDialog(dialogButton);

        // selenium doesn't like it when elements disappear when sending keys
        // so we cannot send it to the dialog
        findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);

        assertThat(getOkCount(), is(0));
        assertThat(getCancelCount(), is(1));
    }

    @ParameterizedTest
    @MethodSource("dialogButtons")
    void testCancelHandler_ClickOutsideDialog(String dialogButton) {
        openDialog(dialogButton);

        findElement(By.tagName("body")).click();

        assertThat(getOkCount(), is(0));
        assertThat(getCancelCount(), is(1));
    }

    private static Stream<Arguments> dialogButtons() {
        return Stream.of(
                         Arguments.of(Named.of("OkCancelDialog",
                                               OkCancelDialogHandlerPmo.SHOW_DIALOG_BUTTON_ID)),
                         // LIN-2804 only occured when a DialogBindingManager was used
                         Arguments.of(Named.of("OkCancelDialog with DialogBindingManager",
                                               OkCancelDialogHandlerPmo.SHOW_DIALOG_WITH_BINDING_MANAGER_BUTTON_ID)));
    }

    private DialogElement openDialog(String dialogButton) {
        $(ButtonElement.class).id(dialogButton).click();
        return $(DialogElement.class).waitForFirst();
    }

    private int getOkCount() {
        String okCounter = findElement(By.id("okCounter")).getText();
        return Integer.valueOf(okCounter);
    }

    private int getCancelCount() {
        String cancelCounter = findElement(By.id("cancelCounter")).getText();
        return Integer.valueOf(cancelCounter);
    }


}
