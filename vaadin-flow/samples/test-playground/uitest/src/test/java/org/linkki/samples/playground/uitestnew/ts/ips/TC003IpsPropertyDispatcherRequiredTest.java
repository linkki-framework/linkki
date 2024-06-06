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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class TC003IpsPropertyDispatcherRequiredTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;
    private LinkkiSectionElement section;

    @BeforeEach
    void setup() {
        testCaseSection = goToTestCase(TestScenarioView.TS004, TestScenarioView.TC003);
        section = testCaseSection.getContentWrapper().$(LinkkiSectionElement.class).first();
    }

    @Test
    void testRequired_UnrestrictedValueSetExclNull_ShouldBeRequired() {
        TextFieldElement textField = section.$(TextFieldElement.class).id("unrestrictedValueSetExclNull");

        assertThat(textField.hasAttribute("required"), is(true));
    }

    @Test
    void testRequired_UnrestrictedValueSetInclNull_ShouldNotBeRequired() {
        TextFieldElement textField = section.$(TextFieldElement.class).id("unrestrictedValueSetInclNull");

        assertThat(textField.hasAttribute("required"), is(false));
    }

}
