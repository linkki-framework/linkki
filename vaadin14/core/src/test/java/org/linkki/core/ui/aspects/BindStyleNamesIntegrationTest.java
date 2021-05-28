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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.vaadin.component.section.AbstractSection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;

public class BindStyleNamesIntegrationTest {

    private static final String MY_STYLE = "blablablub";

    @Test
    public void testCreateAspect_static_single() {
        BindingContext bindingContext = new BindingContext();
        List<NoLabelComponentWrapper> uiElements = UiCreator
                .createUiElements(new TestPmoWithStaticStyleName(),
                                  bindingContext,
                                  c -> new NoLabelComponentWrapper((Component)c))
                .collect(Collectors.toList());

        assertThat(((HasStyle)uiElements.get(0).getComponent()).getClassName(),
                   is(TestPmoWithStaticStyleName.STYLE_NAME));
    }

    @Test
    public void testCreateAspect_static_multiple() {
        BindingContext bindingContext = new BindingContext();
        List<NoLabelComponentWrapper> uiElements = UiCreator
                .createUiElements(new TestPmoWithStaticStyleNames(),
                                  bindingContext,
                                  c -> new NoLabelComponentWrapper((Component)c))
                .collect(Collectors.toList());

        assertThat(((HasStyle)uiElements.get(0).getComponent()).getClassName(),
                   is(String.join(" ", TestPmoWithStaticStyleNames.STYLE_NAME_1,
                                  TestPmoWithStaticStyleNames.STYLE_NAME_2)));
    }

    @Test
    public void testCreateAspect_KeepPreviouslySpecifiedStaticStyles() {
        BindingContext bindingContext = new BindingContext();
        List<NoLabelComponentWrapper> uiElements = UiCreator
                .createUiElements(new TestPmoWithStaticStyleNames(),
                                  bindingContext, c -> {
                                      ((HasStyle)c).setClassName(MY_STYLE);
                                      return new NoLabelComponentWrapper((Component)c);
                                  })
                .collect(Collectors.toList());
        bindingContext.modelChanged();

        assertThat(((HasStyle)uiElements.get(0).getComponent()).getClassName(),
                   is(String.join(" ", MY_STYLE, TestPmoWithStaticStyleNames.STYLE_NAME_1,
                                  TestPmoWithStaticStyleNames.STYLE_NAME_2)));
    }

    @Test
    public void testCreateAspect_dynamic_single() {
        BindingContext bindingContext = new BindingContext();
        TestPmoWithDynamicStyleName pmo = new TestPmoWithDynamicStyleName("style");
        List<NoLabelComponentWrapper> uiElements = UiCreator
                .createUiElements(pmo, bindingContext,
                                  c -> new NoLabelComponentWrapper((Component)c))
                .collect(Collectors.toList());
        assertThat(((HasStyle)uiElements.get(0).getComponent()).getClassName(), is("style"));

        pmo.setClassName("new-style");
        bindingContext.modelChanged();
        assertThat(((HasStyle)uiElements.get(0).getComponent()).getClassName(), is("new-style"));
    }

    @Test
    public void testCreateAspect_dynamic_multiple() {
        BindingContext bindingContext = new BindingContext();
        TestPmoWithDynamicStyleNames pmo = new TestPmoWithDynamicStyleNames("style1", "style2");
        List<NoLabelComponentWrapper> uiElements = UiCreator
                .createUiElements(pmo, bindingContext,
                                  c -> new NoLabelComponentWrapper((Component)c))
                .collect(Collectors.toList());
        assertThat(((HasStyle)uiElements.get(0).getComponent()).getClassName(), is("style1 style2"));

        pmo.setClassNames("new-style1", "new-style2");
        bindingContext.modelChanged();
        assertThat(((HasStyle)uiElements.get(0).getComponent()).getClassName(), is("new-style1 new-style2"));
    }

    @Test
    public void testCreateAspect_KeepPreviouslySpecifiedDynamicSingleStyle() {
        BindingContext bindingContext = new BindingContext();
        TestPmoWithDynamicStyleName pmo = new TestPmoWithDynamicStyleName("style1");
        List<NoLabelComponentWrapper> uiElements = UiCreator
                .createUiElements(pmo, bindingContext, c -> {
                    ((HasStyle)c).setClassName(MY_STYLE);
                    return new NoLabelComponentWrapper((Component)c);
                })
                .collect(Collectors.toList());

        pmo.setClassName("new-style1");
        bindingContext.modelChanged();

        assertThat(((HasStyle)uiElements.get(0).getComponent()).getClassName(), is(MY_STYLE + " new-style1"));
    }

