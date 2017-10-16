/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;
import org.linkki.core.binding.TestEnum;

import com.vaadin.ui.ComboBox;

public class UIComboBoxTest {

    @UISection
    protected static class TestPmo {

        @UIComboBox(position = 1)
        public TestEnum getFoo() {
            return TestEnum.ONE;
        }

        public Set<TestEnum> getFooAvailableValues() {
            LinkedHashSet<TestEnum> someValues = new LinkedHashSet<>();
            someValues.add(TestEnum.ONE);
            someValues.add(TestEnum.TWO);
            someValues.add(TestEnum.THREE);
            return someValues;
        }

        @ModelObject
        public TestPmo getModelObject() {
            return this;
        }
    }

    private ComboBox createComboBox() {
        TestPmo pmo = new TestPmo();
        ComboBox comboBox = (ComboBox)TestUi.componentBoundTo(pmo);
        return comboBox;
    }

    @Test
    public void testNullSelectionIsNotAllowed() {
        ComboBox comboBox = createComboBox();
        assertThat(comboBox.isNullSelectionAllowed(), is(false));
    }
}
