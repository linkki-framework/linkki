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

package org.linkki.samples.playground.pageobjects;

import static org.linkki.testbench.conditions.VaadinElementConditions.elementDisplayed;

import com.vaadin.flow.component.tabs.testbench.TabElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.annotations.Attribute;
import com.vaadin.testbench.elementsbase.Element;

/**
 * Page object for a test scenario selector
 */
@Element("linkki-tab-layout")
@Attribute(name = "id", value = "test-scenario-selector")
public class TestScenarioSelectorElement extends TestBenchElement {

    public TestCaseSelectorElement selectTestScenario(String id) {
        $(TabElement.class).id(id).click();

        return waitUntil(elementDisplayed($(TestCaseSelectorElement.class)));
    }

}
