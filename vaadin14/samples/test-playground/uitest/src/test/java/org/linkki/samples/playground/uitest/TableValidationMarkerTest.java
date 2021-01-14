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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.table.PlaygroundTablePmo;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.tabs.testbench.TabElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class TableValidationMarkerTest extends AbstractUiTest {

    @Test
    public void testMarkersInTable() {
        $(TabElement.class).all().stream().filter(t -> "Tables".equals(t.getText())).findFirst().get().click();

        GridElement table = $(GridElement.class)
                .id(PlaygroundTablePmo.class.getSimpleName() + "_table");

        String value = "Name 2";
        TextFieldElement textField = findTextFieldByValue(table, value);
        value = "abc";
        textField.setValue(value);

        assertThat(textField.getAttribute("invalid"), is("true"));

        $(TabElement.class).all().stream().filter(t -> "All".equals(t.getText())).findFirst().get().click();
        $(TabElement.class).all().stream().filter(t -> "Tables".equals(t.getText())).findFirst().get().click();

        table = $(GridElement.class)
                .id(PlaygroundTablePmo.class.getSimpleName() + "_table");
        textField = findTextFieldByValue(table, value);
        assertThat(textField.getAttribute("invalid"), is("true"));
    }

    private TextFieldElement findTextFieldByValue(GridElement table, String value) {
        return table.$(TextFieldElement.class).all().stream()
                .filter(e -> e.getValue().equals(value)).findFirst().get();
    }

}
