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
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;

class BindTooltipIntegrationTest {

    @Test
    void testCreateAspect_Static() {
        BindingContext bindingContext = new BindingContext();
        List<LabelComponentWrapper> uiElements = UiCreator
                .createUiElements(new TestPmoWithStaticTooltip(),
                                  bindingContext, this::getLabelComponentWrapper)
                .toList();

        assertThat(uiElements.get(0).getComponent().getElement().getAttribute("title"),
                   is(TestPmoWithStaticTooltip.TOOLTIP));
    }

    @Test
    void testCreateAspect_StaticEmpty() {
        BindingContext bindingContext = new BindingContext();
        List<LabelComponentWrapper> uiElements = UiCreator
                .createUiElements(new TestPmoWithStaticEmptyTooltip(),
                                  bindingContext, this::getLabelComponentWrapper)
                .toList();

        assertThat(uiElements.get(0).getComponent().getElement().getAttribute("title"), is(emptyString()));
    }

    @Test
    void testCreateAspect_Auto() {
        BindingContext bindingContext = new BindingContext();
        TestPmoWithAutoTooltip pmo = new TestPmoWithAutoTooltip("basic tooltip");
        List<LabelComponentWrapper> uiElements = UiCreator
                .createUiElements(pmo, bindingContext,
                                  c -> new LabelComponentWrapper((Component)c, WrapperType.COMPONENT))
                .toList();
        assertThat(uiElements.get(0).getComponent().getElement().getAttribute("title"), is("basic tooltip"));

        pmo.setTooltip(null);
        bindingContext.modelChanged();
        assertThat(uiElements.get(0).getComponent().getElement().getAttribute("title"), is(emptyString()));
    }

    @Test
    void testCreateAspect_Dynamic() {
        BindingContext bindingContext = new BindingContext();
        TestPmoWithDynamicTooltip pmo = new TestPmoWithDynamicTooltip("basic tooltip");
        List<LabelComponentWrapper> uiElements = UiCreator
                .createUiElements(pmo, bindingContext, this::getLabelComponentWrapper)
                .toList();
        assertThat(uiElements.get(0).getComponent().getElement().getAttribute("title"), is("basic tooltip"));

        pmo.setTooltip(null);
        bindingContext.modelChanged();
        assertThat(uiElements.get(0).getComponent().getElement().getAttribute("title"), is(emptyString()));
    }

    @Test
    void testCreateAspect_Dynamic_MethodMissing() {
        BindingContext bindingContext = new BindingContext();
        TestPmoMissingDynamicMethod pmo = new TestPmoMissingDynamicMethod();

        Assertions.assertThrows(LinkkiBindingException.class,
                                () -> UiCreator.createUiElements(pmo, bindingContext, this::getLabelComponentWrapper)
                                        .toList());

    }

    static class TestPmoWithStaticTooltip {

        public static final String TOOLTIP = "tooltip";

        @BindTooltip(TOOLTIP)
        @UITextField(label = "static tooltip", position = 0)
        public String getPropertyWithStaticTooltip() {
            return "";
        }
    }

    static class TestPmoWithStaticEmptyTooltip {

        @BindTooltip(value = StringUtils.EMPTY, tooltipType = TooltipType.STATIC)
        @UITextField(label = "static tooltip", position = 0)
        public String getPropertyWithStaticEmptyTooltip() {
            return "";
        }
    }

    static class TestPmoWithAutoTooltip {

        private String tooltip;

        public TestPmoWithAutoTooltip(String tooltip) {
            this.tooltip = tooltip;
        }

        public void setTooltip(String tooltip) {
            this.tooltip = tooltip;
        }

        @BindTooltip
        @UITextField(label = "dynamic tooltip", position = 0)
        public String getProperty() {
            return "";
        }

        public String getPropertyTooltip() {
            return tooltip;
        }
    }

    static class TestPmoWithDynamicTooltip {

        private String tooltip;

        public TestPmoWithDynamicTooltip(String tooltip) {
            this.tooltip = tooltip;
        }

        public void setTooltip(String tooltip) {
            this.tooltip = tooltip;
        }

        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UITextField(label = "dynamic tooltip", position = 0)
        public String getProperty() {
            return "";
        }

        public String getPropertyTooltip() {
            return tooltip;
        }
    }

    static class TestPmoMissingDynamicMethod {

        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UITextField(label = "missing dynamic method", position = 0)
        public String getProperty() {
            return "";
        }
    }

    private LabelComponentWrapper getLabelComponentWrapper(Component c) {
        return new LabelComponentWrapper(c, WrapperType.COMPONENT);
    }
}
