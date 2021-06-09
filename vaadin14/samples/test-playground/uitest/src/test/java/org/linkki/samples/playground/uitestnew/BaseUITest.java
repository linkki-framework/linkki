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

package org.linkki.samples.playground.uitestnew;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.pageobjects.TestCaseSelectorElement;
import org.linkki.samples.playground.pageobjects.TestScenarioSelectorElement;
import org.linkki.samples.playground.uitest.extensions.DriverExtension;
import org.linkki.samples.playground.uitest.extensions.ScreenshotOnFailureExtension;

import com.vaadin.testbench.TestBenchTestCase;

/**
 * Helper class facilitates to use various Testbench functionality for UI testing. It helps to get
 * required web driver to launch the application on specific browser. UI test is done in this browser by
 * accessing various elements within the opened web page and by accessing their properties. SetUp method
 * must be called at the beginning of the test.
 * <p>
 * Various browser configuration options are available using the
 * {@link org.linkki.samples.playground.uitest.extensions.DriverExtension.Configuration @DriverExtension.Configuration}
 * annotation.
 */
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public abstract class BaseUITest extends TestBenchTestCase {

    @RegisterExtension
    protected static DriverExtension driverExtension = new DriverExtension();

    @RegisterExtension
    protected ScreenshotOnFailureExtension screenshotExtension = new ScreenshotOnFailureExtension(this);

    @BeforeAll
    void setUp() {
        setDriver(driverExtension.getDriver());
    }

    protected TestCaseComponentElement goToTestCase(String testScenarioId, String testCaseId) {
        TestScenarioSelectorElement testScenarioSelector = $(TestScenarioSelectorElement.class).first();
        TestCaseSelectorElement testCaseSelector = testScenarioSelector.selectTestScenario(testScenarioId);
        return testCaseSelector.selectTestCase(testCaseId);
    }

}
