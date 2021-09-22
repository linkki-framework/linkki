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
import org.linkki.samples.playground.pageobjects.LinkkiSectionElement;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.uitest.extensions.DriverExtension.Configuration;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.html.testbench.LabelElement;

public class TC001IpsComponentsTest extends PlaygroundUiTest {

    @Nested
    @Configuration(locale = "en")
    class TC001IpsComponentsTestEn extends AbstractTC001IpsComponentsTest {
        TC001IpsComponentsTestEn() {
            super("A String attribute");
        }
    }

    @Nested
    @Configuration(locale = "de")
    class TC001IpsComponentsTestDe extends AbstractTC001IpsComponentsTest {
        TC001IpsComponentsTestDe() {
            super("Ein String-Attribut");
        }
    }

    private abstract class AbstractTC001IpsComponentsTest extends PlaygroundUiTest {
        private TestCaseComponentElement testCaseSection;
        private LinkkiSectionElement section;

        private String expectedlabelValue;
        private String actualLabelValue;

        @BeforeEach
        void setup() {
            super.setUp();
            testCaseSection = goToTestCase("TS004", "TC001");
            section = testCaseSection.getContentWrapper().$(LinkkiSectionElement.class).first();

            actualLabelValue = section.$(LabelElement.class).first().getText();
        }

        AbstractTC001IpsComponentsTest(String labelValue) {
            this.expectedlabelValue = labelValue;
        }

        @Test
        void testModelAttribute_Label() {
            assertThat(actualLabelValue, is(expectedlabelValue));
        }

    }
}
