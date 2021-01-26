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
package org.linkki.core.ui.creation.section;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.pmo.SectionID;
import org.linkki.core.ui.element.annotation.TestUiUtil;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.section.BaseSection;
import org.linkki.core.vaadin.component.section.CustomLayoutSection;
import org.linkki.core.vaadin.component.section.HorizontalSection;

import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;

import edu.umd.cs.findbugs.annotations.NonNull;

@Deprecated
public class SectionCreationContextTest {

    private BindingContext bindingContext = new BindingContext("testBindingContext");

    @Test
    public void testSetSectionId() {
        BaseSection section = createContext(new SCCPmoWithID()).createSection();
        assertThat(section.getId(), is("test-ID"));
    }

    private SectionCreationContext createContext(Object pmo) {
        return new SectionCreationContext(pmo, bindingContext);
    }

    @Test
    public void testSetSectionDefaultId() {
        BaseSection section = createContext(new SCCPmoWithoutID()).createSection();
        assertThat(section.getId(), is("SCCPmoWithoutID"));
    }

    @Test
    public void testSetComponentId() {
        BaseSection section = createContext(new SCCPmoWithID()).createSection();
        assertThat(section.getComponentCount(), is(2));
        assertThat(section.getComponent(0).isVisible(), is(false)); // invisible header
        GridLayout gridLayout = TestUiUtil
                .getContentGrid((org.linkki.core.vaadin.component.section.FormSection)section);

        @NonNull
        Component textField = gridLayout.getComponent(1, 0);
        assertThat(textField.getId(), is("testProperty"));
    }

    @Test
    public void testSectionWithDefaultLayout_shouldCreateFormLayout() {
        BaseSection section = createContext(new SCCPmoWithoutID()).createSection();
        assertThat(section, is(instanceOf(org.linkki.core.vaadin.component.section.FormSection.class)));
    }

    @Test
    public void testSectionWithHorizontalLayout_shouldCreateHorizontalSection() {
        BaseSection section = createContext(new SectionWithHorizontalLayout()).createSection();
        assertThat(section, is(instanceOf(HorizontalSection.class)));
    }

    @Test
    public void testSectionWithCustomLayout_shouldCreateCustomLayoutSection() {
        BaseSection section = createContext(new SectionWithCustomLayout()).createSection();
        assertThat(section, is(instanceOf(CustomLayoutSection.class)));
    }


    @Test
    public void testSectionWithoutAnnotation_usesDefaultValues() {
        BaseSection section = createContext(new SectionWithoutAnnotation()).createSection();
        assertThat(section, is(instanceOf(org.linkki.core.vaadin.component.section.FormSection.class)));
        assertThat(section.getId(), is(SectionWithoutAnnotation.class.getSimpleName()));
        assertThat(section.getCaption(), is(nullValue()));
        assertThat(((org.linkki.core.vaadin.component.section.FormSection)section).getNumberOfColumns(), is(1));
    }

    @UISection
    public static class SCCPmoWithID {

        @SectionID
        public String getId() {
            return "test-ID";
        }

        @UITextField(position = 0, label = "")
        public String getTestProperty() {
            return "some_value";
        }
    }

    @UISection
    private static class SCCPmoWithoutID {
        // no content required
    }

    @UISection(layout = SectionLayout.HORIZONTAL)
    private static class SectionWithHorizontalLayout {
        // no content required
    }

    @UISection(layout = SectionLayout.CUSTOM)
    private static class SectionWithCustomLayout {
        // no content required
    }

    private static class SectionWithoutAnnotation {
        // no content required
    }
}
