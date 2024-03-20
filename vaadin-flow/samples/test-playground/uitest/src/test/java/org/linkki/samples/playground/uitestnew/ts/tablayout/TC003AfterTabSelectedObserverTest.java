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

package org.linkki.samples.playground.uitestnew.ts.tablayout;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.linkki.testbench.pageobjects.LinkkiTextElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.tabs.testbench.TabElement;

class TC003AfterTabSelectedObserverTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseComponent;

    @BeforeEach
    void goToTestCase() {
        testCaseComponent = goToTestCaseByUrl(TestScenarioView.TS010, TestScenarioView.TC004);
    }

    @Test
    void testUpdate() {
        testCaseComponent.$(TabElement.class).id("sheet-without-observer").click();
        String value = getComponentWithoutObserver().$(LinkkiTextElement.class).id("value").getText();
        testCaseComponent.$(TabElement.class).id("sheet-with-observer").click();
        assertThat(getComponentWithObserver().$(LinkkiTextElement.class).id("value").getText()).isEqualTo(value);
        testCaseComponent.$(TabElement.class).id("sheet-without-observer").click();
        $(ButtonElement.class).withId("changeValue").first().click();
        String changedValue = getComponentWithoutObserver().$(LinkkiTextElement.class).id("value").getText();

        testCaseComponent.$(TabElement.class).id("sheet-with-observer").click();
        assertThat(getComponentWithObserver().$(LinkkiTextElement.class).id("value").getText()).isEqualTo(changedValue);
    }

    private LinkkiSectionElement getComponentWithoutObserver() {
        return testCaseComponent.$(LinkkiSectionElement.class).id("withoutObserver");
    }

    private LinkkiSectionElement getComponentWithObserver() {
        return testCaseComponent.$(LinkkiSectionElement.class).id("withObserver");
    }
}
