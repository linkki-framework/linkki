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

package org.linkki.samples.playground.uitestnew.ts.components;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.html.testbench.InputTextElement;

class TC007UIComboBoxTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC007);
    }

    @Test
    void testClearButton_ComboBoxWithNull() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");

        assertThat(hasClearButton(comboBoxElement), is(true));
    }

    @Test
    void testClearButton_ComboBoxWithoutNull() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithoutNull");

        assertThat(hasClearButton(comboBoxElement), is(false));
    }

    private boolean hasClearButton(ComboBoxElement element) {
        return element.hasAttribute("clear-button-visible");
    }

    @Test
    void testRemoveContent_ComboBoxWithNull() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");
        comboBoxElement.clear();

        assertThat(comboBoxElement.getInputElementValue()).isNullOrEmpty();
    }

    @Test
    void testRemoveContent_ComboBoxWithoutNull() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithoutNull");
        comboBoxElement.clear();

        assertThat(comboBoxElement.getInputElementValue()).isNotNull();
    }

    @Test
    void testLeftAligned() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("leftAligned");

        comboBox.openPopup();

        // should be 'left' but Vaadin set 'start' instead
        assertThat(comboBox.$(InputTextElement.class).first().getCssValue("text-align")).isEqualTo("start");
        $("vaadin-combo-box-item").all().forEach(i -> assertThat(i.$(DivElement.class).last()
                .getCssValue("text-align")).isEqualTo("left"));

        comboBox.closePopup();
    }

    @Test
    void testCenterAligned() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("centerAligned");

        comboBox.openPopup();

        assertThat(comboBox.$(InputTextElement.class).first().getCssValue("text-align")).isEqualTo("center");
        $("vaadin-combo-box-item").all().forEach(i -> assertThat(i.$(DivElement.class).last()
                .getCssValue("text-align")).isEqualTo("center"));

        comboBox.closePopup();
    }

    @Test
    void testRightAligned() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("rightAligned");

        comboBox.openPopup();

        // should be 'right' but Vaadin set 'end' instead
        assertThat(comboBox.$(InputTextElement.class).first().getCssValue("text-align")).isEqualTo("end");
        $("vaadin-combo-box-item").all().forEach(i -> assertThat(i.$(DivElement.class).last()
                .getCssValue("text-align")).isEqualTo("right"));

        comboBox.closePopup();
    }

    @Test
    void testFillInitiallyNonNullButRequiredString_shouldNotBeInvalid() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("nonNullStringValue");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
        comboBox.selectByText("1");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
        comboBox.selectByText("");
        assertThat(comboBox.hasAttribute("invalid")).isTrue();
        comboBox.selectByText("1");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
    }

    @Test
    void testFillInitiallyNonNullButRequiredDecimal_shouldNotBeInvalid() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("nonNullDecimalValue");
        comboBox.scrollIntoView();
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
        comboBox.selectByText("1,00");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
        comboBox.selectByText("");
        assertThat(comboBox.hasAttribute("invalid")).isTrue();
        comboBox.selectByText("1,00");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
    }

}
