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
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.html.testbench.H4Element;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;


public class BindCaptionTest extends PlaygroundUiTest {

    @BeforeEach
    public void setup() {
        super.setUp();
    }

    @Test
    public void testSectionCaption() {
        TestCaseComponentElement section = goToTestCase(PlaygroundApplicationView.TS013,
                                                        PlaygroundApplicationView.TC002);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);
        H4Element caption = getHeader(section).$(H4Element.class).first();

        captionField.setValue("section caption");

        assertThat(caption.getText(), is("section caption"));
    }

    @Test
    public void testSectionCaption_WithEditButton() {
        TestCaseComponentElement section = goToTestCase(PlaygroundApplicationView.TS013,
                                                        PlaygroundApplicationView.TC003);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithEditButtonPmo.PROPERTY_SECTION_CAPTION);
        H4Element caption = getHeader(section).$(H4Element.class).first();

        captionField.setValue("section caption");

        assertThat(caption.getText(), is("section caption"));
    }

    @Test
    public void testSectionCaption_WithSectionHeaderButton() {
        TestCaseComponentElement section = goToTestCase(PlaygroundApplicationView.TS013,
                                                        PlaygroundApplicationView.TC004);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithSectionHeaderButtonPmo.PROPERTY_SECTION_CAPTION);
        H4Element caption = getHeader(section).$(H4Element.class).first();

        captionField.setValue("section caption");

        assertThat(caption.getText(), is("section caption"));
    }

    @Test
    public void testSectionCaption_WithCloseButton() {
        TestCaseComponentElement section = goToTestCase(PlaygroundApplicationView.TS013,
                                                        PlaygroundApplicationView.TC005);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithCloseButtonPmo.PROPERTY_SECTION_CAPTION);
        H4Element caption = getHeader(section).$(H4Element.class).first();

        captionField.setValue("section caption");

        assertThat(caption.getText(), is("section caption"));
    }

    @Test
    public void testSectionCaption_EmptyCaptionHidesElement() {
        TestCaseComponentElement section = goToTestCase(PlaygroundApplicationView.TS013,
                                                        PlaygroundApplicationView.TC002);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(DynamicCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);
        H4Element caption = getHeader(section).$(H4Element.class).first();

        captionField.setValue("");

        assertThat(caption.isDisplayed(), is(false));
    }

    @Test
    public void testSectionCaption_ButtonStaysVisibleWithEmptyCaption() {
        TestCaseComponentElement section = goToTestCase(PlaygroundApplicationView.TS013,
                                                        PlaygroundApplicationView.TC004);
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
        TestCaseComponentElement section = goToTestCase(PlaygroundApplicationView.TS013,
                                                        PlaygroundApplicationView.TC002);
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

    private HorizontalLayoutElement getHeader(TestCaseComponentElement section) {
        return section.$(HorizontalLayoutElement.class).first();
    }

}