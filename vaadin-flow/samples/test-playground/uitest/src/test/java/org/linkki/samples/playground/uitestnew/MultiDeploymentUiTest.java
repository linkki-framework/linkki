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
package org.linkki.samples.playground.uitestnew;

import static org.linkki.test.matcher.Matchers.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.linkki.samples.playground.application.SampleView;
import org.linkki.samples.playground.uitest.AbstractLinkkiUiTest;
import org.linkki.testbench.WebDriverExtension;
import org.linkki.testbench.util.DriverProperties;
import org.openqa.selenium.WebDriver;

import com.vaadin.testbench.TestBenchElement;

class MultiDeploymentUiTest extends AbstractLinkkiUiTest {

    private static final String PLAYGROUND = "";
    private static final String F10_SAMPLE = "f10";
    private static final String F10_SAMPLE_CONTEXT_PATH = "linkki-f10-sample/ui";

    @RegisterExtension
    static WebDriverExtension f10SampleDriverExtension = new WebDriverExtension(F10_SAMPLE, F10_SAMPLE_CONTEXT_PATH);

    private String currentSystem = PLAYGROUND;

    @Override
    public WebDriver getDriver() {
        if (F10_SAMPLE.equals(currentSystem)) {
            return f10SampleDriverExtension.getDriver();
        } else {
            return super.getDriver();
        }
    }

    @Test
    void testPlaygroundShowsTestScenario() {
        currentSystem = PLAYGROUND;
        goToView(SampleView.NAME);
        assertThat($(TestBenchElement.class).withAttributeContaining("class", "linkki-main-area").exists());
    }

    @Test
    void f10SampleShowsApplication() {
        currentSystem = F10_SAMPLE;
        getDriver().navigate().to(DriverProperties.getTestUrl(F10_SAMPLE, F10_SAMPLE_CONTEXT_PATH, ""));
        assertThat($(TestBenchElement.class).withAttributeContaining("class", "linkki-main-area").exists());
    }

}
