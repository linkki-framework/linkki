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

package org.linkki.samples.playground.uitestnew.ts.components;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.uitest.DriverProperties;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.textfield.testbench.TextAreaElement;

public class TC010UIButtonTest extends PlaygroundUiTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        getDriver().get(DriverProperties.getTestUrl(""));
        goToTestCase("TS005", "TC010");
    }

    @Test
    void testButton_OnClick() {
        DivElement counter = getCounter();
        ButtonElement increaseCounterButton = $(ButtonElement.class).id("increaseCounter");

        assertThat(counter.getText()).isEqualTo("Counter: 0");

        IntStream.range(0, 10).forEach(i -> increaseCounterButton.click());

        assertThat(counter.getText()).isEqualTo("Counter: 10");
    }

    @Test
    void testButton_OnDoubleClick() {
        DivElement counter = getCounter();
        ButtonElement increaseCounterButton = $(ButtonElement.class).id("increaseCounter");

        assertThat(counter.getText()).isEqualTo("Counter: 0");

        increaseCounterButton.doubleClick();

        assertThat(counter.getText()).isEqualTo("Counter: 1");
    }

    @Test
    void testButton_ShortcutKey() {
        DivElement counter = getCounter();
        assertThat(counter.getText()).isEqualTo("Counter: 0");

        sendKeys(null, Keys.ENTER);

        assertThat(counter.getText()).isEqualTo("Counter: 1");
    }

    @Test
    void testButton_ShortcutKey_FocusOnButton() {
        DivElement counter = getCounter();
        assertThat(counter.getText()).isEqualTo("Counter: 0");
        ButtonElement increaseCounterButton = $(ButtonElement.class).id("increaseCounter");
        increaseCounterButton.focus();

        sendKeys(null, Keys.ENTER);

        assertThat(counter.getText())
                .as("Browser default triggering the focused increase counter button should not be executed")
                .isEqualTo("Counter: 1");
    }

    @Test
    void testButtonShortcut_WithTextAreaFocus_WithEnterClickShorcut() {
        TextAreaElement textArea = getTextArea();
        DivElement counter = getCounter();
        assertThat(counter.getText()).isEqualTo("Counter: 0");
        textArea.focus();

        sendKeys(null, "Enter!");
        sendKeys(null, Keys.ENTER);

        assertThat(textArea.getValue())
                .describedAs("As enter is used as button shortcut, the browser default which would create line breaks in text area sould be prevented.")
                .isEqualTo("Enter!");
        assertThat(counter.getText()).isEqualTo("Counter: 1");
        assertThat($(DivElement.class).id("contentAsText").getText())
                .as("Value of the text area should have been sent to server")
                .isEqualTo("Enter!");
    }

    @Test
    void testButtonShortcut_WithTextAreaFocus_WithCrtlEnterClickShortCut() {
        TextAreaElement textArea = getTextArea();
        DivElement counter = getCounter();
        assertThat(counter.getText()).isEqualTo("Counter: 0");
        textArea.focus();

        sendKeys(null, "Crtl+Enter!");
        sendKeys(Keys.CONTROL, Keys.ENTER);

        assertThat(textArea.getValue()).isEqualTo("Crtl+Enter!");
        assertThat(counter.getText()).isEqualTo("Counter: 1");
        assertThat($(DivElement.class).id("contentAsText").getText())
                .as("Value of the text area should have been sent to server")
                .isEqualTo("Crtl+Enter!");
    }

    @Test
    void testButtonShortcut_WithTextAreaFocus_OnShiftEnter() {
        TextAreaElement textArea = getTextArea();
        DivElement counter = getCounter();

        textArea.focus();
        sendKeys(null, "Shift+Enter!");
        sendKeys(Keys.SHIFT, Keys.ENTER);

        assertThat(textArea.getValue()).isEqualTo("Shift+Enter!\n");
        assertThat(counter.getText()).isEqualTo("Counter: 0");
    }

    private TextAreaElement getTextArea() {
        return $(TextAreaElement.class).id("content");
    }

    private DivElement getCounter() {
        return $(DivElement.class).id("counter");
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
