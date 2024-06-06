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

package org.linkki.samples.playground.uitestnew.ts.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.linkki.testbench.pageobjects.LinkkiTextElement;
import org.openqa.selenium.Dimension;

import com.vaadin.testbench.TestBenchElement;

public class UISectionHeaderTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;
    private LinkkiSectionElement section;
    private LinkkiTextElement label;

    @BeforeEach
    void setup() {
        testCaseSection = goToTestCase(TestScenarioView.TS002, TestScenarioView.TC001);

        section = testCaseSection.getContentWrapper().$(LinkkiSectionElement.class).first();

        label = section.$(LinkkiTextElement.class).id("label");
    }

    @Test
    void testSectionHeaderTitle() {
        assertThat(section.getCaption()).isEqualTo("I am a section caption");
    }

    @Test
    void testSectionHeaderComponents() {
        List<TestBenchElement> headerComponents = section.getHeaderComponents().all();

        assertThat(headerComponents).hasSize(3);
        assertThat(headerComponents.get(1).getTagName()).isEqualTo("vaadin-button");
        assertThat(headerComponents.get(1).getText()).isEqualTo("Button 1");
        assertThat(headerComponents.get(2).getTagName()).isEqualTo("vaadin-button");
        assertThat(headerComponents.get(2).getText()).isEqualTo("Button 2");
    }

    @Test
    void testSectionHeaderButtonsPosition_SameY() {
        getDriver().manage().window().setSize(new Dimension(1920, 1080));

        assertThat(section.getHeaderComponents().get(1).getLocation().getY())
                .describedAs("Button 1 and Button 2 should have the same y position")
                .isEqualTo(section.getHeaderComponents().get(2).getLocation().getY());
    }

    @Test
    void testSectionHeaderButtonsPosition_Order() {
        getDriver().manage().window().setSize(new Dimension(1920, 1080));

        assertThat(section.getHeaderComponents().get(1).getLocation().getX())
                .describedAs("Button 1 should be left of Button 2")
                .isLessThan(section.getHeaderComponents().get(2).getLocation().getX());
    }

    @Test
    void testLableChangeOnButtonClick() {
        assertThat(label.getText()).isEqualTo("none");

        section.getHeaderComponents().get(1).click();
        assertThat(label.getText()).isEqualTo("Button 1");

        section.getHeaderComponents().get(2).click();
        assertThat(label.getText()).isEqualTo("Button 2");
    }

}
