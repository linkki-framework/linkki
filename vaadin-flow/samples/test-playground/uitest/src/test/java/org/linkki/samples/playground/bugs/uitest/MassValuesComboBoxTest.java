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

package org.linkki.samples.playground.bugs.uitest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.BugCollectionView;
import org.linkki.samples.playground.bugs.lin2622.MassValuesComboBoxPmo;
import org.linkki.samples.playground.uitest.AbstractLinkkiUiTest;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;

/**
 * LIN-2622
 */
public class MassValuesComboBoxTest extends AbstractLinkkiUiTest {

    @Test
    public void testValueOnRefreshWithSameAvailableValues() {
        goToView(BugCollectionView.ROUTE);
        openTab(MassValuesComboBoxPmo.CAPTION);

        ComboBoxElement comboBox = $(ComboBoxElement.class).id(MassValuesComboBoxPmo.PROPERTY_MASS_VALUE);

        comboBox.openPopup();
        comboBox.selectByText("DE");

        assertThat(comboBox.getSelectedText(), is("DE"));
    }

}
