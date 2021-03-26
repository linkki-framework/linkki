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
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.Binder;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.bind.annotation.Bind;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.wrapper.FormItemComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;

public class BindVisibleIntegrationTest {

    @Test
    public void testAspectBindVisibleAnnotation_default() {
        BindingContext bindingContext = new BindingContext();
        Map<String, Boolean> uiElements = UiCreator
                .createUiElements(new TestWithBindVisibleFieldsPmo(),
                                  bindingContext, c -> new FormItemComponentWrapper((Component)c))
                .map(c -> c.getComponent())
                .collect(Collectors.toMap(k -> k.getId().get(), v -> v.isVisible()));

        assertThat(uiElements.get("alwaysVisibleField"), is(true));
        assertThat(uiElements.get("invisibleField"), is(false));
        assertThat(uiElements.get("visibleField"), is(true));
        assertThat(uiElements.get("visibleFieldWithAttribute"), is(true));
    }

    @Test
    public void testAspectBindVisibleAnnotation_visibleFieldWithAttributeOverride() {
        BindingContext bindingContext = new BindingContext();
        Map<String, Boolean> uiElements = UiCreator
                .createUiElements(new TestWithBindVisibleFieldsPmo(),
                                  bindingContext, c -> new FormItemComponentWrapper((Component)c))
                .map(c -> c.getComponent())
                .collect(Collectors.toMap(k -> k.getId().get(), v -> v.isVisible()));

        assertThat(uiElements.get("visibleFieldWithAttribute"), is(true));

        uiElements = UiCreator
                .createUiElements(new TestWithBindVisibleFieldsPmo(false),
                                  bindingContext, c -> new FormItemComponentWrapper((Component)c))
                .map(c -> c.getComponent())
                .collect(Collectors.toMap(k -> k.getId().get(), v -> v.isVisible()));

        assertThat(uiElements.get("visibleFieldWithAttribute"), is(false));
    }

    @Test
    public void testBindVisible_fieldBinding() {
        BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
        BindingContext bindingContext = bindingManager.getContext("");

        TestView view = new TestView();
        TestWithBindVisibleFieldsPmo pmo = new TestWithBindVisibleFieldsPmo();

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingContext);

        assertThat(view.testFieldBindingButton.isVisible(), is(true));
        pmo.setTestFieldBindingButton(false);
        bindingContext.modelChanged();
        assertThat(view.testFieldBindingButton.isVisible(), is(false));
    }

    public static class TestWithBindVisibleFieldsPmo {

        public static final String BUTTON_BINDING = "testFieldBindingButton";
        private boolean testFieldBindingButtonVisible = true;

        private boolean customVisibility = true;

        public TestWithBindVisibleFieldsPmo() {
            this(true);
        }

        public TestWithBindVisibleFieldsPmo(boolean customVisibility) {
            this.customVisibility = customVisibility;
        }

        @UITextField(position = 10)
        public String getAlwaysVisibleField() {
            return "getAlwaysVisibleField";
        }

        @BindVisible
        @UITextField(position = 20)
        public String getInvisibleField() {
            return "getInvisibleField";
        }

        public boolean isInvisibleFieldVisible() {
            return false;
        }

        @BindVisible
        @UITextField(position = 30)
        public String getVisibleField() {
            return "getVisibleField";
        }

        public boolean isVisibleFieldVisible() {
            return true;
        }

        @BindVisible
        @UITextField(position = 40, visible = VisibleType.INVISIBLE)
        public String getVisibleFieldWithAttribute() {
            return "getVisibleFieldWithAttribute";
        }

        public boolean isVisibleFieldWithAttributeVisible() {
            return this.customVisibility;
        }

        public boolean isTestFieldBindingButtonVisible() {
            return testFieldBindingButtonVisible;
        }

        public void setTestFieldBindingButton(boolean visible) {
            this.testFieldBindingButtonVisible = visible;
        }
    }

    public static class TestView {

        @Bind(pmoProperty = TestWithBindVisibleFieldsPmo.BUTTON_BINDING)
        @BindVisible
        private final Button testFieldBindingButton = new Button();

    }

}
