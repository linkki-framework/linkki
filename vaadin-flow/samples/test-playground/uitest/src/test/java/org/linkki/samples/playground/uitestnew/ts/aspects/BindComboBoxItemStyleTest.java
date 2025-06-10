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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.aspects.BindComboBoxItemStylePmo.BindComboBoxItemStyleWithComboBoxPmo;
import org.linkki.samples.playground.ts.aspects.BindComboBoxItemStylePmo.BindComboBoxItemStyleWithMultiSelectPmo;
import org.linkki.samples.playground.ts.aspects.BindComboBoxItemStylePmo.TextColor;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.combobox.testbench.MultiSelectComboBoxElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.html.testbench.InputTextElement;

class BindComboBoxItemStyleTest extends PlaygroundUiTest {
    private TestCaseComponentElement testCaseSection;

    @BeforeEach
    void goToTestCase() {
        testCaseSection = goToTestCase(TestScenarioView.TS008, TestScenarioView.TC011);
    }

    @Test
    void testStaticItemStyle_ComboBox() {
        var section = testCaseSection.$(LinkkiSectionElement.class)
                .id(BindComboBoxItemStyleWithComboBoxPmo.class.getSimpleName());
        var comboBox = section.$(ComboBoxElement.class).id("greyText");

        comboBox.openPopup();

        assertThat($("vaadin-combo-box-item").all())
                .allSatisfy(i -> assertThat(i.$(DivElement.class).last().getClassNames())
                        .contains("text-secondary"));

        comboBox.closePopup();
    }

    @Test
    void testDynamicItemStyle_ComboBox() {
        var section = testCaseSection.$(LinkkiSectionElement.class)
                .id(BindComboBoxItemStyleWithComboBoxPmo.class.getSimpleName());
        var comboBox = section.$(ComboBoxElement.class).id("textColor");

        comboBox.openPopup();

        assertThat($("vaadin-combo-box-item").all())
                .map(i -> i.$(DivElement.class).last().getClassNames())
                .isEqualTo(Arrays.stream(TextColor.values())
                        .map(TextColor::getStyleName)
                        .map(Set::of)
                        .collect(Collectors.toList()));

        comboBox.closePopup();
    }

    @Test
    void testDynamicItemStyleWithAlignment_ComboBox() {
        var section = testCaseSection.$(LinkkiSectionElement.class)
                .id(BindComboBoxItemStyleWithComboBoxPmo.class.getSimpleName());
        var comboBox = section.$(ComboBoxElement.class).id("alignedText");
        assertThat(comboBox.$(InputTextElement.class).first().getCssValue("text-align")).isEqualTo("end");

        comboBox.openPopup();

        assertThat($("vaadin-combo-box-item").all())
                .allSatisfy(i -> assertThat(i.$(DivElement.class).last().getClassNames())
                        .contains("text-right", "text-primary"));

        comboBox.closePopup();
    }

    @Test
    void testStaticItemStyle_MultiSelect() {
        var section = testCaseSection.$(LinkkiSectionElement.class)
                .id(BindComboBoxItemStyleWithMultiSelectPmo.class.getSimpleName());
        var comboBox = section.$(MultiSelectComboBoxElement.class).id("greyText");

        comboBox.openPopup();

        assertThat($("vaadin-multi-select-combo-box-item").all())
                .allSatisfy(i -> assertThat(i.$(DivElement.class).last().getClassNames())
                        .contains("text-secondary"));

        comboBox.closePopup();
    }

    @Test
    void testDynamicItemStyle_MultiSelect() {
        var section = testCaseSection.$(LinkkiSectionElement.class)
                .id(BindComboBoxItemStyleWithMultiSelectPmo.class.getSimpleName());
        var comboBox = section.$(MultiSelectComboBoxElement.class).id("textColor");

        comboBox.openPopup();

        assertThat($("vaadin-multi-select-combo-box-item").all())
                .map(i -> i.$(DivElement.class).last().getClassNames())
                .isEqualTo(Arrays.stream(TextColor.values())
                        .map(TextColor::getStyleName)
                        .map(Set::of)
                        .collect(Collectors.toList()));

        comboBox.closePopup();
    }
}