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

package org.linkki.core.ui.aspects.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.HeightAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindHeight.BindHeightAspectDefinitionCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

class BindHeightIntegrationTest {
    private final TestPmo pmo = new TestPmo();
    private final BindingContext bindingContext = new BindingContext();

    @Test
    void testBindHeight_StaticValue() {
        var textField = (TextField)getUiElements().get("heightValue");
        var textField2 = (TextField)getUiElements().get("heightDefaultValue");

        var textFieldCustomHeight = textField.getHeight();
        var textFieldDefaultHeight = textField2.getHeight();

        assertThat(textFieldCustomHeight).isEqualTo("200px");
        assertThat(textFieldDefaultHeight).isEqualTo("");
    }

    @Test
    void testBindHeightAspectDefinitionCreator() {
        BindHeight annotation = new BindHeight() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return BindHeight.class;
            }

            @Override
            public String value() {
                return "100em";
            }
        };
        var aspectDefinitionCreator = new BindHeightAspectDefinitionCreator();

        var aspectDefinition = (HeightAspectDefinition)aspectDefinitionCreator.create(annotation);
        var aspect = aspectDefinition.createAspect();

        assertThat(aspect.getName()).isEqualTo(HeightAspectDefinition.NAME);
        assertThat(aspect.getValue()).isEqualTo("100em");
    }

    private Map<String, Component> getUiElements() {
        return UiCreator
                .createUiElements(pmo,
                                  bindingContext,
                                  c -> new NoLabelComponentWrapper((Component)c))
                .map(VaadinComponentWrapper::getComponent)
                .collect(Collectors.toMap(k -> k.getId().get(), v -> v));
    }

    static class TestPmo {
        private static final String VALUE = "200px";

        @BindHeight(VALUE)
        @UITextField(position = 0)
        public String getHeightValue() {
            return "";
        }

        @BindHeight
        @UITextField(position = 10)
        public String getHeightDefaultValue() {
            return "";
        }

    }
}
