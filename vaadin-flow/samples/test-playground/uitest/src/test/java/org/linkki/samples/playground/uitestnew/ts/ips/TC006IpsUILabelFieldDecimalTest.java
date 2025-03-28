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

package org.linkki.samples.playground.uitestnew.ts.ips;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.linkki.testbench.pageobjects.LinkkiTextElement;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class TC006IpsUILabelFieldDecimalTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;
    private LinkkiSectionElement section;

    @BeforeEach
    void setup() {
        testCaseSection = goToTestCase(TestScenarioView.TS004, TestScenarioView.TC006);
        section = testCaseSection.getContentWrapper().$(LinkkiSectionElement.class).first();
    }

    @Test
    void testDecimalNullUILabel_NormalValue() {
        LinkkiTextElement labelField = section.$(LinkkiTextElement.class).id("decimalLabel");
        TextFieldElement field = section.$(TextFieldElement.class).id("decimalValue");

        field.setValue("1,00");

        assertThat(labelField.getText(), is("1,00"));
    }
}
