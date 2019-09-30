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

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.BugCollectionLayout;
import org.linkki.samples.playground.bugs.lin1486.ComboBoxVanishingValuePmo;
import org.linkki.samples.playground.uitest.AbstractUiTest;

import com.vaadin.testbench.elements.ComboBoxElement;

/**
 * LIN-1486
 */
public class ComboBoxVanishingValueTest extends AbstractUiTest {

    @Test
    public void testValueOnRefreshWithSameAvailbleValues() {
        clickButton(BugCollectionLayout.ID);

        ComboBoxElement comboBox = $(ComboBoxElement.class).id(ComboBoxVanishingValuePmo.PROPERTY_CHOICE);
        assertThat(comboBox.getValue(), is(not(nullValue())));

        clickButton(ComboBoxVanishingValuePmo.PROPERTY_CHANGE_CHOICES);
        clickButton(ComboBoxVanishingValuePmo.PROPERTY_UPDATE_BINDING_CONTEXT);
        assertThat(comboBox.getValue(), is(not(emptyOrNullString())));

    }

}
