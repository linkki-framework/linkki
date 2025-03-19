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

package org.linkki.samples.playground.uitestnew.ts.formelements;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.formelements.ButtonPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiTextElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.radiobutton.testbench.RadioButtonGroupElement;
import com.vaadin.flow.component.textfield.testbench.TextAreaElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class TC010UIButtonTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCaseByUrl(TestScenarioView.TS005, TestScenarioView.TC010);
    }

    @Test
    void testOnClick() {
        LinkkiTextElement counter = getCounter();
        ButtonElement increaseCounterButton = $(ButtonElement.class).id("increaseCounter");

        assertThat(counter.getText()).isEqualTo("Counter: 0");

        IntStream.range(0, 10).forEach(i -> increaseCounterButton.click());

        assertThat(counter.getText()).isEqualTo("Counter: 10");
    }

    @Test
    void testOnDoubleClick() {
        LinkkiTextElement counter = getCounter();
        ButtonElement increaseCounterButton = $(ButtonElement.class).id("increaseCounter");

        assertThat(counter.getText()).isEqualTo("Counter: 0");

        increaseCounterButton.doubleClick();

        assertThat(counter.getText())
                .as("Double click on buttons should only invoke the method once")
                .isEqualTo("Counter: 1");
    }

    @Test
    void testShortcutKey() {
        LinkkiTextElement counter = getCounter();
        assertThat(counter.getText()).isEqualTo("Counter: 0");

        sendKeys(null, Keys.ENTER);

        assertThat(counter.getText()).isEqualTo("Counter: 1");
    }

    @Disabled("LIN-2620")
    @Test
    void testShortcutKey_FocusOnShortcutButton() {
        LinkkiTextElement counter = getCounter();
        assertThat(counter.getText()).isEqualTo("Counter: 0");
        ButtonElement increaseCounterButton = $(ButtonElement.class).id("buttonWithEnter");
        increaseCounterButton.focus();

        sendKeys(null, Keys.ENTER);

        assertThat(counter.getText())
                .as("Button should not be triggered twice if the shortcut is triggered while focusing the button")
                .isEqualTo("Counter: 1");
    }

    @Test
    void testShortcutKey_InSingleLineInput_WithEnter() {
        TextFieldElement textField = $(TextFieldElement.class).id("contentAsTextField");
        LinkkiTextElement counter = getCounter();
        assertThat(counter.getText()).isEqualTo("Counter: 0");
        assertThat($(LinkkiTextElement.class).id("contentAsText").getText()).isEmpty();

        textField.focus();
        sendKeys(null, "Enter!");
        sendKeys(null, Keys.ENTER);

        assertThat(counter.getText())
                .describedAs("Button shortcut should be triggered")
                .isEqualTo("Counter: 1");
        assertThat($(LinkkiTextElement.class).id("contentAsText").getText())
                .as("New value of the input should have been sent to server")
                .isEqualTo("Enter!");
    }

    @Test
    void testShortcutKey_InSingleLineInput_WithEnterIfNotEmpty() {
        TextFieldElement textField = $(TextFieldElement.class).id("contentAsTextField");
        LinkkiTextElement counter = getCounter();
        RadioButtonGroupElement radioButtonGroup = $(RadioButtonGroupElement.class).id("testBehaviorWithTextArea");
        radioButtonGroup.selectByText(ButtonPmo.TestShortcutBehavior.BUTTON_DISABLED_IF_EMPTY.toString());
        assertThat(counter.getText()).isEqualTo("Counter: 0");
        assertThat($(LinkkiTextElement.class).id("contentAsText").getText()).isEmpty();
        assertThat($(ButtonElement.class).id("buttonWithEnterIfNotEmpty").isEnabled()).isFalse();

        textField.focus();
        sendKeys(null, "Enter!");
        sendKeys(null, Keys.ENTER);

        assertThat(counter.getText())
                .describedAs("Button shortcut should be triggered")
                .isEqualTo("Counter: 1");
        assertThat($(LinkkiTextElement.class).id("contentAsText").getText())
                .as("New value of the input should have been sent to server")
                .isEqualTo("Enter!");
    }

    @Test
    void testShortcutKey_InMultiLineInput_WithEnter() {
        TextAreaElement textArea = getTextArea();
        LinkkiTextElement counter = getCounter();
        assertThat(counter.getText()).isEqualTo("Counter: 0");

        textArea.focus();
        sendKeys(null, "Enter!");
        sendKeys(null, Keys.ENTER);
        sendKeys(null, "1");

        assertThat(textArea.getValue())
                .describedAs("Using enter in text area should result in a new line")
                .isEqualTo("Enter!\n1");
        assertThat(counter.getText())
                .describedAs("Button shortcut should not be triggered if the current focus is a multi line input")
                .isEqualTo("Counter: 0");

        sendKeys(null, Keys.TAB);
        sendKeys(null, Keys.ENTER);

        assertThat($(LinkkiTextElement.class).id("contentAsText").getText())
                .as("Value of the text area should have been sent to server")
                .isEqualTo("Enter!\n1");
        // Two invokes due to LIN-2620
        assertThat(counter.getText()).isEqualTo("Counter: 2");
    }

    @Test
    void testShortcut_InMultiLineInput_WithEnterOnShiftEnter() {
        TextAreaElement textArea = getTextArea();
        LinkkiTextElement counter = getCounter();

        textArea.focus();
        sendKeys(null, "Shift+Enter!");
        sendKeys(Keys.SHIFT, Keys.ENTER);

        assertThat(textArea.getValue()).isEqualTo("Shift+Enter!\n");
        assertThat(counter.getText()).isEqualTo("Counter: 0");
    }

    private TextAreaElement getTextArea() {
        return $(TextAreaElement.class).id("content");
    }

    private LinkkiTextElement getCounter() {
        return $(LinkkiTextElement.class).id("counter");
    }

    private void sendKeys(Keys modifier, CharSequence... keys) {
        Actions actionProvider = new Actions(getDriver());
        if (modifier != null) {
            actionProvider.keyDown(modifier);
        }
        actionProvider.sendKeys(keys);
        if (modifier != null) {
            actionProvider.keyUp(modifier);
        }
        actionProvider.build().perform();
    }

}
