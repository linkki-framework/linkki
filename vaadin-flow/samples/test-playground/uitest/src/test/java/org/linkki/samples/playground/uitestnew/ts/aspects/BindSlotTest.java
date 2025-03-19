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
 *
 */

package org.linkki.samples.playground.uitestnew.ts.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.aspects.BindSlotPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.DivElement;

/**
 * UI test for {@link BindSlotPmo}.
 */
public class BindSlotTest extends PlaygroundUiTest {

    /**
     * Verifies that slots and its styles are set correctly.
     */
    @Test
    public void testBindSlots() {
        TestCaseComponentElement section = goToTestCase(TestScenarioView.TS008, TestScenarioView.TC016);

        var leftSlot = section.$(ButtonElement.class).id("leftButton");
        assertThat(leftSlot.getDomAttribute("slot")).isEqualTo("left-slot");

        var rightSlot = section.$(DivElement.class).id("rightSlot");
        assertThat(rightSlot.getDomAttribute("slot")).isEqualTo("right-slot");
        assertThat(rightSlot.getCssValue("padding-right")).isEqualTo("32px");
        assertThat(rightSlot.$(ButtonElement.class).all()).hasSize(2);
    }
}
