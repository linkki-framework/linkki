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

package org.linkki.samples.playground.bugs.uitest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.BugCollectionLayout;
import org.linkki.samples.playground.bugs.lin1442.ComboBoxCaptionRefreshPmo;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.openqa.selenium.Keys;

import com.vaadin.testbench.elements.ComboBoxElement;

/**
 * LIN-1442
 */
public class ComboBoxCaptionRefreshTest extends AbstractUiTest {

    @Test
    public void testValueOnResettingListItemValues() {
        clickButton(BugCollectionLayout.ID);

        ComboBoxElement comboBox = $(ComboBoxElement.class).id(ComboBoxCaptionRefreshPmo.PROPERTY_CHOICE);
        assertThat(comboBox.getValue(), is(not(nullValue())));
        String oldValue = comboBox.getValue();
        List<String> oldPopupSuggestions = comboBox.getPopupSuggestions();
        comboBox.sendKeys(Keys.ESCAPE);

        clickButton(ComboBoxCaptionRefreshPmo.PROPERTY_CHANGE_CHOICES_VALUES);
        assertThat(comboBox.getValue(), is(not(oldValue)));
        List<String> popupSuggestions = comboBox.getPopupSuggestions();
        for (int i = 0; i < oldPopupSuggestions.size(); i++) {
            assertThat(oldPopupSuggestions.get(i), is(not(popupSuggestions.get(i))));
        }
    }

}
