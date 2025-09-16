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
import static org.linkki.core.ui.aspects.annotation.BindHelperTextIntegrationTest.TestPmo.AUTO_STATIC_HELPER_TEXT;
import static org.linkki.core.ui.aspects.annotation.BindHelperTextIntegrationTest.TestPmo.STATIC_HELPER_TEXT;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.types.HelperTextType;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

class BindHelperTextIntegrationTest {

    private final TestPmo pmo = new TestPmo();
    private final BindingContext bindingContext = new BindingContext();

    @Test
    void testAspectBindHelperText_Static() {
        var textfield = (TextField)getUiElements().get("helperTextWithStaticValue");

        var helperTextResult = textfield.getHelperText();

        assertThat(helperTextResult).isEqualTo(STATIC_HELPER_TEXT);
    }

    @Test
    void testAspectBindHelperText_Dynamic() {
        var textfield = (TextField)getUiElements().get("helperTextWithDynamicValue");

        var helperTextResult = textfield.getHelperText();

        assertThat(helperTextResult).isEqualTo("This is a dynamic helper text");
    }

    @Test
    void testAspectBindHelperText_AutoStatic() {
        var textfield = (TextField)getUiElements().get("helperTextWithAutoStaticValue");

        var helperTextResult = textfield.getHelperText();

        assertThat(helperTextResult).isEqualTo(AUTO_STATIC_HELPER_TEXT);
    }

    @Test
    void testAspectBindHelperText_AutoDynamic() {
        var textfield = (TextField)getUiElements().get("helperTextWithAutoDynamicValue");

        var helperTextResult = textfield.getHelperText();

        assertThat(helperTextResult).isEqualTo("This is an auto dynamic helper text");
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

        public static final String STATIC_HELPER_TEXT = "This is a static helper text";
        public static final String AUTO_STATIC_HELPER_TEXT = "This is an auto static helper text";

        @BindHelperText(value = STATIC_HELPER_TEXT, helperTextType = HelperTextType.STATIC)
        @UITextField(position = 0)
        public String getHelperTextWithStaticValue() {
            return "";
        }

        @BindHelperText(value = "This helper text will be ignored", helperTextType = HelperTextType.DYNAMIC)
        @UITextField(position = 10)
        public String getHelperTextWithDynamicValue() {
            return "";
        }

        public String getHelperTextWithDynamicValueHelperText() {
            return "This is a dynamic helper text";
        }

        @BindHelperText(AUTO_STATIC_HELPER_TEXT)
        @UITextField(position = 15)
        public String getHelperTextWithAutoStaticValue() {
            return "";
        }

        @BindHelperText
        @UITextField(position = 20)
        public String getHelperTextWithAutoDynamicValue() {
            return "";
        }

        public String getHelperTextWithAutoDynamicValueHelperText() {
            return "This is an auto dynamic helper text";
        }
    }
}
