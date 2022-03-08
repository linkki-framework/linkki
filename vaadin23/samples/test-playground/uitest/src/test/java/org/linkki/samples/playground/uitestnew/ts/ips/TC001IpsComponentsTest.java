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

package org.linkki.samples.playground.uitestnew.ts.ips;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitest.Locator;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.UITestConfiguration;

import com.vaadin.testbench.TestBenchElement;

public class TC001IpsComponentsTest extends PlaygroundUiTest {

    @Nested
    @UITestConfiguration(locale = "en")
    class TC001IpsComponentsTestEn extends AbstractTC001IpsComponentsTest {
        TC001IpsComponentsTestEn() {
            super("A String attribute");
        }
    }

    @Nested
    @UITestConfiguration(locale = "de")
    class TC001IpsComponentsTestDe extends AbstractTC001IpsComponentsTest {
        TC001IpsComponentsTestDe() {
            super("Ein String-Attribut");
        }
    }

    private abstract class AbstractTC001IpsComponentsTest extends PlaygroundUiTest {
        private TestCaseComponentElement testCaseSection;

        private String expectedlabelValue;

        @BeforeEach
        void setup() {
            super.setUp();
            testCaseSection = goToTestCase(TestScenarioView.TS004, TestScenarioView.TC001);
        }

        AbstractTC001IpsComponentsTest(String labelValue) {
            this.expectedlabelValue = labelValue;
        }

        @Test
        void testModelAttribute_Label() {
            TestBenchElement label = testCaseSection.findElement(Locator.byText(expectedlabelValue));

            assertThat(label.getText(), is(expectedlabelValue));
            assertThat(label.getTagName(), is("label"));
        }

    }
}
