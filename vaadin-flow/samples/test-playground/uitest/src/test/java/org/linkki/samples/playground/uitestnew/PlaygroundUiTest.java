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

package org.linkki.samples.playground.uitestnew;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.pageobjects.TestCaseSelectorElement;
import org.linkki.samples.playground.pageobjects.TestScenarioSelectorElement;
import org.linkki.samples.playground.ts.TestCaseComponent;
import org.linkki.samples.playground.uitest.AbstractLinkkiUiTest;
import org.linkki.testbench.conditions.VaadinElementConditions;
import org.linkki.testbench.util.DriverProperties;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class PlaygroundUiTest extends AbstractLinkkiUiTest {

    protected TestCaseComponentElement goToTestCase(String testScenarioId, String testCaseId) {
        TestScenarioSelectorElement testScenarioSelector = $(TestScenarioSelectorElement.class).waitForFirst();
        TestCaseSelectorElement testCaseSelector = testScenarioSelector.selectTestScenario(testScenarioId);
        return testCaseSelector.selectTestCase(testCaseId);
    }

    protected TestCaseComponentElement goToTestCaseByUrl(String testScenarioId, String testCaseId) {
        getDriver().navigate().to(DriverProperties.getTestUrl(DEFAULT_CONTEXT_PATH, testScenarioId + "/" + testCaseId));

        return waitUntil(VaadinElementConditions
                .elementDisplayed($(TestCaseComponentElement.class)
                        .withAttribute("id", TestCaseComponent.getTestId(testScenarioId, testCaseId))));
    }

}
