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

package org.linkki.samples.playground.uitestnew.ts.messages;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.flow.component.icon.testbench.IconElement;

/**
 * Note: Selenium always returns an rgba(...) value, the 1 means no transparency.
 */
class TC001MessageTableTest extends PlaygroundUiTest {

    private static final String MESSAGE_TABLE = "MessageTablePmo_table";

    @BeforeEach
    void setup() {
        super.setUp();
        goToTestCase(TestScenarioView.TS013, TestScenarioView.TC001);
    }

    @Test
    void testTitle() {
        var span = $(SpanElement.class).id(MESSAGE_TABLE + "_title");
        assertThat(span.getText()).isEqualTo("Message Table");
    }
    @Test
    void testTitleDuplicateMessageDoesNotShowAsEmptyRow() {
        GridElement grid = $(GridElement.class).id(MESSAGE_TABLE);
        assertThat(grid.getRowCount()).isEqualTo(3);
    }

    @Test
    void testErrorIcon() {
        GridElement grid = $(GridElement.class).id(MESSAGE_TABLE);
        IconElement icon = grid.getCell(0, 0).$(IconElement.class).first();

        assertThat(icon.getCssValue("color")).isEqualTo("rgba(210, 67, 67, 1)");
        assertThat(icon.getAttribute("icon")).isEqualTo("vaadin:exclamation-circle");
    }

    @Test
    void testWarningIcon() {
        GridElement grid = $(GridElement.class).id(MESSAGE_TABLE);
        IconElement icon = grid.getCell(1, 0).$(IconElement.class).first();

        assertThat(icon.getCssValue("color")).isEqualTo("rgba(242, 174, 85, 1)");
        assertThat(icon.getAttribute("icon")).isEqualTo("vaadin:warning");
    }

    @Test
    void testInfoIcon() {
        GridElement grid = $(GridElement.class).id(MESSAGE_TABLE);
        IconElement icon = grid.getCell(2, 0).$(IconElement.class).first();

        assertThat(icon.getCssValue("color")).isEqualTo("rgba(153, 199, 255, 1)");
        assertThat(icon.getAttribute("icon")).isEqualTo("vaadin:info-circle");
    }

}
