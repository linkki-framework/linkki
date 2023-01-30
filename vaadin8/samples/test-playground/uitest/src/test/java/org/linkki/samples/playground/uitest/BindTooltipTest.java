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
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.dynamicannotations.DynamicAnnotationsLayout;
import org.linkki.samples.playground.dynamicannotations.DynamicTooltipPmo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.testbench.elements.GridLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

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
    public void testTooltip_HtmlContent_LineBreak() {
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
        // <script> will be removed by the HTML content sanitization 
        assertThat(tooltip.getText(), is(emptyString()));
    }

    @Test
    public void testTooltip_Label() {
        TextFieldElement field = $(TextFieldElement.class).id(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT);
        field.setValue("tooltip");

        getLabel(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT).showTooltip();

        WebElement labelTooltip = findElement(By.className("v-tooltip"));
        assertThat(labelTooltip.getText(), is("tooltip"));
    }

    @Test
    public void testTooltip_Label_HtmlContent_LineBreak() {
        TextFieldElement field = $(TextFieldElement.class).id(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT);
        field.setValue("multiple<br>lines");
        field.showTooltip();

        getLabel(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT).showTooltip();

        WebElement labelTooltip = findElement(By.className("v-tooltip"));
        assertThat(labelTooltip.getText(), is("multiple\nlines"));
    }

    @Test
    public void testTooltip_Label_Span() {
        TextFieldElement field = $(TextFieldElement.class).id(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT);
        field.setValue("<span>span tag</span>");

        getLabel(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT).showTooltip();

        WebElement labelTooltip = findElement(By.className("v-tooltip"));
        assertThat(labelTooltip.getText(), is("span tag"));
    }

    @Test
    public void testTooltip_Label_Script() {
        TextFieldElement field = $(TextFieldElement.class).id(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT);
        field.setValue("<script>alert(\"hello\")</script>");

        getLabel(DynamicTooltipPmo.PROPERTY_TOOLTIP_TEXT).showTooltip();

        WebElement labelTooltip = findElement(By.className("v-tooltip"));
        // <script> will be removed by the HTML content sanitization
        assertThat(labelTooltip.getText(), is(emptyString()));
    }

    private LabelElement getLabel(String id) {
        GridLayoutElement gridLayoutElement = $(VerticalLayoutElement.class).id(DynamicTooltipPmo.class.getSimpleName())
                .$(GridLayoutElement.class).first();
        List<WebElement> gridLayoutSlots = gridLayoutElement.getWrappedElement()
                .findElements(By.className("v-gridlayout-slot"));
        for (int i = 0; i < gridLayoutSlots.size(); i++) {
            WebElement gridLayoutSlot = gridLayoutSlots.get(i);
            if (!gridLayoutSlot.findElements(By.id(id)).isEmpty()) {
                return $(LabelElement.class).context(gridLayoutElement).get(i / 2);
            }
        }
        throw new IllegalArgumentException("No label found for id " + id);
    }
}
