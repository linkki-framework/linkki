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

package org.linkki.samples.playground.uitestnew.ts.components;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.data.Offset.offset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiTextElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.H2Element;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;

class TC003HeadlineTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;

    @BeforeEach
    void goToTestCase() {
        testCaseSection = goToTestCase(TestScenarioView.TS010, TestScenarioView.TC003);
    }

    @Test
    void testHeadline() {
        var headline = testCaseSection.$(HorizontalLayoutElement.class).id("headline-component");

        assertThat(headline.$(H2Element.class).first().getText())
                .as("Title is still shown although children are added to the title")
                .contains("Headline title");
        assertThat(headline.$(LinkkiTextElement.class).first().getRect().getX())
                .as("Components added with addToTitle should be directly after the headline text with a gap")
                .isCloseTo(606, offset(1));
        assertThat(headline.$(ButtonElement.class).first().getLocation().getX())
                .as("Components added with getContent().add should be right aligned")
                .isGreaterThan(1000);
    }

    @Test
    void testUIHeadline_updateTitleOnModelChanged() {
        var headlineToTest = testCaseSection.$(HorizontalLayoutElement.class).id("headline");
        var titleBeforeButtonClick = headlineToTest.getText();
        int counterBeforeClick = Integer.parseInt(titleBeforeButtonClick.split(" - ")[1]);

        testCaseSection.$(ButtonElement.class).id("increment").click();

        assertThat(headlineToTest.getText()).contains(String.valueOf(counterBeforeClick + 1));
    }
}
