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

package org.linkki.samples.playground.uitestnew.ts.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
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

class BindCaptionTest extends PlaygroundUiTest {

    @Test
    void testSectionCaption() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC002);
        var section = getSection(BindCaptionWithoutButtonPmo.class);
        var captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);

        captionField.setValue("section caption");

        assertThat(section.getCaption()).isEqualTo("section caption");
    }

    @Test
    void testSectionCaption_WithEditButton() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC003);
        var section = getSection(BindCaptionWithEditButtonPmo.class);
        var captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithEditButtonPmo.PROPERTY_SECTION_CAPTION);

        captionField.setValue("section caption");

        assertThat(section.getCaption()).isEqualTo("section caption");
    }

    @Test
    void testSectionCaption_WithSectionHeaderButton() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC004);
        var section = getSection(BindCaptionWithSectionHeaderButtonPmo.class);
        var captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithSectionHeaderButtonPmo.PROPERTY_SECTION_CAPTION);

        captionField.setValue("section caption");

        assertThat(section.getCaption()).isEqualTo("section caption");
    }

    @Test
    void testSectionCaption_WithCloseButton() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC005);
        var section = getSection(BindCaptionWithCloseButtonPmo.class);
        var captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithCloseButtonPmo.PROPERTY_SECTION_CAPTION);

        captionField.setValue("section caption");

        assertThat(section.getCaption()).isEqualTo("section caption");
    }

    @Test
    void testSectionCaption_EmptyCaptionHidesElement() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC002);
        var section = getSection(BindCaptionWithoutButtonPmo.class);
        var captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);

        captionField.setValue("");

        assertThat(section.$(H4Element.class).single().isDisplayed()).isFalse();
    }

    @Test
    void testSectionCaption_ButtonStaysVisibleWithEmptyCaption() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC004);
        var section = getSection(BindCaptionWithSectionHeaderButtonPmo.class);
        var captionField = section.$(TextFieldElement.class)
                .id(BindCaptionWithSectionHeaderButtonPmo.PROPERTY_SECTION_CAPTION);
        var button = section.getHeaderComponents().id("callAnAmbulance");

        captionField.setValue("");

        assertThat(section.$(H4Element.class).single().isDisplayed()).isFalse();
        assertThat(button.isDisplayed()).isTrue();
    }

    @Test
    void testButtonCaption() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC002);
        var section = getSection(BindCaptionWithoutButtonPmo.class);
        var captionField = section.getContent().$(TextFieldElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);
        var button = section.getContent().$(ButtonElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_BUTTON);

        captionField.setValue("button caption");

        assertThat(button.getText()).isEqualTo("button caption");
    }

    @Test
    void testCheckboxCaption() {
        goToTestCase(TestScenarioView.TS008, TestScenarioView.TC002);
        var section = getSection(BindCaptionWithoutButtonPmo.class);
        var captionField = section.getContent().$(TextFieldElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);
        var checkbox = section.getContent().$(CheckboxElement.class)
                .id(BindCaptionWithoutButtonPmo.PROPERTY_CHECKBOX);

        captionField.setValue("checkbox caption");

        assertThat(checkbox.getText()).isEqualTo("checkbox caption");
    }

}