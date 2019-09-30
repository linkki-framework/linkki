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


import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.dynamicannotations.DynamicAnnotationsLayout;
import org.linkki.samples.playground.dynamicannotations.DynamicTooltipPmo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.testbench.elements.TextFieldElement;

public class BindTooltipTest extends AbstractUiTest {

    @BeforeEach
    public void setup() {
        clickButton(DynamicAnnotationsLayout.ID);
    }

    @Test
    public void testTooltip() {
        TextFieldElement field = $(TextFieldElement.class).id(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT);
        field.setValue("tooltip");
        field.showTooltip();

        WebElement tooltip = findElement(By.className("v-tooltip"));
        assertThat(tooltip.getText(), is("tooltip"));
    }

    @Test
    public void testTooltip_LineBreak() {
        TextFieldElement field = $(TextFieldElement.class).id(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT);
        field.setValue("multiple<br>lines");
        field.showTooltip();

        WebElement tooltip = findElement(By.className("v-tooltip"));
        assertThat(tooltip.getText(), is("multiple\nlines"));
    }

    @Test
    public void testTooltip_Span() {
        TextFieldElement field = $(TextFieldElement.class).id(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT);
        field.setValue("<span>span tag</span>");
        field.showTooltip();

        WebElement tooltip = findElement(By.className("v-tooltip"));
        assertThat(tooltip.getText(), is("span tag"));
    }

    @Test
    public void testTooltip_Script() {
        TextFieldElement field = $(TextFieldElement.class).id(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT);
        field.setValue("<script>alert(\"hello\")</script>");
        field.showTooltip();

        WebElement tooltip = findElement(By.className("v-tooltip"));
        assertThat(tooltip.getText(), is("<script>alert(\"hello\")</script>"));
    }

}
