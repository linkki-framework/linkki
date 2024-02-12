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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.linkki.testbench.util.DriverProperties;

public class FrontendDependenciesUITest extends PlaygroundUiTest {
    @BeforeEach
    void beforeEach() {
        getDriver().navigate().to(DriverProperties.getTestUrl(DEFAULT_CONTEXT_PATH, "bugs/LIN-3497"));
    }

    @Test
    void annotationsOnInjectedComponentsAndPmosAreAddedToBundle() {
        LinkkiSectionElement pmoWithCssImport = $(LinkkiSectionElement.class).id("PmoWithCssImport");
        // should have grey (#eee/238/238/238) background
        assertThat(pmoWithCssImport.getCssValue("background-color"), is("rgba(238, 238, 238, 1)"));
        // element should be displayed
        assertThat($("component-with-custom-resource").first().getText(), is("caption in resource"));
    }

}
