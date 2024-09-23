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

package org.linkki.core.ui.element.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.ui.test.WithLocale;

import com.github.mvysny.kaributesting.v10.ComboBoxKt;
import com.vaadin.flow.component.combobox.ComboBox;

@WithLocale("en")
@ExtendWith(KaribuUIExtension.class)
class UIComboBoxWithBooleanIntegrationTest {

    @Test
    void testComboBox_InclNull_ObjectBoolean() {
        var modelObject = new TestModelObjectWithObjectBoolean();
        var pmo = TestUiUtil.createSectionWith(new ComboBoxBooleanTestPmo(modelObject), new BindingContext());
        ComboBox<Boolean> component = TestUiUtil.getComponentById(pmo,"inclNull");

        assertThat(ComboBoxKt.getSuggestions(component)).containsExactly("Yes", "No");
        assertThat(component.isClearButtonVisible()).as("Clear button should be visible").isTrue();
        assertThat(component.getValue()).isNull();

        KaribuUtils.Fields.setValue(component, true);

        assertThat(component.getValue()).isTrue();

        KaribuUtils.Fields.setValue(component, null);

        assertThat(component.getValue()).isNull();
    }

    @Test
    void testComboBox_InclNull_PrimitiveBoolean() {
        var modelObject = new TestModelObjectWithPrimitiveBoolean();
        var pmo = TestUiUtil.createSectionWith(new ComboBoxBooleanTestPmo(modelObject), new BindingContext());
        ComboBox<Boolean> component = TestUiUtil.getComponentById(pmo,"inclNull");

        assertThat(ComboBoxKt.getSuggestions(component)).containsExactly("Yes", "No");
        assertThat(component.isClearButtonVisible())
                .as("Clear button should not be visible " +
                        "although AvailableValuesType.ENUM_VALUES_INCL_NULL is used")
                .isFalse();
        assertThat(component.getValue()).isFalse();

        KaribuUtils.Fields.setValue(component, true);

        assertThat(component.getValue()).isTrue();

        KaribuUtils.Fields.setValue(component, null);

        assertThat(component.getValue())
                .as("Setting the value to null should be ignored, " +
                        "making the combobox to restore the previous valid value")
                .isTrue();
    }

    @Test
    void testComboBox_ExclNull_ObjectBoolean() {
        var modelObject = new TestModelObjectWithObjectBoolean();
        var pmo = TestUiUtil.createSectionWith(new ComboBoxBooleanTestPmo(modelObject), new BindingContext());
        ComboBox<Boolean> component = TestUiUtil.getComponentById(pmo,"exclNull");

        assertThat(ComboBoxKt.getSuggestions(component)).containsExactly("Yes", "No");
        assertThat(component.isClearButtonVisible())
                .as("Clear button should not be visible although data type permits null").isFalse();
        assertThat(component.getValue()).isNull();

        KaribuUtils.Fields.setValue(component, true);

        assertThat(component.getValue()).isTrue();

        KaribuUtils.Fields.setValue(component, null);

        assertThat(component.getValue())
                .as("Setting the value to null should be ignored, " +
                        "making the combobox to restore the previous valid value")
                .isTrue();
    }

    @Test
    void testComboBox_ExclNull_PrimitiveBoolean() {
        var modelObject = new TestModelObjectWithPrimitiveBoolean();
        var pmo = TestUiUtil.createSectionWith(new ComboBoxBooleanTestPmo(modelObject), new BindingContext());
        ComboBox<Boolean> component = TestUiUtil.getComponentById(pmo,"exclNull");

        assertThat(ComboBoxKt.getSuggestions(component)).containsExactly("Yes", "No");
        assertThat(component.isClearButtonVisible())
                .as("Clear button should not be visible").isFalse();
        assertThat(component.getValue()).isFalse();

        KaribuUtils.Fields.setValue(component, true);

        assertThat(component.getValue()).isTrue();

        KaribuUtils.Fields.setValue(component, null);

        assertThat(component.getValue())
                .as("Setting the value to null should be ignored, " +
                        "making the combobox to restore the previous valid value")
                .isTrue();
    }

    @UISection
    static class ComboBoxBooleanTestPmo {

        @ModelObject
        private final Object modelObject;

        public ComboBoxBooleanTestPmo(Object modelObject) {
            this.modelObject = modelObject;
        }

        @UIComboBox(position = 0, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = "value")
        public void inclNull() {
            // model binding
        }

        @UIComboBox(position = 1, content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, modelAttribute = "value")
        public void exclNull() {
            // model binding
        }
    }

    static class TestModelObjectWithObjectBoolean {

        private Boolean value;

        public Boolean getValue() {
            return value;
        }

        public void setValue(Boolean value) {
            this.value = value;
        }

    }

    static class TestModelObjectWithPrimitiveBoolean {

        private boolean value;

        public boolean getValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

    }
}