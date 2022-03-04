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


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.aspects.BindComboBoxItemStylePmo.TextColor;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.testbench.TestBenchElement;

class BindComboBoxItemStyleTest extends PlaygroundUiTest {
    private TestCaseComponentElement testCaseSection;

    @BeforeEach
    void goToTestCase() {
        testCaseSection = goToTestCase(TestScenarioView.TS008, TestScenarioView.TC011);
    }

    @Test
    void testStaticItemStyle() {
        ComboBoxElement comboBox = testCaseSection.$(ComboBoxElement.class).id("greyText");

        comboBox.openPopup();

        $("vaadin-combo-box-item").all().forEach(i -> assertThat(i.$(DivElement.class).last().getClassNames())
                .contains("text-secondary"));

        comboBox.closePopup();
    }

    @Test
    void testDynamicItemStyle() {
        ComboBoxElement comboBox = testCaseSection.$(ComboBoxElement.class).id("textColor");

        comboBox.openPopup();

        List<TestBenchElement> items = $("vaadin-combo-box-item").all();

        for (int i = 0; i < TextColor.values().length; i++) {
            TestBenchElement comboItem = items.get(i);
            TextColor item = TextColor.values()[i];

            assertThat(comboItem.$(DivElement.class).last().getClassNames())
                    .contains(item.getStyleName());
        }

        comboBox.closePopup();
    }


}