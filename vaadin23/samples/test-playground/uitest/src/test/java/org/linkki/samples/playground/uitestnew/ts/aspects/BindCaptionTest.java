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

package org.linkki.samples.playground.uitestnew.ts.aspects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.LinkkiSectionElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.aspects.BindCaptionWithCloseButtonPmo;
import org.linkki.samples.playground.ts.aspects.BindCaptionWithEditButtonPmo;
import org.linkki.samples.playground.ts.aspects.BindCaptionWithSectionHeaderButtonPmo;
import org.linkki.samples.playground.ts.aspects.BindCaptionWithoutButtonPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.html.testbench.H4Element;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;

public class BindCaptionTest extends PlaygroundUiTest {

    @Test
    public void testSectionCaption() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC002);
        LinkkiSectionElement section = getSection(BindCaptionWithoutButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);

        captionField.setValue("section caption");

        assertThat(section.getCaption(), is("section caption"));
    }

    @Test
    public void testSectionCaption_WithEditButton() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC003);
        LinkkiSectionElement section = getSection(BindCaptionWithEditButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithEditButtonPmo.PROPERTY_SECTION_CAPTION);

        captionField.setValue("section caption");

        assertThat(section.getCaption(), is("section caption"));
    }

    @Test
    public void testSectionCaption_WithSectionHeaderButton() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC004);
        LinkkiSectionElement section = getSection(BindCaptionWithSectionHeaderButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithSectionHeaderButtonPmo.PROPERTY_SECTION_CAPTION);

        captionField.setValue("section caption");

        assertThat(section.getCaption(), is("section caption"));
    }

    @Test
    public void testSectionCaption_WithCloseButton() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC005);
        LinkkiSectionElement section = getSection(BindCaptionWithCloseButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithCloseButtonPmo.PROPERTY_SECTION_CAPTION);

        captionField.setValue("section caption");

        assertThat(section.getCaption(), is("section caption"));
    }

    @Test
    public void testSectionCaption_EmptyCaptionHidesElement() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC002);
        LinkkiSectionElement section = getSection(BindCaptionWithoutButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);

        captionField.setValue("");

        assertThat(section.$(H4Element.class).first().isDisplayed(), is(false));
    }

    @Test
    public void testSectionCaption_ButtonStaysVisibleWithEmptyCaption() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC004);
        LinkkiSectionElement section = getSection(BindCaptionWithSectionHeaderButtonPmo.class);
        TextFieldElement captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithSectionHeaderButtonPmo.PROPERTY_SECTION_CAPTION);
        TestBenchElement button = section.getHeaderComponents().id("callAnAmbulance");

        captionField.setValue("");

        assertThat(section.$(H4Element.class).first().isDisplayed(), is(false));
        assertThat(button.isDisplayed(), is(true));
    }

    @Test
    public void testButtonCaption() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC002);
        LinkkiSectionElement section = getSection(BindCaptionWithoutButtonPmo.class);
        TextFieldElement captionField = section.getContent().$(TextFieldElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);
        ButtonElement button = section.getContent().$(ButtonElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_BUTTON);

        captionField.setValue("button caption");

        assertThat(button.getText(), is("button caption"));
    }

    @Test
    public void testCheckboxCaption() {
        LinkkiSectionElement section = getSection(BindCaptionWithoutButtonPmo.class);
        TextFieldElement captionField = section.getContent().$(TextFieldElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);
        CheckboxElement checkbox = section.getContent().$(CheckboxElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_CHECKBOX);


        captionField.setValue("checkbox caption");

        assertThat(checkbox.getText(), is("checkbox caption"));
    }

}