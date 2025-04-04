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

package org.linkki.samples.playground.uitestnew.ts.ips;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.UITestConfiguration;
import org.openqa.selenium.By;

import com.vaadin.flow.component.html.testbench.NativeLabelElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class TC001IpsComponentsTest {

    @Nested
    @UITestConfiguration(locale = "en")
    class TC001IpsComponentsTestEn extends AbstractTC001IpsComponentsTest {
        TC001IpsComponentsTestEn() {
            super("A String attribute", "Overriden attribute");
        }
    }

    @Nested
    @UITestConfiguration(locale = "de")
    class TC001IpsComponentsTestDe extends AbstractTC001IpsComponentsTest {
        TC001IpsComponentsTestDe() {
            super("Ein String-Attribut", "Überschriebenes Attribut");
        }
    }

    private abstract class AbstractTC001IpsComponentsTest extends PlaygroundUiTest {
        private final String modelObjectLabel;
        private final String modelObjectChildLabel;
        private TestCaseComponentElement testCaseSection;

        AbstractTC001IpsComponentsTest(String modelObjectLabel, String modelObjectChildLabel) {
            this.modelObjectLabel = modelObjectLabel;
            this.modelObjectChildLabel = modelObjectChildLabel;
        }

        @BeforeEach
        void goToTestCase() {
            testCaseSection = goToTestCase(TestScenarioView.TS004, TestScenarioView.TC001);
        }

        @Test
        void testModelAttribute_Label() {
            var textField = testCaseSection.$(TextFieldElement.class).id("string");
            var label = textField
                    .findElement(By.xpath("./.."))
                    .$(NativeLabelElement.class).last();

            assertThat(label.getText(), is(modelObjectLabel));
            assertThat(label.getTagName(), is("label"));
        }

        @Test
        void testModelAttribute_ModelObjectChild_Label() {
            var textField = testCaseSection.$(TextFieldElement.class).id("getStringFromModelObjectChild");
            var label = textField
                    .findElement(By.xpath("./.."))
                    .$(NativeLabelElement.class).last();

            assertThat(label.getText(), is(modelObjectChildLabel));
            assertThat(label.getTagName(), is("label"));
        }

        @Test
        void testModelAttribute_ModelObjectNull_Label() {
            var textField = testCaseSection.$(TextFieldElement.class).id("getStringWithModelObjectNull");
            var label = textField
                    .findElement(By.xpath("./.."))
                    .$(NativeLabelElement.class).last();

            assertThat(label.getText(), is(modelObjectLabel));
            assertThat(label.getTagName(), is("label"));
        }
    }
}
