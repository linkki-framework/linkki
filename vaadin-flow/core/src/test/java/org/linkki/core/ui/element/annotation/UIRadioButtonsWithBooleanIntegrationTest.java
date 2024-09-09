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

import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(KaribuUIExtension.class)
class UIRadioButtonsWithBooleanIntegrationTest {

    @Test
    void testBooleanRadioButtons_InclNull_ObjectBoolean() {
        var modelObject = new TestModelObjectWithObjectBoolean();
        var pmo = new BooleanRadioButtonsTestPmo(modelObject);
        var bindingContext = new BindingContext();
        var radioButtons = getRadioButtonsComponentAt(0, pmo, bindingContext);

        assertThat(radioButtons.getChildren().filter(c -> c.getElement().getTag().equals("vaadin-radio-button")))
                .map(c -> c.getElement().getTextRecursively()).containsExactly("Yes", "No", "Not Specified");
        assertThat(radioButtons.getChildren().map(c -> c.getElement().getProperty("checked")).toList())
                .containsOnly("false", "false", "true");
        assertThat(radioButtons.getListDataView().getItems().toList())
                .containsExactly(Optional.of(true), Optional.of(false), Optional.empty());

        KaribuUtils.Fields.setValue(radioButtons, Optional.of(true));

        assertThat(radioButtons.getChildren().toList().get(0).getElement().getProperty("checked"))
                .isEqualTo("true");
        assertThat(radioButtons.getValue()).hasValue(true);
        assertThat(modelObject.getValue()).isTrue();

        KaribuUtils.Fields.setValue(radioButtons, null);

        assertThat(radioButtons.getValue()).isEmpty();
        assertThat(modelObject.getValue()).isNull();
        assertThat(radioButtons.getChildren().toList().get(2).getElement().getProperty("checked"))
                .isEqualTo("true");
    }

    @Test
    void testBooleanRadioButtons_InclNull_PrimitiveBoolean() {
        var modelObject = new TestModelObjectWithPrimitiveBoolean();
        var pmo = new BooleanRadioButtonsTestPmo(modelObject);
        var bindingContext = new BindingContext();
        var radioButtons = getRadioButtonsComponentAt(0, pmo, bindingContext);

        assertThat(radioButtons.getListDataView().getItems().toList())
                .containsExactly(Optional.of(true), Optional.of(false));

        KaribuUtils.Fields.setValue(radioButtons, Optional.of(true));

        assertThat(radioButtons.getValue()).isEqualTo(Optional.of(true));
        assertThat(modelObject.getValue()).isTrue();

        KaribuUtils.Fields.setValue(radioButtons, Optional.of(false));

        assertThat(radioButtons.getValue()).isEqualTo(Optional.of(false));
        assertThat(modelObject.getValue()).isFalse();
    }

    @Test
    void testBooleanRadioButtons_ExclNull_ObjectBoolean() {
        var modelObject = new TestModelObjectWithObjectBoolean();
        var pmo = new BooleanRadioButtonsTestPmo(modelObject);
        var bindingContext = new BindingContext();
        var radioButtons = getRadioButtonsComponentAt(1, pmo, bindingContext);

        assertThat(radioButtons.getListDataView().getItems().toList())
                .containsExactly(Optional.of(true), Optional.of(false));
    }

    @Test
    void testBooleanRadioButtons_ExclNull_PrimitiveBoolean() {
        var modelObject = new TestModelObjectWithPrimitiveBoolean();
        var pmo = new BooleanRadioButtonsTestPmo(modelObject);
        var bindingContext = new BindingContext();
        var radioButtons = getRadioButtonsComponentAt(1, pmo, bindingContext);

        assertThat(radioButtons.getListDataView().getItems().toList())
                .containsExactly(Optional.of(true), Optional.of(false));
    }

    @SuppressWarnings("unchecked")
    private static RadioButtonGroup<Optional<Boolean>> getRadioButtonsComponentAt(int position,
                                                                                  BooleanRadioButtonsTestPmo pmo,
                                                                                  BindingContext bindingContext) {
        return (RadioButtonGroup<Optional<Boolean>>) UiCreator
                .<RadioButtonGroup<Boolean>, NoLabelComponentWrapper>createUiElements(pmo, bindingContext,
                        NoLabelComponentWrapper::new)
                .map(VaadinComponentWrapper::getComponent)
                .toList()
                .get(position);
    }

    @UISection
    static class BooleanRadioButtonsTestPmo {

        @ModelObject
        private final Object modelObject;

        public BooleanRadioButtonsTestPmo(Object modelObject) {
            this.modelObject = modelObject;
        }

        @UIRadioButtons(position = 0, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = "value")
        public void inclNull() {
            // model binding
        }

        @UIRadioButtons(position = 1, content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, modelAttribute = "value")
        public void noNull() {
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
