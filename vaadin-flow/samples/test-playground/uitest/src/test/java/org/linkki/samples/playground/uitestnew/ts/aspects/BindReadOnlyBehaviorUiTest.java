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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.openqa.selenium.NoSuchElementException;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.InputTextElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class BindReadOnlyBehaviorUiTest extends PlaygroundUiTest {

    private TestCaseComponentElement testcase;

    @BeforeEach
    void goToTestCase() {
        testcase = goToTestCaseByUrl(TestScenarioView.TS008, TestScenarioView.TC012);
    }

    @Test
    void testBindReadOnlyBehavior_GivenReadOnly_WhenWritable_Writable() {
        var section = testcase.$(LinkkiSectionElement.class).all().get(1);

        var textfield = section.$(TextFieldElement.class).id("writableTextField");
        // TextFieldElement leads in this test to a empty value
        var input = textfield.$(InputTextElement.class).single();
        input.setValue("text");

        assertThat(input.getValue()).isEqualTo("text");
    }

    @Test
    void testBindReadOnlyBehavior_GivenWritable_WhenWritable_Writable() {
        var section = testcase.$(LinkkiSectionElement.class).all().get(0);

        var textfield = section.$(TextFieldElement.class).id("writableTextField");
        // TextFieldElement leads in this test to a empty value
        var input = textfield.$(InputTextElement.class).single();
        input.setValue("text");

        assertThat(input.getValue()).isEqualTo("text");
    }

    @Test
    void testBindReadOnlyBehavior_GivenReadOnly_WhenDisabled_Disabled() {
        var section = testcase.$(LinkkiSectionElement.class).all().get(1);

        var button = section.$(ButtonElement.class).id("disabledButton");

        assertThat(button.isEnabled()).isFalse();
    }

    @Test
    void testBindReadOnlyBehavior_GivenWritable_WhenDisabled_Enabled() {
        var section = testcase.$(LinkkiSectionElement.class).all().get(0);

        var button = section.$(ButtonElement.class).id("disabledButton");

        assertThat(button.isEnabled()).isTrue();
    }

    @Test
    void testBindReadOnlyBehavior_GivenReadOnly_WhenInvisible_Invisible() {
        var section = testcase.$(LinkkiSectionElement.class).all().get(1);

        var button = section.$(ButtonElement.class);

        assertThrows(NoSuchElementException.class, () -> button.id("invisibleButton"));
    }

    @Test
    void testBindReadOnlyBehavior_GivenWritable_WhenInvisible_Visible() {
        var section = testcase.$(LinkkiSectionElement.class).all().get(0);

        var button = section.$(ButtonElement.class).id("invisibleButton");

        assertThat(button.isDisplayed()).isTrue();
    }

    @Test
    void testBindReadOnlyBehavior_GivenWritable_WhenInvisibleIfWritable_NoButtonsDisplayed() {
        var section = testcase.$(LinkkiSectionElement.class).all().get(0);

        var button = section.$(ButtonElement.class);

        assertThrows(NoSuchElementException.class, () -> button.id("invisibleIfWritableButtonDefaultInvisible"));
        assertThrows(NoSuchElementException.class, () -> button.id("invisibleIfWritableButtonDefaultVisible"));
    }

    @Test
    void testBindReadOnlyBehavior_GivenReadOnly_WhenInvisibleIfWritable_OneButtonDisplayed() {
        var section = testcase.$(LinkkiSectionElement.class).all().get(1);

        var button = section.$(ButtonElement.class);
        var button2 = section.$(ButtonElement.class).id("invisibleIfWritableButtonDefaultVisible");

        assertThrows(NoSuchElementException.class,
                     () -> button.id("invisibleIfWritableButtonDefaultInvisible"));
        assertThat(button2.isDisplayed()).isTrue();
    }

}
