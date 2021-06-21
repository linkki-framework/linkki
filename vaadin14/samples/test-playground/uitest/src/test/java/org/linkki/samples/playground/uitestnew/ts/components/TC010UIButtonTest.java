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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.uitestnew.BaseUITest;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;

public class TC010UIButtonTest extends BaseUITest {

    @BeforeEach
    void setUp() {
        goToTestCase("TS005", "TC010");
    }

    @Order(1)
    @Test
    void testButton_IncreaseCounter() {
        DivElement counter = $(DivElement.class).id("counter");
        ButtonElement increaseCounterButton = $(ButtonElement.class).id("increaseCounter");

        assertThat(counter.getText()).isEqualTo("Counter: 0");

        IntStream.range(0, 10).forEach(i -> increaseCounterButton.click());

        assertThat(counter.getText()).isEqualTo("Counter: 10");
    }

    @Order(2)
    // TODO LIN-2343
    @Disabled("Button should not receive a click on enter")
    @Test
    void testButton_ShortcutKey() {
        DivElement counter = $(DivElement.class).id("counter");
        ButtonElement increaseCounterButton = $(ButtonElement.class).id("increaseCounter");

        String counterValueBefore = counter.getText();

        // the button shortcut should trigger even when another element is in focus
        increaseCounterButton.sendKeys(Keys.ENTER);

        NotificationElement notification = $(NotificationElement.class).first();
        assertThat(notification.getText()).isEqualTo("Button for Notification pressed!");
        // the counter should not be changed
        assertThat(counter.getText()).isEqualTo(counterValueBefore);
    }

    @Order(3)
    @Test
    void testButton_ResetCounter() {
        DivElement counter = $(DivElement.class).id("counter");
        ButtonElement resetCounterButton = $(ButtonElement.class).id("resetCounter");

        resetCounterButton.click();

        assertThat(counter.getText()).isEqualTo("Counter: 0");
    }

}
