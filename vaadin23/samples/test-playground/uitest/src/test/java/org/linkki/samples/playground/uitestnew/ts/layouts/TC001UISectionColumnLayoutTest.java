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

package org.linkki.samples.playground.uitestnew.ts.layouts;

import org.junit.jupiter.api.BeforeEach;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorUiSectionComponent;

import com.vaadin.flow.component.tabs.testbench.TabElement;

class TC001UISectionColumnLayoutTest extends TC001AbstractSectionTest {

    @Override
    protected String getTestCaseId() {
        return TestScenarioView.TC001;
    }

    @Override
    @BeforeEach
    void setup() {
        super.setup();
        getTestCaseSection().getContentWrapper().$(TabElement.class)
                .id(BasicElementsLayoutBehaviorUiSectionComponent.FORM).click();
    }
}
