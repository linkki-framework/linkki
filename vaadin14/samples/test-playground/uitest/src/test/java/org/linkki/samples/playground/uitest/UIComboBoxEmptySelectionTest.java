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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;

public class UIComboBoxEmptySelectionTest extends AbstractUiTest {

    @Test
    public void testEmptyComboBoxSelection() {
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id("AllUiElementsUiSectionPmo");
        ComboBoxElement comboBoxElement = section.$(ComboBoxElement.class).id("enumValueComboBox");

        List<String> selectionList = Optional.ofNullable(comboBoxElement.getOptions()).orElse(Collections.emptyList());
        assertNotNull(selectionList);
        assertTrue(selectionList.size() > 0);

        String selectedElement = selectionList.get(0);

        String comboBoxId = comboBoxElement.getAttribute("id");

        // Test normal selection
        selectCombobox(comboBoxId, selectedElement);
        assertThat(getComboBoxSelection(comboBoxId), is(selectedElement));

        // Clear selection by clicking 'x'
        clearComboBoxSelection(comboBoxId);
        assertThat(getComboBoxSelection(comboBoxId), is(""));
    }

}
