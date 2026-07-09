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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.aspects.BindFormItemLabelWidthPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;

import com.vaadin.flow.component.html.testbench.DivElement;

class BindFormItemLabelWidthTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;

    @BeforeEach
    void goToTestCase() {
        testCaseSection = goToTestCase(TestScenarioView.TS008, TestScenarioView.TC023);
    }

    @Test
    void testStaticLabelWidth() {
        var labelWidthWithAnnotation = getLabelWidth(BindFormItemLabelWidthPmo.ExtendedLabelWidthPmo.class);
        var labelWidthWithoutAnnotation = getLabelWidth(BindFormItemLabelWidthPmo.DefaultPmo.class);

        assertThat(labelWidthWithAnnotation).isGreaterThan(labelWidthWithoutAnnotation);
    }

    private double getLabelWidth(Class<?> pmoClass) {
        var label = testCaseSection.$(LinkkiSectionElement.class).id(pmoClass.getSimpleName())
                .getContent()
                .$("vaadin-form-item").single()
                .$(DivElement.class).id("label");

        var width = label.getCssValue("width");
        return Double.parseDouble(width.replace("px", ""));
    }

}
