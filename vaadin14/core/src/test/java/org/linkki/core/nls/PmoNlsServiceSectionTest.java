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
package org.linkki.core.nls;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.nls.sample.SamplePmo;
import org.linkki.core.ui.aspects.CaptionAspectDefinition;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.vaadin.component.section.AbstractSection;
import org.linkki.core.vaadin.component.section.FormLayoutSection;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class PmoNlsServiceSectionTest {

    Label sectionHeader;

    private String textfieldLabelWithoutTranslation;

    private String textfieldLabelWithTranslation;

    private Button buttonWithTranslatedCaption;


    private String textfieldLabelTranslation;

    private String buttonLabelTranslation;

    private String buttonCaptionTranslation;

    private Button buttonWithoutTranslatedCaption;


    @BeforeEach
    public void setUp() {
        BindingContext context = new BindingContext();
        AbstractSection section = new PmoBasedSectionFactory().createSection(new SamplePmo(), context);
        HorizontalLayout header = (HorizontalLayout)section.getComponent(0);
        sectionHeader = (Label)header.getComponent(0);
        FormLayout sectionContent = ((FormLayoutSection)section).getSectionContent();
        textfieldLabelWithTranslation = sectionContent.getComponent(0).getCaption();
        textfieldLabelWithoutTranslation = sectionContent.getComponent(1).getCaption();
        buttonWithTranslatedCaption = (Button)sectionContent.getComponent(2);
        buttonWithoutTranslatedCaption = (Button)sectionContent.getComponent(3);

        // test setup
        PmoNlsService.get();
        textfieldLabelTranslation = getLabelTranslation(SamplePmo.PROPERTY_TEXTFIELD);
        assertThat(textfieldLabelTranslation, is(not(SamplePmo.PMO_LABEL)));
        buttonLabelTranslation = getLabelTranslation(SamplePmo.PROPERTY_MYBUTTON);
        assertThat(buttonLabelTranslation, is(not(SamplePmo.PMO_LABEL)));
        buttonCaptionTranslation = getCaptionTranslation(SamplePmo.PROPERTY_MYBUTTON);
        assertThat(buttonCaptionTranslation, is(not(SamplePmo.PMO_CAPTION)));

        assertThat(getLabelTranslation(SamplePmo.PROPERTY_CBFIELD), is(SamplePmo.PMO_LABEL));
        assertThat(getCaptionTranslation(SamplePmo.PROPERTY_MYBUTTON2), is(SamplePmo.PMO_CAPTION));
    }

    private String getLabelTranslation(String property) {
        return PmoNlsService.get().getLabel(SamplePmo.class, property, "label", SamplePmo.PMO_LABEL);
    }

    private String getCaptionTranslation(String property) {
        return PmoNlsService.get().getLabel(SamplePmo.class, property, CaptionAspectDefinition.NAME,
                                            SamplePmo.PMO_CAPTION);
    }

    @Test
    public void testSectionCaption() {
        assertThat(sectionHeader.getValue(), is("Translated section caption"));
    }

    @Test
    public void testCaptions() {
        assertThat(textfieldLabelWithTranslation, is(textfieldLabelTranslation));
        assertThat(textfieldLabelWithoutTranslation, is(SamplePmo.PMO_LABEL));
        assertThat(buttonWithTranslatedCaption.getCaption(), is(buttonCaptionTranslation));
        assertThat(buttonWithoutTranslatedCaption.getCaption(), is(SamplePmo.PMO_CAPTION));
    }
}
