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
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.section.AbstractSection;
import org.linkki.core.vaadin.component.section.CustomLayoutSection;
import org.linkki.core.vaadin.component.section.HorizontalSection;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;

public class PmoBasedSectionFactoryTest {

    private BindingContext bindingContext = new BindingContext("testBindingContext");

    @Test
    public void testSetSectionId() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SCCPmoWithID(), bindingContext);
        assertThat(section.getId(), is("test-ID"));
    }

    @Test
    public void testSetSectionDefaultId() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SCCPmoWithoutID(), bindingContext);
        assertThat(section.getId(), is("SCCPmoWithoutID"));
    }

    @Test
    public void testSetComponentId() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SCCPmoWithID(), bindingContext);
        @SuppressWarnings("deprecation")
        GridLayout gridLayout = TestUiUtil
                .getContentGrid((org.linkki.core.vaadin.component.section.FormSection)section);
        Component textField = gridLayout.getComponent(1, 0);

        assertThat(textField.getId(), is("testProperty"));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testSectionWithDefaultLayout_shouldCreateFormSection() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SCCPmoWithoutID(), bindingContext);
        assertThat(section, is(instanceOf(org.linkki.core.vaadin.component.section.FormSection.class)));
    }

    @Test
    public void testSectionWithHorizontalLayout_shouldCreateHorizontalSection() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SectionWithHorizontalLayout(),
                                                                              bindingContext);
        assertThat(section, is(instanceOf(HorizontalSection.class)));
    }

    @Test
    public void testSectionWithCustomLayout_shouldCreateCustomLayoutSection() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SectionWithCustomLayout(),
                                                                              bindingContext);
        assertThat(section, is(instanceOf(CustomLayoutSection.class)));
    }


    @SuppressWarnings("deprecation")
    @Test
    public void testSectionWithoutAnnotation_usesDefaultValues() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SectionWithoutAnnotation(),
                                                                              bindingContext);
        assertThat(section, is(instanceOf(org.linkki.core.vaadin.component.section.FormSection.class)));
        assertThat(section.getId(), is(SectionWithoutAnnotation.class.getSimpleName()));
        assertThat(section.getCaption(), is(nullValue()));
        assertThat(((org.linkki.core.vaadin.component.section.FormSection)section).getNumberOfColumns(), is(1));
    }

    @Test
    public void testCreateSectionHeader() {
        SCCPmoWithID containerPmo = new SCCPmoWithID();
        PmoBasedSectionFactory factory = new PmoBasedSectionFactory();

        AbstractSection tableSection = factory.createSection(containerPmo, bindingContext);
        HorizontalLayout header = (HorizontalLayout)tableSection.getComponent(0);

        assertThat(header.getComponent(2), instanceOf(Button.class));
        assertThat(header.getComponent(2).getCaption(), is("header button"));
    }

    @UISection(caption = "Test")
    public static class SCCPmoWithID {

        @SectionID
        public String getId() {
            return "test-ID";
        }

        @UITextField(position = 0, label = "")
        public String getTestProperty() {
            return "some_value";
        }

        @SectionHeader
        @UIButton(position = 10, caption = "header button")
        public void click() {
            // nothing to do
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
