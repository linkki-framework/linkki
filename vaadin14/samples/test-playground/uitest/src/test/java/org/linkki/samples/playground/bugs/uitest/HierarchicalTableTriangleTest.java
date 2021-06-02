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

package org.linkki.samples.playground.bugs.uitest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.lin1917.TriangleTablePmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationUI;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.grid.testbench.TreeGridElement;

/**
 * LIN-1890
 */
public class HierarchicalTableTriangleTest extends AbstractUiTest {

    @Test
    public void testTriangleIsShownAfterChildIsAdded() {
        openTab(PlaygroundApplicationUI.BUGS_TAB_ID);

        TreeGridElement gridElement = $(TreeGridElement.class).id("TriangleTablePmo_table");
        WebElement toggleElement = getToggleElement(gridElement);

        assertThat(toggleElement.getCssValue("visibility"), is("hidden"));

        gridElement.getCell(0, 0).click();
        clickSectionHeaderButton(TriangleTablePmo.class, 0);

        assertThat(toggleElement.getCssValue("visibility"), is(not("hidden")));
    }

    private WebElement getToggleElement(TreeGridElement grid) {
        final JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
        final WebElement shadowRoot = (WebElement)jsExecutor.executeScript("return arguments[0].shadowRoot",
                                                                           grid.getExpandToggleElement(0, 0));
        return shadowRoot.findElement(By.cssSelector("span[part=\"toggle\"]"));
    }
}
