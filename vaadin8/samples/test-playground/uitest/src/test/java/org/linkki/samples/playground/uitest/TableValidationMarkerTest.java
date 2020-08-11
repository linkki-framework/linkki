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

package org.linkki.samples.playground.uitest;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.allelements.AllUiElementsTabsheetArea;
import org.linkki.samples.playground.table.PlaygroundTablePmo;
import org.linkki.samples.playground.table.TablePage;

import com.vaadin.testbench.elements.TextFieldElement;

@SuppressWarnings("deprecation")
public class TableValidationMarkerTest extends AbstractUiTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testMarkersInTable() {
        clickButton(TablePage.ID);

        com.vaadin.testbench.elements.TableElement table = $(com.vaadin.testbench.elements.TableElement.class)
                .id(PlaygroundTablePmo.class.getSimpleName());

        String value = "Name 2";
        TextFieldElement textField = findTextFieldByValue(table, value);
        value = "abc";
        textField.setValue(value);

        assertThat(textField.getClassNames(), hasItem("v-textfield-error"));

        clickButton(AllUiElementsTabsheetArea.ID);
        clickButton(TablePage.ID);

        table = $(com.vaadin.testbench.elements.TableElement.class)
                .id(PlaygroundTablePmo.class.getSimpleName());
        textField = findTextFieldByValue(table, value);
        assertThat(textField.getClassNames(), hasItem("v-textfield-error"));
    }

    private TextFieldElement findTextFieldByValue(com.vaadin.testbench.elements.TableElement table, String value) {
        return table.$(TextFieldElement.class).all().stream()
                .filter(e -> e.getValue().equals(value)).findFirst().get();
    }

}
