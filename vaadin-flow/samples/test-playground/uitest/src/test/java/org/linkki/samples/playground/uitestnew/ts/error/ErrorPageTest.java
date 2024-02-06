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

package org.linkki.samples.playground.uitestnew.ts.error;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.By;

import com.vaadin.flow.component.button.testbench.ButtonElement;

class ErrorPageTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS017, TestScenarioView.TC002);
    }

    @Test
    void testErrorPage() {
        $(ButtonElement.class).id("runtimeException").click();

        var errorPage = findElements(By.id("CustomErrorPage"));
        assertThat(errorPage).hasSize(1);

        $(ButtonElement.class).get(0).click();
        assertThat(getDriver().getCurrentUrl()).endsWith(DEFAULT_CONTEXT_PATH + "/");
    }
}
