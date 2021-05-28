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
import static org.linkki.test.matcher.Matchers.hasValue;

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
import org.linkki.core.vaadin.component.section.FormLayoutSection;
import org.linkki.core.vaadin.component.section.HorizontalSection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class PmoBasedSectionFactoryTest {

    private final BindingContext bindingContext = new BindingContext("testBindingContext");

    @Test
    public void testSetSectionId() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SCCPmoWithID(), bindingContext);
        assertThat(section.getId(), hasValue("test-ID"));
    }

    @Test
    public void testSetSectionDefaultId() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SCCPmoWithoutID(), bindingContext);
        assertThat(section.getId(), hasValue("SCCPmoWithoutID"));
    }

    @Test
    public void testSetComponentId() {
        FormLayoutSection section = (FormLayoutSection)PmoBasedSectionFactory.createAndBindSection(new SCCPmoWithID(),
                                                                                                   bindingContext);
        Component textField = TestUiUtil.getComponentAtIndex(0, section.getSectionContent());

        assertThat(textField.getId(), hasValue("testProperty"));
    }

    @Test
    public void testSectionWithDefaultLayout_shouldCreateFormSection() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SCCPmoWithoutID(), bindingContext);
        assertThat(section, is(instanceOf(FormLayoutSection.class)));
    }

    @Test
    public void testSectionWithHorizontalLayout_shouldCreateHorizontalSection() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SectionWithHorizontalLayout(),
                                                                              bindingContext);
        assertThat(section, is(instanceOf(HorizontalSection.class)));
    }

    @Test
    public void testSectionWithoutAnnotation_usesDefaultValues() {
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(new SectionWithoutAnnotation(),
                                                                              bindingContext);
        assertThat(section, is(instanceOf(FormLayoutSection.class)));
        assertThat(section.getId(), hasValue(SectionWithoutAnnotation.class.getSimpleName()));
        assertThat(section.getCaption(), is(""));
    }

    @Test
    public void testCreateSectionHeader() {
        SCCPmoWithID containerPmo = new SCCPmoWithID();
        PmoBasedSectionFactory factory = new PmoBasedSectionFactory();

        AbstractSection tableSection = factory.createSection(containerPmo, bindingContext);
        HorizontalLayout header = (HorizontalLayout)TestUiUtil.getComponentAtIndex(0, tableSection);

        assertThat(header.getComponentAt(1), instanceOf(Button.class));
        assertThat(((Button)header.getComponentAt(1)).getText(), is("header button"));
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

    private static class SectionWithoutAnnotation {
        // no content required
    }
}
