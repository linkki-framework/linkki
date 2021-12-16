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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;

public class TC007UIComboBoxTest extends PlaygroundUiTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        goToTestCase(PlaygroundApplicationView.TS005, PlaygroundApplicationView.TC007);
    }

    @Test
    void testEmptyComboBoxSelection() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");

        List<String> selectionList = Optional.ofNullable(comboBoxElement.getOptions()).orElse(Collections.emptyList());
        assertNotNull(selectionList);
        assertTrue(selectionList.size() > 0);

        String selectedElement = selectionList.get(0);
        comboBoxElement.selectByText(selectedElement);

        clearComboBoxSelection(comboBoxElement);
        assertThat(comboBoxElement.getSelectedText(), is(""));
    }

    private void clearComboBoxSelection(ComboBoxElement comboBoxElement) {
        final WebElement shadowRoot = getShadowRoot(comboBoxElement);
        final WebElement clearButton = shadowRoot
                .findElement(By.id("clearButton"));
        clearButton.click();
    }

    private WebElement getShadowRoot(WebElement shadowHost) {
        return (WebElement)((JavascriptExecutor)driver).executeScript("return arguments[0].shadowRoot",
                                                                      shadowHost);
    }
}
