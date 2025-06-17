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
package org.linkki.samples.playground.uitestnew.ts.linkkitheme;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.linkkitheme.FormItemLabelAlignmentComponent;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.tabs.testbench.TabElement;

class TC002FormItemLabelAlignmentTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;

    @BeforeEach
    void goToTestCase() {
        testCaseSection = goToTestCase(TestScenarioView.TS007, TestScenarioView.TC002);
    }

    @Test
    void testFormItemLabelAlignment_Start() {
        testCaseSection.getContentWrapper().$(TabElement.class)
                .id(FormItemLabelAlignmentComponent.ALIGNMENT_START).click();

        var label = testCaseSection.$(LinkkiSectionElement.class).id("BasicElementsLayoutLabelStartPmo")
                .getContent()
                .$("vaadin-form-item").first()
                .$(DivElement.class).id("label");
        assertThat(label.getCssValue("text-align")).isEqualTo("start");
    }

    @Test
    void testFormItemLabelAlignment_End() {
        testCaseSection.getContentWrapper().$(TabElement.class)
                .id(FormItemLabelAlignmentComponent.ALIGNMENT_END).click();

        var label = testCaseSection.$(LinkkiSectionElement.class).id("BasicElementsLayoutBehaviorUiSectionPmo")
                .getContent()
                .$("vaadin-form-item").first()
                .$(DivElement.class).id("label");
        assertThat(label.getCssValue("text-align")).isEqualTo("end");
    }

}