    @Test
    public void testCreateAspect_KeepPreviouslySpecifiedDynamicStyles() {
        BindingContext bindingContext = new BindingContext();
        TestPmoWithDynamicStyleNames pmo = new TestPmoWithDynamicStyleNames("style1", "style2");
        List<NoLabelComponentWrapper> uiElements = UiCreator
                .createUiElements(pmo, bindingContext, c -> {
                    ((HasStyle)c).setClassName(MY_STYLE);
                    return new NoLabelComponentWrapper((Component)c);
                })
                .collect(Collectors.toList());

        pmo.setClassNames("new-style1", "new-style2");
        bindingContext.modelChanged();

        assertThat(((HasStyle)uiElements.get(0).getComponent()).getClassName(),
                   is(MY_STYLE + " new-style1 new-style2"));
    }

    @Test
    public void testCreateAspect_dynamic_methodMissing() {
        BindingContext bindingContext = new BindingContext();
        TestPmoMissingDynamicStyleNamesMethod pmo = new TestPmoMissingDynamicStyleNamesMethod();

        Assertions.assertThrows(LinkkiBindingException.class, () -> {
            UiCreator
                    .createUiElements(pmo, bindingContext, c -> new NoLabelComponentWrapper((Component)c))
                    .collect(Collectors.toList());
        });

    }

    @Test
    public void testSection_static() {
        AbstractSection section = PmoBasedSectionFactory
                .createAndBindSection(new TestSectionPmoWithStaticClassStyleNames(),
                                      new BindingContext());
        assertThat(section.getClassName(), containsString(TestSectionPmoWithStaticClassStyleNames.STYLE_1));
        assertThat(section.getClassName(), containsString(TestSectionPmoWithStaticClassStyleNames.STYLE_2));
    }

    @Test
    public void testSection_dynamic() {
        TestSectionPmoWithDynamicClassStyleNames pmo = new TestSectionPmoWithDynamicClassStyleNames();
        BindingContext bindingContext = new BindingContext();
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(pmo, bindingContext);

        pmo.setStyleNames(Arrays.asList("gucci", "versace"));
        bindingContext.modelChanged();
        assertThat(section.getClassName(), containsString("gucci"));
        assertThat(section.getClassName(), containsString("versace"));

        pmo.setStyleNames(Arrays.asList("chanel"));
        bindingContext.modelChanged();
        String styleNamesAfterUpdate = section.getClassName();
        assertThat(styleNamesAfterUpdate, not(containsString("gucci")));
        assertThat(styleNamesAfterUpdate, not(containsString("versace")));
        assertThat(styleNamesAfterUpdate, containsString("chanel"));
    }

    @Test
    public void testSection_dynamic_inherited() {
        TestChildSectionPmoWithDynamicClassStyleNames pmo = new TestChildSectionPmoWithDynamicClassStyleNames();
        BindingContext bindingContext = new BindingContext();
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(pmo, bindingContext);

        pmo.setStyleNames(Arrays.asList("ml"));
        bindingContext.modelChanged();
        assertThat(section.getClassName(), containsString("ml"));

        pmo.setStyleNames(Arrays.asList("lisp"));
        bindingContext.modelChanged();
        String styleNamesAfterUpdate = section.getClassName();
        assertThat(styleNamesAfterUpdate, not(containsString("ml")));
        assertThat(styleNamesAfterUpdate, containsString("lisp"));
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

        public void setClassName(String styleName) {
            this.styleName = styleName;
        }

        @BindStyleNames
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

        public void setClassNames(String... styleNames) {
            this.styleNames = styleNames;
        }

        @BindStyleNames
        @UITextField(label = "dynamic style names", position = 0)
        public String getProperty() {
            return "";
        }

        public List<String> getPropertyStyleNames() {
            return Arrays.asList(styleNames);
        }
    }

    public static class TestPmoMissingDynamicStyleNamesMethod {

        @BindStyleNames
        @UITextField(label = "missing dynamic style names method", position = 0)
        public String getProperty() {
            return "";
        }

        public String[] getPropertyStyleNames() {
            return new String[] {};
        }
    }

    @UISection
    @BindStyleNames
    public static class TestSectionPmoWithDynamicClassStyleNames {

        private List<String> styleNames = new ArrayList<>();

        public List<String> getStyleNames() {
            return styleNames;
        }

        public void setStyleNames(List<String> styleNames) {
            this.styleNames = styleNames;
        }
    }

    @UISection
    @BindStyleNames({ TestSectionPmoWithStaticClassStyleNames.STYLE_1,
            TestSectionPmoWithStaticClassStyleNames.STYLE_2 })
    public static class TestSectionPmoWithStaticClassStyleNames {
        public static final String STYLE_1 = "gucci";
        public static final String STYLE_2 = "versace";
    }

    @UISection
    public static class TestChildSectionPmoWithDynamicClassStyleNames extends TestSectionPmoWithDynamicClassStyleNames {
        // nothing to do as everything should be inherited
    }
}
