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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class BindSuffixTest extends PlaygroundUiTest {

    @Test
    public void testSuffixStatic() {
        TestCaseComponentElement section = goToTestCase(TestScenarioView.TS008,
                                                        TestScenarioView.TC009);
        TextFieldElement suffixTextField = section.$(TextFieldElement.class)
                .id("suffixStaticText");
        WebElement suffixDiv = getSuffix(suffixTextField);

        assertThat(suffixDiv.getText(), is("A nice static suffix"));

    }

    @Test
    public void testSuffixDynamic_WithValue() {
        TestCaseComponentElement section = goToTestCase(TestScenarioView.TS008,
                                                        TestScenarioView.TC009);

        TextFieldElement suffixTextField = section.$(TextFieldElement.class)
                .id("suffixDynamicText");
        TextFieldElement input = section.$(TextFieldElement.class).id("suffixText");
        input.setValue("I can be changed");
        WebElement suffixDiv = getSuffix(suffixTextField);

        assertThat(suffixDiv.getText(), is("I can be changed"));

        input.setValue("I got changed");
        suffixDiv = getSuffix(suffixTextField);

        assertThat(suffixDiv.getText(), is("I got changed"));
    }

    @Test
    public void testSuffixDynamic_WithEmptyValue() {
        TestCaseComponentElement section = goToTestCase(TestScenarioView.TS008,
                                                        TestScenarioView.TC009);

        TextFieldElement suffixTextField = section.$(TextFieldElement.class)
                .id("suffixDynamicText");
        TextFieldElement input = section.$(TextFieldElement.class).id("suffixText");
        input.setValue("I can be changed");
        WebElement suffixDiv = getSuffix(suffixTextField);

        assertThat(suffixDiv.getText(), is("I can be changed"));

        input.setValue("");
        suffixDiv = getSuffix(suffixTextField);

        assertThat(suffixDiv.getText(), is(""));
    }

}
