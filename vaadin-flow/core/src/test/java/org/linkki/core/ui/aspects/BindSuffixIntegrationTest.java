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

package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.annotation.BindSuffix;
import org.linkki.core.ui.aspects.types.SuffixType;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;

class BindSuffixIntegrationTest {

    private final TestWithBindSuffixFieldsPmo pmo = new TestWithBindSuffixFieldsPmo();
    private final BindingContext bindingContext = new BindingContext();
    private final Map<String, Component> uiElements = UiCreator
            .createUiElements(pmo,
                              bindingContext,
                              c -> new NoLabelComponentWrapper((Component)c))
            .map(c -> c.getComponent())
            .collect(Collectors.toMap(k -> k.getId().get(), v -> v));

    @Test
    public void testAspectBindSuffixAnnotation_NotBound() {
        Component component = uiElements.get("notBoundSuffixField");

        String suffixResult = component.getElement().getTextRecursively();

        assertThat(suffixResult, is(""));
    }

    @Test
    public void testAspectBindSuffixAnnotation_Static() {
        Component component = uiElements.get("suffixFieldStatic");

        String suffixResult = component.getElement().getTextRecursively();

        assertThat(suffixResult, is("§"));
    }

    @Test
    public void testAspectBindSuffixAnnotation_DynamicWithValue() {
        Component component = uiElements.get("suffixFieldDynamic");

        pmo.setSuffix("€");
        bindingContext.modelChanged();
        String suffixResult = component.getElement().getTextRecursively();

        assertThat(suffixResult, is("€"));
    }

    @Test
    public void testAspectBindSuffixAnnotation_DynamicEmptyValue() {
        Component component = uiElements.get("suffixFieldDynamic");

        pmo.setSuffix("");
        bindingContext.modelChanged();
        String suffixResult = component.getElement().getTextRecursively();

        assertThat(suffixResult, is(""));
    }

    @Test
    public void testAspectBindSuffixAnnotation_AutoWithValue() {
        Component component = uiElements.get("suffixFieldAutoWithValue");

        String suffixResult = component.getElement().getTextRecursively();

        assertThat(suffixResult, is("€"));
    }

    @Test
    public void testAspectBindSuffixAnnotation_AutoEmptyValue() {
        Component component = uiElements.get("suffixFieldAutoEmptyValue");

        String suffixResult = component.getElement().getTextRecursively();

        assertThat(suffixResult, is("&"));
    }

    public static class TestWithBindSuffixFieldsPmo {

        private String suffix = "!";

        public TestWithBindSuffixFieldsPmo() {

        }

        public TestWithBindSuffixFieldsPmo(String suffix) {
            this.suffix = suffix;
        }

        @UITextField(position = 10)
        public String getNotBoundSuffixField() {
            return "NotBoundSuffixField";
        }

        @BindSuffix(value = "§", suffixType = SuffixType.STATIC)
        @UITextField(position = 20)
        public String getSuffixFieldStatic() {
            return "SuffixFieldStatic";
        }

        @BindSuffix(suffixType = SuffixType.DYNAMIC)
        @UITextField(position = 50)
        public String getSuffixFieldDynamic() {
            return "getSuffixFieldWithAttribute";
        }

        public String getSuffixFieldDynamicSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        @BindSuffix(value = "€")
        @UITextField(position = 30)
        public String getSuffixFieldAutoWithValue() {
            return "getSuffixFieldAuto";
        }

        public String getSuffixFieldAutoWithValueSuffix() {
            return "$";
        }

        @BindSuffix
        @UITextField(position = 40)
        public String getSuffixFieldAutoEmptyValue() {
            return "getSuffixFieldEmptyValueAuto";
        }

        public String getSuffixFieldAutoEmptyValueSuffix() {
            return "&";
        }

    }
}
