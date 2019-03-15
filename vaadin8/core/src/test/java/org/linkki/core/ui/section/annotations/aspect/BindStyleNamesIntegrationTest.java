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

package org.linkki.core.ui.section.annotations.aspect;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.ui.UiElementCreator;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.core.ui.section.annotations.BindStyleNames;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.section.annotations.aspect.BindStyleAnnotationAspectDefinition.StyleType;

import com.vaadin.ui.Component;

public class BindStyleNamesIntegrationTest {

    @Test
    public void testCreateAspect_static_single() {
        BindingContext bindingContext = new BindingContext();
        List<LabelComponentWrapper> uiElements = UiElementCreator
                .createUiElements(new TestPmoWithStaticStyleName(),
                                  bindingContext, c -> new LabelComponentWrapper((Component)c))
                .collect(Collectors.toList());

        assertThat(uiElements.get(0).getComponent().getStyleName(), is(TestPmoWithStaticStyleName.STYLE_NAME));
    }

    @Test
    public void testCreateAspect_static_multiple() {
        BindingContext bindingContext = new BindingContext();
        List<LabelComponentWrapper> uiElements = UiElementCreator
                .createUiElements(new TestPmoWithStaticStyleNames(),
                                  bindingContext, c -> new LabelComponentWrapper((Component)c))
                .collect(Collectors.toList());

        assertThat(uiElements.get(0).getComponent().getStyleName(),
                   is(String.join(" ", TestPmoWithStaticStyleNames.STYLE_NAME_1,
                                  TestPmoWithStaticStyleNames.STYLE_NAME_2)));
    }

    @Test
    public void testCreateAspect_dynamic_single() {
        BindingContext bindingContext = new BindingContext();
        TestPmoWithDynamicStyleName pmo = new TestPmoWithDynamicStyleName("style");
        List<LabelComponentWrapper> uiElements = UiElementCreator
                .createUiElements(pmo, bindingContext, c -> new LabelComponentWrapper((Component)c))
                .collect(Collectors.toList());
        assertThat(uiElements.get(0).getComponent().getStyleName(), is("style"));

        pmo.setStyleName("new-style");
        bindingContext.modelChanged();
        assertThat(uiElements.get(0).getComponent().getStyleName(), is("new-style"));
    }

    @Test
    public void testCreateAspect_dynamic_multiple() {
        BindingContext bindingContext = new BindingContext();
        TestPmoWithDynamicStyleNames pmo = new TestPmoWithDynamicStyleNames("style1", "style2");
        List<LabelComponentWrapper> uiElements = UiElementCreator
                .createUiElements(pmo, bindingContext, c -> new LabelComponentWrapper((Component)c))
                .collect(Collectors.toList());
        assertThat(uiElements.get(0).getComponent().getStyleName(), is("style1 style2"));

        pmo.setStyleNames("new-style1", "new-style2");
        bindingContext.modelChanged();
        assertThat(uiElements.get(0).getComponent().getStyleName(), is("new-style1 new-style2"));
    }

    @Test(expected = LinkkiBindingException.class)
    public void testCreateAspect_dynamic_methodMissing() {
        BindingContext bindingContext = new BindingContext();
        TestPmoMissingDynamicStyleNamesMethod pmo = new TestPmoMissingDynamicStyleNamesMethod();
        UiElementCreator.createUiElements(pmo, bindingContext, c -> new LabelComponentWrapper((Component)c))
                .collect(Collectors.toList());
    }

    public static class TestPmoWithStaticStyleName {

        public static final String STYLE_NAME = "sample";

        @BindStyleNames(STYLE_NAME)
        @UITextField(label = "static style name", position = 0)
        public String getPropertyWithStaticStyleName() {
            return "";
        }
    }

    public static class TestPmoWithStaticStyleNames {

        public static final String STYLE_NAME_1 = "sample1";
        public static final String STYLE_NAME_2 = "sample2";

        @BindStyleNames({ STYLE_NAME_1, STYLE_NAME_2 })
        @UITextField(label = "static style names", position = 0)
        public String getPropertyWithStaticStyleNames() {
            return "";
        }
    }

    public static class TestPmoWithDynamicStyleName {

        private String styleName;

        public TestPmoWithDynamicStyleName(String styleName) {
            this.styleName = styleName;
        }

        public void setStyleName(String styleName) {
            this.styleName = styleName;
        }

        @BindStyleNames(type = StyleType.DYNAMIC)
        @UITextField(label = "dynamic style names", position = 0)
        public String getProperty() {
            return "";
        }

        public String getPropertyStyleNames() {
            return styleName;
        }
    }

    public static class TestPmoWithDynamicStyleNames {

        private String[] styleNames;

        public TestPmoWithDynamicStyleNames(String... styleNames) {
            this.styleNames = styleNames;
        }

        public void setStyleNames(String... styleNames) {
            this.styleNames = styleNames;
        }

        @BindStyleNames(type = StyleType.DYNAMIC)
        @UITextField(label = "dynamic style names", position = 0)
        public String getProperty() {
            return "";
        }

        public List<String> getPropertyStyleNames() {
            return Arrays.asList(styleNames);
        }
    }

    public static class TestPmoMissingDynamicStyleNamesMethod {

        @BindStyleNames(type = StyleType.DYNAMIC)
        @UITextField(label = "missing dynamic style names method", position = 0)
        public String getProperty() {
            return "";
        }

        public String[] getPropertyStyleNames() {
            return new String[] {};
        }
    }
}
