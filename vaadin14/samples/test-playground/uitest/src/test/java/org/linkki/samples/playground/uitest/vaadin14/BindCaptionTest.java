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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.dynamicannotations.DynamicCaptionWithCloseButtonPmo;
import org.linkki.samples.playground.dynamicannotations.DynamicCaptionWithEditButtonPmo;
import org.linkki.samples.playground.dynamicannotations.DynamicCaptionWithSectionHeaderButtonPmo;
import org.linkki.samples.playground.dynamicannotations.DynamicCaptionWithoutButtonPmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationUI;
import org.linkki.samples.playground.uitest.AbstractUiTest;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.html.testbench.H4Element;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;


public class BindCaptionTest extends AbstractUiTest {

    @BeforeEach
    public void setup() {
        openTab(PlaygroundApplicationUI.DYNAMIC_ASPECT_TAB_ID);
    }

    @Test
    public void testSectionCaption() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithoutButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);
        H4Element caption = getHeader(section).$(H4Element.class).first();

        captionField.setValue("section caption");

        assertThat(caption.getText(), is("section caption"));
    }

    @Test
    public void testSectionCaption_WithEditButton() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithEditButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithEditButtonPmo.PROPERTY_SECTION_CAPTION);
        H4Element caption = getHeader(section).$(H4Element.class).first();

        captionField.setValue("section caption");

        assertThat(caption.getText(), is("section caption"));
    }

    @Test
    public void testSectionCaption_WithSectionHeaderButton() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithSectionHeaderButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithSectionHeaderButtonPmo.PROPERTY_SECTION_CAPTION);
        H4Element caption = getHeader(section).$(H4Element.class).first();

        captionField.setValue("section caption");

        assertThat(caption.getText(), is("section caption"));
    }

    @Test
    public void testSectionCaption_WithCloseButton() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithCloseButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithCloseButtonPmo.PROPERTY_SECTION_CAPTION);
        H4Element caption = getHeader(section).$(H4Element.class).first();

        captionField.setValue("section caption");

        assertThat(caption.getText(), is("section caption"));
    }

    @Test
    public void testSectionCaption_EmptyCaptionHidesElement() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithoutButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);
        H4Element caption = getHeader(section).$(H4Element.class).first();

        captionField.setValue("");

        assertThat(caption.isDisplayed(), is(false));
    }

    @Test
    public void testSectionCaption_ButtonStaysVisibleWithEmptyCaption() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithSectionHeaderButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithSectionHeaderButtonPmo.PROPERTY_SECTION_CAPTION);
        H4Element caption = getHeader(section).$(H4Element.class).first();
        ButtonElement button = getHeader(section).$(ButtonElement.class).id("callAnAmbulance");

        captionField.setValue("");

        assertThat(caption.isDisplayed(), is(false));
        assertThat(button.isDisplayed(), is(true));
    }

    @Test
    public void testButtonCaption() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithoutButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);
        ButtonElement button = section.$(ButtonElement.class).id(DynamicCaptionWithoutButtonPmo.PROPERTY_BUTTON);

        captionField.setValue("button caption");

        assertThat(button.getText(), is("button caption"));
    }

    @Test
    public void testCheckboxCaption() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithoutButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);
        CheckboxElement checkbox = section.$(CheckboxElement.class)
                .id(DynamicCaptionWithoutButtonPmo.PROPERTY_CHECKBOX);


        captionField.setValue("checkbox caption");

        assertThat(checkbox.getText(), is("checkbox caption"));
    }

    private VerticalLayoutElement getSection(Class<?> cls) {
        return $(VerticalLayoutElement.class).id(cls.getSimpleName());
    }

    private HorizontalLayoutElement getHeader(VerticalLayoutElement section) {
        return section.$(HorizontalLayoutElement.class).first();
    }

}