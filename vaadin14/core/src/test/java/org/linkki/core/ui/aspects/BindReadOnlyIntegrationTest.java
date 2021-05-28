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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;

public class BindReadOnlyIntegrationTest {

    @Test
    public void testAspectOverridesDefault_Writable() {
        BindingContext bindingContext = new BindingContext();
        @SuppressWarnings("unchecked")
        Map<String, HasValue<?, String>> uiElements = UiCreator
                .createUiElements(new TestPmoWithReadOnlyFields(false),
                                  bindingContext,
                                  c -> new NoLabelComponentWrapper((Component)c))
                .map(e -> (HasValue<?, String>)e.getComponent())
                .collect(Collectors.toMap(HasValue::getValue, Function.identity()));

        assertThat(uiElements.get("onlyGetter").isReadOnly(), is(true));
        assertThat(uiElements.get("getterAndSetter").isReadOnly(), is(false));
        assertThat(uiElements.get("getterAndAlwaysReadOnlyAspect").isReadOnly(), is(true));
        assertThat(uiElements.get("getterAndSetterAndAlwaysReadOnlyAspect").isReadOnly(), is(true));
        assertThat(uiElements.get("getterAndSetterAndDynamicReadOnlyAspect").isReadOnly(), is(false));
    }

    @Test
    public void testAspectOverridesDefault_ReadOnly() {
        BindingContext bindingContext = new BindingContext();
        @SuppressWarnings("unchecked")
        Map<String, HasValue<?, String>> uiElements = UiCreator
                .createUiElements(new TestPmoWithReadOnlyFields(true),
                                  bindingContext,
                                  c -> new NoLabelComponentWrapper((Component)c))
                .map(e -> (HasValue<?, String>)e.getComponent())
                .collect(Collectors.toMap(HasValue::getValue, Function.identity()));

        assertThat(uiElements.get("onlyGetter").isReadOnly(), is(true));
        assertThat(uiElements.get("getterAndSetter").isReadOnly(), is(false));
        assertThat(uiElements.get("getterAndAlwaysReadOnlyAspect").isReadOnly(), is(true));
        assertThat(uiElements.get("getterAndSetterAndAlwaysReadOnlyAspect").isReadOnly(), is(true));
        assertThat(uiElements.get("getterAndSetterAndDynamicReadOnlyAspect").isReadOnly(), is(true));
    }


    public static class TestPmoWithReadOnlyFields {

        private final boolean readOnly;

        public TestPmoWithReadOnlyFields(boolean readOnly) {
            this.readOnly = readOnly;
        }

        @UITextField(position = 10)
        public String getOnlyGetter() {
            return "onlyGetter";
        }

        @UITextField(position = 20)
        public String getGetterAndSetter() {
            return "getterAndSetter";
        }

        public void setGetterAndSetter(@SuppressWarnings("unused") String ignored) {
            // nope
        }

        @BindReadOnly
        @UITextField(position = 30)
        public String getGetterAndAlwaysReadOnlyAspect() {
            return "getterAndAlwaysReadOnlyAspect";
        }

        @BindReadOnly
        @UITextField(position = 40)
        public String getGetterAndSetterAndAlwaysReadOnlyAspect() {
            return "getterAndSetterAndAlwaysReadOnlyAspect";
        }

        public void setGetterAndSetterAndAlwaysReadOnlyAspect(@SuppressWarnings("unused") String ignored) {
            // nope
        }

        @BindReadOnly(ReadOnlyType.DYNAMIC)
        @UITextField(position = 50)
        public String getGetterAndSetterAndDynamicReadOnlyAspect() {
            return "getterAndSetterAndDynamicReadOnlyAspect";
        }

        public void setGetterAndSetterAndDynamicReadOnlyAspect(@SuppressWarnings("unused") String ignored) {
            // nope
        }

        public boolean isGetterAndSetterAndDynamicReadOnlyAspectReadOnly() {
            return readOnly;
        }

    }
}
