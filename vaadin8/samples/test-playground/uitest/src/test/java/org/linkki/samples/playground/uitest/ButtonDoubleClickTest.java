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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.BugCollectionLayout;
import org.linkki.samples.playground.uitest.extensions.DriverExtension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;
import com.vaadin.testbench.elements.WindowElement;

@DriverExtension.Configuration(restartAfterEveryTest = true)
public class ButtonDoubleClickTest extends AbstractUiTest {

    @BeforeEach
    public void setup() {
        clickButton(BugCollectionLayout.ID);
    }

    @Test
    public void testDoubleClick_UiButton() {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id("DoubleClickPmo");
        WebElement button = section.$(ButtonElement.class).id("dialogButton");

        js.executeScript("let button = arguments[0]; button.click(); button.click();", button);

        assertThat($(WindowElement.class).caption("LIN-1738 Dialog").all(), hasSize(1));
    }

    @Test
    public void testDoubleClick_EditButton() {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id("DoubleClickPmo");
        WebElement button = section.$(ButtonElement.class).id("buttonPmo");

        js.executeScript("let button = arguments[0]; button.click(); button.click();", button);

        assertThat($(WindowElement.class).caption("LIN-1738 Dialog").all(), hasSize(1));
    }
}
