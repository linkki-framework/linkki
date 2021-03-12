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

package org.linkki.samples.playground.uitest.vaadin14;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.openqa.selenium.By;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.menubar.testbench.MenuBarElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextAreaElement;

public class HeadlineTest extends AbstractUiTest {

    @BeforeEach
    public void setup() {
        MenuBarElement menu = $(MenuBarElement.class).id(LinkkiApplicationTheme.APPLICATION_MENU);
        menu.getButtons().stream().filter(b -> "Sample Layout".equals(b.getText())).findFirst().get().click();
    }

    @Test
    public void testHeadline_dynamicTitle() {
        findElement(By.id("ReportList")).click();

        String reportListHeadline = $(VerticalLayoutElement.class).id("ReportListPage")
                .findElement(By.className("linkki-headline"))
                .findElement(By.tagName("h2")).getText();

        assertThat(reportListHeadline, containsString("Report List"));
        assertThat(reportListHeadline, containsString(String.valueOf($(GridElement.class).first().getRowCount())));

        findElement(By.id("CreateReport")).click();

        addReport();

        findElement(By.id("ReportList")).click();

        reportListHeadline = $(VerticalLayoutElement.class).id("ReportListPage")
                .findElement(By.className("linkki-headline"))
                .findElement(By.tagName("h2")).getText();

        assertThat(reportListHeadline, containsString("Report List"));
        assertThat(reportListHeadline, containsString(String.valueOf($(GridElement.class).first().getRowCount())));
    }

    private void addReport() {
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id("ReportSectionPmo");
        ComboBoxElement comboBoxElement = section.$(ComboBoxElement.class).id("type");
        comboBoxElement.selectByText("Question");

        TextAreaElement textAreaElement = section.$(TextAreaElement.class).id("description");
        textAreaElement.sendKeys("Please check report!");
        // tab out to lose focus
        textAreaElement.sendKeys("\t");

        ButtonElement buttonElement = section.$(ButtonElement.class).id("send");
        buttonElement.click();
    }
}
