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

package org.linkki.samples.playground.uitest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.dynamicannotations.DynamicAnnotationsLayout;
import org.linkki.samples.playground.dynamicannotations.DynamicCaptionWithCloseButtonPmo;
import org.linkki.samples.playground.dynamicannotations.DynamicCaptionWithEditButtonPmo;
import org.linkki.samples.playground.dynamicannotations.DynamicCaptionWithSectionHeaderButtonPmo;
import org.linkki.samples.playground.dynamicannotations.DynamicCaptionWithoutButtonPmo;

import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

public class BindCaptionTest extends AbstractUiTest {

    private <T> VerticalLayoutElement getSection(Class<T> cls) {
        return $(VerticalLayoutElement.class).id(cls.getSimpleName());
    }

    private boolean hasHeader(VerticalLayoutElement section) {
        return section.$(HorizontalLayoutElement.class).exists();
    }

    private HorizontalLayoutElement getHeader(VerticalLayoutElement section) {
        return section.$(HorizontalLayoutElement.class).first();
    }

    private TextFieldElement getTextFieldById(VerticalLayoutElement section, String id) {
        return section.$(TextFieldElement.class).id(id);
    }

    @BeforeEach
    public void setup() {
        clickButton(DynamicAnnotationsLayout.ID);
    }

    @Test
    public void testDynamicSectionCaption() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithoutButtonPmo.class);
        HorizontalLayoutElement header = getHeader(section);
        LabelElement caption = header.$(LabelElement.class).first();

        assertThat(caption.getText(), is("Dynamic caption"));

        TextFieldElement captionField = getTextFieldById(section,
                                                         DynamicCaptionWithoutButtonPmo.PROPERTY_DYNAMIC_CAPTION);
        captionField.setValue("bar");
        assertThat(caption.getText(), is("bar"));

        captionField.setValue("");
        assertThat(header.$(LabelElement.class).exists(), is(false));
        assertThat(hasHeader(section), is(false));
    }

    @Test
    public void testDynamicSectionCaption_EditButton() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithEditButtonPmo.class);
        HorizontalLayoutElement header = getHeader(section);
        LabelElement caption = header.$(LabelElement.class).first();

        assertThat(caption.getText(), is("Dynamic caption with edit button"));

        TextFieldElement captionField = getTextFieldById(section,
                                                         DynamicCaptionWithEditButtonPmo.PROPERTY_SECTION_CAPTION);
        captionField.setValue("bar");
        assertThat(caption.getText(), is("bar"));

        assertThat(header.$(LabelElement.class).all().size(), is(2));
        captionField.setValue("");
        assertThat(hasHeader(section), is(true));
        assertThat(header.$(LabelElement.class).all().size(), is(1));
    }

    @Test
    public void testDynamicSectionCaption_SectionHeaderButton() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithSectionHeaderButtonPmo.class);
        HorizontalLayoutElement header = getHeader(section);
        LabelElement caption = header.$(LabelElement.class).first();

        assertThat(caption.getText(), is("Dynamic caption with section header button"));

        TextFieldElement captionField = getTextFieldById(section,
                                                         DynamicCaptionWithSectionHeaderButtonPmo.PROPERTY_SECTION_CAPTION);
        captionField.setValue("bar");
        assertThat(caption.getText(), is("bar"));

        assertThat(header.$(LabelElement.class).all().size(), is(2));
        captionField.setValue("");
        assertThat(hasHeader(section), is(true));
        assertThat(header.$(LabelElement.class).all().size(), is(1));
    }

    @Test
    public void testDynamicSectionCaption_CloseButton() {
        VerticalLayoutElement section = getSection(DynamicCaptionWithCloseButtonPmo.class);
        HorizontalLayoutElement header = getHeader(section);
        LabelElement caption = header.$(LabelElement.class).first();

        assertThat(caption.getText(), is("Dynamic caption with close button"));

        TextFieldElement captionField = getTextFieldById(section,
                                                         DynamicCaptionWithCloseButtonPmo.PROPERTY_SECTION_CAPTION);
        captionField.setValue("☃");
        assertThat(caption.getText(), is("☃"));

        assertThat(header.$(LabelElement.class).all().size(), is(2));
        captionField.setValue("");
        assertThat(hasHeader(section), is(true));
        assertThat(header.$(LabelElement.class).all().size(), is(1));
    }

}