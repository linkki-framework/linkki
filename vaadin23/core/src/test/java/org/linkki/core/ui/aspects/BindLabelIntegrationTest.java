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

package org.linkki.core.ui.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.ui.aspects.types.LabelType;
import org.linkki.core.ui.aspects.annotation.BindLabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

class BindLabelIntegrationTest {

    private final BindingContext bindingContext = new BindingContext();

    @Test
    void testAspectBindIconAnnotation_Static_withButton() {
        List<Component> uiElements = UiCreator
                .createUiElements(new TestPmo(), bindingContext,
                                  c -> new LabelComponentWrapper((Component)c, WrapperType.COMPONENT))
                .map(VaadinComponentWrapper::getComponent).collect(Collectors.toList());

        TextField textFieldStatic = (TextField)uiElements.get(0);
        assertThat(textFieldStatic.getLabel()).isEqualTo("This is my static label");

        TextField textFieldDynamic = (TextField)uiElements.get(1);
        assertThat(textFieldDynamic.getLabel()).isEqualTo("This is my dynamic label");

        TextField textFieldAuto = (TextField)uiElements.get(2);
        assertThat(textFieldAuto.getLabel()).isEqualTo("This is my auto label");

        TextField textFieldAuto2 = (TextField)uiElements.get(2);
        assertThat(textFieldAuto2.getLabel()).isEqualTo("This is my auto label");
    }

    private static class TestPmo {

        @BindLabel(value = "This is my static label", labelType = LabelType.STATIC)
        @UITextField(label = "Should not be used as label", position = 0)
        public String getPropertyWithStaticLabel() {
            return "";
        }

        @BindLabel(value = "Should not be used as label", labelType = LabelType.DYNAMIC)
        @UITextField(label = "Should not be used as label", position = 1)
        public String getPropertyWithDynamicLabel() {
            return "";
        }

        @SuppressWarnings("unused")
        public String getPropertyWithDynamicLabelLabel() {
            return "This is my dynamic label";
        }

        @BindLabel(labelType = LabelType.AUTO)
        @UITextField(label = "Should not be used as label", position = 2)
        public String getPropertyWithAutoLabel() {
            return "";
        }

        @SuppressWarnings("unused")
        public String getPropertyWithAutoLabelLabel() {
            return "This is my auto label";
        }

        @BindLabel(value = "This is my auto label", labelType = LabelType.AUTO)
        @UITextField(label = "Should not be used as label", position = 3)
        public String getPropertyWithAutoLabel2() {
            return "";
        }


    }

}
