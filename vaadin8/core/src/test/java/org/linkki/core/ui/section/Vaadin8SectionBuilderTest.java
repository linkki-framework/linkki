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
package org.linkki.core.ui.section;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.pmo.SectionID;
import org.linkki.core.ui.section.PmoBasedSectionFactory.SectionBuilder;
import org.linkki.core.ui.section.annotations.SectionLayout;
import org.linkki.core.ui.section.annotations.TestUiUtil;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;

import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;

import edu.umd.cs.findbugs.annotations.NonNull;

public class Vaadin8SectionBuilderTest {

    private BindingContext bindingContext = new BindingContext("testBindingContext");

    @Test
    public void testSetSectionId() {
        AbstractSection section = createSectionBuilder(new SCCPmoWithID()).createSection();
        assertThat(section.getId(), is("test-ID"));
    }

    private SectionBuilder createSectionBuilder(Object pmo) {
        return new SectionBuilder(pmo, bindingContext);
    }

    @Test
    public void testSetSectionDefaultId() {
        AbstractSection section = createSectionBuilder(new SCCPmoWithoutID()).createSection();
        assertThat(section.getId(), is("SCCPmoWithoutID"));
    }

    @Test
    public void testSetComponentId() {
        AbstractSection section = createSectionBuilder(new SCCPmoWithID()).createSection();
        assertThat(section.getComponentCount(), is(1));
        GridLayout gridLayout = TestUiUtil.getContentGrid((FormSection)section);
        
        @NonNull
        Component textField = gridLayout.getComponent(1, 0);
        assertThat(textField.getId(), is("testProperty"));
    }

    @Test
    public void testSectionWithDefaultLayout_shouldCreateFormLayout() {
        AbstractSection section = createSectionBuilder(new SCCPmoWithoutID()).createSection();
        assertThat(section, is(instanceOf(FormSection.class)));
    }

    @Test
    public void testSectionWithHorizontalLayout_shouldCreateHorizontalSection() {
        AbstractSection section = createSectionBuilder(new SectionWithHorizontalLayout()).createSection();
        assertThat(section, is(instanceOf(HorizontalSection.class)));
    }

    @Test
    public void testSectionWithCustomLayout_shouldCreateCustomLayoutSection() {
        AbstractSection section = createSectionBuilder(new SectionWithCustomLayout()).createSection();
        assertThat(section, is(instanceOf(CustomLayoutSection.class)));
    }

    
    @Test
    public void testSectionWithoutAnnotation_usesDefaultValues() {
        AbstractSection section = createSectionBuilder(new SectionWithoutAnnotation()).createSection();
        assertThat(section, is(instanceOf(FormSection.class)));
        assertThat(section.getId(), is(SectionWithoutAnnotation.class.getSimpleName()));
        assertThat(section.getCaption(), is(nullValue()));
        assertThat(((FormSection)section).getNumberOfColumns(), is(1));
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
