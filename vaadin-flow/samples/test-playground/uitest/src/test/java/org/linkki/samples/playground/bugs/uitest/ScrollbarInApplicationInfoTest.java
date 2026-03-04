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

package org.linkki.samples.playground.bugs.uitest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.application.custom.CustomView;
import org.linkki.samples.playground.uitest.AbstractLinkkiUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import com.vaadin.flow.component.menubar.testbench.MenuBarItemElement;

class ScrollbarInApplicationInfoTest extends AbstractLinkkiUiTest {

    @Test
    void testScrollbarInApplicationInfo() {
        goToView(CustomView.NAME);
        var js = (JavascriptExecutor)getDriver();

        $(MenuBarItemElement.class).id("appmenu-help").click();
        clickContextMenuItem("Info");
        var dialog = findDialog("Über linkki Samples :: Application Framework - Angepasst");
        var formItems = dialog.findElements(By.cssSelector("vaadin-form-item"));

        assertThat(formItems).allSatisfy(item -> {
            Long scrollHeight = (Long)js.executeScript("return arguments[0].scrollHeight;", item);
            Long clientHeight = (Long)js.executeScript("return arguments[0].clientHeight;", item);
            assertThat(scrollHeight).isEqualTo(clientHeight);
        });
    }
}
